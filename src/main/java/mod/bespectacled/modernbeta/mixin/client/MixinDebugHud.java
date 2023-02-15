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
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClime;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @Shadow private MinecraftClient client;
    
    @Inject(method = "getLeftText", at = @At("TAIL"))
    private void injectGetLeftText(CallbackInfoReturnable<List<String>> info) {
        BlockPos pos = this.client.getCameraEntity().getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        IntegratedServer integratedServer = this.client.getServer();
        ServerWorld serverWorld = null;
        
        if (integratedServer != null) {
            serverWorld = integratedServer.getWorld(this.client.world.getRegistryKey());
        }
        
        if (serverWorld != null && ModernBeta.DEV_ENV) {
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
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
                
                if (modernBetaBiomeSource.getCaveBiomeProvider() instanceof CaveClimateSampler climateSampler) {
                    CaveClime clime = climateSampler.sample(x >> 2, y >> 2, z >> 2);
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
            
            if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
                
                info.getReturnValue().add(
                    String.format(
                        "[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d", 
                        chunkProvider.getHeight(x, z, Type.WORLD_SURFACE_WG),
                        chunkProvider.getHeight(x, z, Type.OCEAN_FLOOR),
                        chunkProvider.getSeaLevel()
                    )
                );
                
                if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Noise Chunk Provider WSF height: %d", 
                            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                        )
                    );
                }

                /*
                int worldMinY = modernBetaChunkGenerator.getMinimumY();
                int minHeight = modernBetaChunkGenerator.getBiomeInjector().sampleMinHeightAround(biomeX, biomeZ);
                BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, -1, minHeight).setY(y);
                
                boolean canPlaceCave = BiomeInjector.CAVE_PREDICATE.test(context);
                
                info.getReturnValue().add(
                    String.format(
                        "[Modern Beta] Valid cave biome position: %b",
                        canPlaceCave
                    )
                );
                */

                if (modernBetaChunkGenerator.getBiomeInjector() != null) {
                    RegistryEntry<Biome> biome = modernBetaChunkGenerator.getBiomeInjector().getBiomeAtBlock(x, y, z, null);
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Injected biome: %s",
                            biome.getKey().get().getValue().toString()
                        )
                   );
                }
            }
        }
    }
}
