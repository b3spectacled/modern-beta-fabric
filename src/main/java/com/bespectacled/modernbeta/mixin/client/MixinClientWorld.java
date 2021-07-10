package com.bespectacled.modernbeta.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.BetaClimateResolver;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld extends World implements BetaClimateResolver {
    @Shadow private MinecraftClient client;
    
    @Unique private Vec3d curPos = new Vec3d(0, 0, 0);
    @Unique private boolean useBetaBiomeColors;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
        ClientPlayNetworkHandler netHandler, 
        ClientWorld.Properties properties,
        RegistryKey<World> worldKey, 
        DimensionType dimensionType, 
        int loadDistance, 
        Supplier<Profiler> profiler,
        WorldRenderer renderer, 
        boolean debugWorld, 
        long seed, 
        CallbackInfo ci
    ) {
        long worldSeed = ModernBeta.RENDER_CONFIG.fixedSeed;
        boolean useBetaBiomeColors = ModernBeta.RENDER_CONFIG.useFixedSeed;
        
        if (this.isClient && this.client.getServer() != null && worldKey != null) { // Server check
           BiomeSource biomeSource = this.client.getServer().getWorld(worldKey).getChunkManager().getChunkGenerator().getBiomeSource();

           worldSeed = this.client.getServer().getWorld(worldKey).getSeed();
           useBetaBiomeColors = 
               biomeSource instanceof OldBiomeSource oldBiomeSource &&
               oldBiomeSource.getBiomeProvider() instanceof BetaClimateResolver &&
               !ModernBeta.RENDER_CONFIG.useFixedSeed;
        }
        
        ModernBeta.setBlockColorsSeed(worldSeed, useBetaBiomeColors);
        
        this.useBetaBiomeColors = useBetaBiomeColors;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private Vec3d captureBlockPos(Vec3d pos) {
        return curPos = pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"),
        index = 6  
    )
    private Vec3d injectBetaSkyColor(Vec3d skyColorVec) {
        if (this.useBetaBiomeColors && ModernBeta.RENDER_CONFIG.renderBetaSkyColor) {
            int x = (int)curPos.getX();
            int z = (int)curPos.getZ();
            
            skyColorVec = Vec3d.unpackRgb(this.sampleSkyColor(x, z));
        }
        
        return skyColorVec;
    }
}

