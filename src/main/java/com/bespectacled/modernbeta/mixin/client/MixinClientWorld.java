package com.bespectacled.modernbeta.mixin.client;

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
import com.bespectacled.modernbeta.util.MutableClientWorld;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld extends World implements MutableClientWorld, BetaClimateResolver {
    @Unique private Vec3d curPos = new Vec3d(0, 0, 0);
    
    @Unique private boolean useBetaBiomeColors = false;
    @Unique private boolean isOverworld = false;
    
    @Shadow private MinecraftClient client;

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
        this.isOverworld = worldKey.getValue().equals(DimensionType.OVERWORLD_REGISTRY_KEY.getValue());
        
        boolean useBetaBiomeColors = ModernBeta.RENDER_CONFIG.useFixedSeed;
        long worldSeed = ModernBeta.RENDER_CONFIG.fixedSeed;
        
        if (client.getServer() != null) { // Server check
           BiomeSource biomeSource = client.getServer().getOverworld().getChunkManager().getChunkGenerator().getBiomeSource();
           
           if (!ModernBeta.RENDER_CONFIG.useFixedSeed && 
               biomeSource instanceof OldBiomeSource && 
               ((OldBiomeSource)biomeSource).isProviderInstanceOf(BetaBiomeProvider.class)
           ) {
               useBetaBiomeColors = this.isOverworld;
               worldSeed = client.getServer().getOverworld().getSeed();
           }
        }
        
        this.useBetaBiomeColors = useBetaBiomeColors;
        
        ModernBeta.setBlockColorsSeed(worldSeed, useBetaBiomeColors);
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private Vec3d captureBlockPos(Vec3d pos) {
        curPos = pos;
        return pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"),
        index = 6  
    )
    private Vec3d injectBetaSkyColor(Vec3d skyColorVec) {
        if (useBetaBiomeColors && ModernBeta.RENDER_CONFIG.renderBetaSkyColor && this.isOverworld) {
            int x = (int)curPos.getX();
            int z = (int)curPos.getZ();
            
            skyColorVec = Vec3d.unpackRgb(this.sampleSkyColor(x, z));
        }
        
        return skyColorVec;
    }
    
    @Override
    public boolean useBetaBiomeColors() {
        return this.useBetaBiomeColors;
    }
}

