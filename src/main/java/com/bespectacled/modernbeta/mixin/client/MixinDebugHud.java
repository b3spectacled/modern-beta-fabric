package com.bespectacled.modernbeta.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public class MixinDebugHud {
    @Shadow private MinecraftClient client;
    
    @Inject(method = "getLeftText", at = @At("TAIL"))
    private void injectGetLeftText(CallbackInfoReturnable<List<String>> info) {
        BlockPos pos = this.client.getCameraEntity().getBlockPos();
        int x = pos.getX();
        int z = pos.getZ();
        
        IntegratedServer integratedServer = this.client.getServer();
        ServerWorld serverWorld = null;
        
        if (integratedServer != null) {
            serverWorld = integratedServer.getWorld(this.client.world.getRegistryKey());
        }
        
        if (serverWorld != null) {
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (
                biomeSource instanceof OldBiomeSource oldBiomeSource && 
                oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler
            ) {
                info.getReturnValue().add(
                    String.format(
                        "Climate Temp: %.3f Rainfall: %.3f", 
                        climateSampler.sampleTemp(x, z), 
                        climateSampler.sampleRain(x, z)
                    )
                );
            }
        }
    }
}
