package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import mod.bespectacled.modernbeta.util.ModernBetaClientWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Unique private static Vec3d modernBeta_pos;
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;
    
    @Inject(method = "render", at = @At("HEAD"))
    private static void capturePos(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo info) {
        modernBeta_pos = camera.getPos();
    }
    
    @ModifyVariable(
        method = "render",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"
        ),
        ordinal = 1
    )
    private static int modifyWaterFogColor(int waterFogColor) {
        if (BlockColorSampler.INSTANCE.sampleWaterColor()) {
            int x = (int)modernBeta_pos.getX();
            int z = (int)modernBeta_pos.getZ();
            
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(x, z);
            
            return BlockColorSampler.INSTANCE.colorMapUnderwater.getColor(clime.temp(), clime.rain());
        }
        
        return waterFogColor;
    }
    
    @ModifyVariable(
        method = "render",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/DimensionEffects;getFogColorOverride(FF)[F")
    )
    private static float[] modifyFogSunsetCols(float[] skyCols) {
        return ModernBeta.RENDER_CONFIG.configOther.useAlphaSunset ? null : skyCols;
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientWorld world, int renderDistance, float skyDarkness, CallbackInfo info) {
        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = calculateFogWeight(renderDistance);
        }
        
        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaWorld = ((ModernBetaClientWorld)world).isModernBetaWorld();
    }
    
    @ModifyVariable(
        method = "render",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 7
    )
    private static float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernBeta.RENDER_CONFIG.configBiomeColor.useOldFogColor ? modernBeta_fogWeight : weight;
    }
    
    @Unique
    private static float calculateFogWeight(int renderDistance) {
        // Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance) 
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'
        
        int clampedDistance = MathHelper.clamp(renderDistance, 0, 16);
        clampedDistance = (int)((16 - clampedDistance) / (float)16 * 3);
        
        float weight = 1.0F / (float)(4 - clampedDistance);
        weight = 1.0F - (float)Math.pow(weight, 0.25);
        
        return weight;
    }
}
