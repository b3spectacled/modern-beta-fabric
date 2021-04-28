package com.bespectacled.modernbeta.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.world.biome.provider.BetaBiomeProvider;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

/*
 * Based on Colormatic ClientWorldMixin
 */
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld extends World {
    
    @Unique private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
    
    @Unique private BlockPos curBlockPos = new BlockPos(0, 0, 0);
    
    @Unique private boolean useBetaBiomeColors = false;
    @Unique private boolean isOverworld = false;
    
    @Shadow private MinecraftClient client;

    private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPlayNetworkHandler netHandler, ClientWorld.Properties properties,
            RegistryKey<World> worldKey, DimensionType dimensionType, int loadDistance, Supplier<Profiler> profiler,
            WorldRenderer renderer, boolean debugWorld, long seed, CallbackInfo ci) {
        
        boolean useBetaBiomeColors = BETA_CONFIG.rendering_config.useFixedSeed;
        long worldSeed = BETA_CONFIG.rendering_config.fixedSeed;
        
        if (client.getServer() != null) { // Server check
           BiomeSource biomeSource = client.getServer().getOverworld().getChunkManager().getChunkGenerator().getBiomeSource();
           
           if (!BETA_CONFIG.rendering_config.useFixedSeed && 
               biomeSource instanceof OldBiomeSource && 
               ((OldBiomeSource)biomeSource).isProviderInstanceOf(BetaBiomeProvider.class)
           ) {
               useBetaBiomeColors = true;
               worldSeed = client.getServer().getOverworld().getSeed();
           }
        }
        
        this.isOverworld = worldKey.getValue().equals(DimensionType.OVERWORLD_REGISTRY_KEY.getValue());
        this.useBetaBiomeColors = useBetaBiomeColors;
        
        ModernBeta.setBlockColorsSeed(worldSeed, useBetaBiomeColors);
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At("HEAD"),
        index = 1
    )
    private BlockPos captureBlockPos(BlockPos pos) {
        curBlockPos = pos;
        
        return pos;
    }
    
    @ModifyVariable(
        method = "method_23777",
        at = @At(value = "INVOKE_ASSIGN",  target = "Lnet/minecraft/world/biome/Biome;getSkyColor()I"),
        index = 6  
    )
    private int injectBetaSkyColor(int skyColor) {
        if (this.useBetaBiomeColors && BETA_CONFIG.rendering_config.renderBetaSkyColor && this.isOverworld) {
            skyColor = BetaClimateSampler.INSTANCE.getSkyColor(curBlockPos.getX(), curBlockPos.getZ());
        }
        
        return skyColor;
    }
}
