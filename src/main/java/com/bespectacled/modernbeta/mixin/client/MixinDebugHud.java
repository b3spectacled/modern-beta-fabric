package com.bespectacled.modernbeta.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
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
        
        if (serverWorld != null && ModernBeta.DEV_ENV) {
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (biomeSource instanceof OldBiomeSource oldBiomeSource) {
                if (oldBiomeSource.getBiomeProvider() instanceof ClimateBiomeProvider climateBiomeProvider) {
                    Clime clime = climateBiomeProvider.getClimateSampler().sampleClime(x, z);
                    double temp = clime.temp();
                    double rain = clime.rain();
                    
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Climate Temp: %.3f Rainfall: %.3f", 
                            temp, 
                            rain
                        )
                    );
                }
            }
            
            if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator) {
                ChunkProvider chunkProvider = oldChunkGenerator.getChunkProvider();
                
                info.getReturnValue().add(
                    String.format(
                        "[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d", 
                        chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, null),
                        chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR, null),
                        chunkProvider.getSeaLevel()
                    )
                );
            }
        }
    }
}