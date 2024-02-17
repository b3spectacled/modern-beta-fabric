package mod.bespectacled.modernbeta.world.spawn;

import com.mojang.datafixers.util.Pair;
import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.slf4j.event.Level;

import java.util.Optional;
import java.util.Random;

/*
 * Port of Beta 1.7.3 player spawn locator.
 * 
 */
public class SpawnLocatorEarlyRelease implements SpawnLocator {
    private final Random rand;

    private final ChunkProvider chunkProvider;
    private final BiomeSource biomeSource;

    public SpawnLocatorEarlyRelease(ChunkProvider chunkProvider, Random rand) {
        this.rand = rand;
        
        this.chunkProvider = chunkProvider;
        this.biomeSource = chunkProvider.getChunkGenerator().getBiomeSource();
    }

    @Override
    public Optional<BlockPos> locateSpawn() {
        ModernBeta.log(Level.INFO, "Setting a grass spawn..");

        int x = 0;
        int z = 0;
        int attempts = 0;

        BlockPos suitableBiomeLocation = getSuitableBiomeLocation(0, 0, 256);
        if (suitableBiomeLocation != null) {
            x = suitableBiomeLocation.getX();
            z = suitableBiomeLocation.getZ();
        } else {
            ModernBeta.log(Level.INFO, "Unable to find spawn biome");
        }
        
        while(!this.isGrassAt(x, z, null)) {
            if (attempts > 10000) {
                ModernBeta.log(Level.INFO, "Exceeded spawn attempts, spawning anyway at 0,0..");
                
                x = 0;
                z = 0;
                break;
            }
            
            x += this.rand.nextInt(64) - this.rand.nextInt(64);
            z += this.rand.nextInt(64) - this.rand.nextInt(64);
            
            attempts++;
        }
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        
        return Optional.of(new BlockPos(x, y, z));
    }

    private BlockPos getSuitableBiomeLocation(int x, int z, int radius) {
        int minX = x - radius >> 2;
        int minZ = z - radius >> 2;
        int maxX = x + radius >> 2;
        int maxZ = z + radius >> 2;
        int width = maxX - minX + 1;
        int length = maxZ - minZ + 1;
        BlockPos position = null;
        int choiceChance = 1;

        for (int i = 0; i < width * length; i++) {
            int biomeX = minX + i % width;
            int biomeZ = minZ + i / width;
            RegistryEntry<Biome> biome = this.biomeSource.getBiome(biomeX, 16, biomeZ, null);
            if (biome.isIn(ModernBetaTagProviderBiome.IS_EARLY_RELEASE_SPAWN)
                && (position == null || this.rand.nextInt(choiceChance) == 0)) {
                position = new BlockPos(biomeX << 2, 0, biomeZ << 2);
                choiceChance++;
            }
        }

        return position;
    }

    private boolean isGrassAt(int x, int z, HeightLimitView world) {
        int seaLevel = this.chunkProvider.getSeaLevel();

        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);

        return y >= seaLevel;
    }

}
