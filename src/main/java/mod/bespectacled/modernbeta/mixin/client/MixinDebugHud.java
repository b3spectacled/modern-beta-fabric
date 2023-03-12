package mod.bespectacled.modernbeta.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import mod.bespectacled.modernbeta.world.biome.OldBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import mod.bespectacled.modernbeta.world.gen.OldChunkGenerator;
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
        int y = pos.getY();
        int z = pos.getZ();
        
        int biomeX = x >> 2;
        int biomeZ = z >> 2;
        
        IntegratedServer integratedServer = this.client.getServer();
        ServerWorld serverWorld = null;
        
        if (integratedServer != null) {
            serverWorld = integratedServer.getWorld(this.client.world.getRegistryKey());
        }
        
        if (serverWorld != null && ModernBeta.DEV_ENV) {
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (biomeSource instanceof OldBiomeSource oldBiomeSource) {
                if (oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
                    Clime clime = climateSampler.sample(x, z);
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
                
                if (oldBiomeSource.getCaveBiomeProvider() instanceof CaveClimateSampler climateSampler) {
                    Clime clime = climateSampler.sample(x >> 2, y >> 2, z >> 2);
                    double temp = clime.temp();
                    double rain = clime.rain();
                    
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Cave Climate Temp: %.3f Rainfall: %.3f",
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
                        chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG),
                        chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR),
                        chunkProvider.getSeaLevel()
                    )
                );
                
                if (chunkProvider instanceof NoiseChunkProvider noiseChunkProvider) {
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Noise Chunk Provider WSF height: %d", 
                            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR)
                        )
                    );
                }

                int worldMinY = oldChunkGenerator.getMinimumY();
                int minHeight = oldChunkGenerator.getBiomeInjector().sampleMinHeightAround(biomeX, biomeZ);
                BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, -1, minHeight, BlockStates.AIR, BlockStates.AIR).setY(y);
                
                boolean canPlaceCave = BiomeInjector.CAVE_PREDICATE.test(context);
                
                info.getReturnValue().add(
                    String.format(
                        "[Modern Beta] Valid cave position: %b",
                        canPlaceCave
                    )
                );
            }
        }
    }
}
