package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Unique private static Vec3d modernBeta_pos;
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;
    
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
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 7
    )
    private static float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernBeta.CONFIG.useOldFogColor ? modernBeta_fogWeight : weight;
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
