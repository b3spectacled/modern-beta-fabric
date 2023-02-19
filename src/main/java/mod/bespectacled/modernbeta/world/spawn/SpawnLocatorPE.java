package mod.bespectacled.modernbeta.world.spawn;

import java.util.Optional;

import org.apache.logging.log4j.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.data.ModernBetaTagProviderBiome;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.mersenne.MTRandom;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

/*
 * Port of MCPE v0.6.0 alpha player spawn locator.
 * 
 */
public class SpawnLocatorPE implements SpawnLocator {
    private final MTRandom rand;
    
    private final ChunkProvider chunkProvider;
    private final PerlinOctaveNoise beachOctaveNoise;
    
    public SpawnLocatorPE(ChunkProvider chunkProvider, PerlinOctaveNoise beachOctaveNoise) {
        this.rand = new MTRandom();
        
        this.chunkProvider = chunkProvider;
        this.beachOctaveNoise = beachOctaveNoise;
    }
    
    @Override
    public Optional<BlockPos> locateSpawn() {
        ModernBeta.log(Level.INFO, "Setting a PE beach spawn..");
        
        int x = 0;
        int z = 0;
        int attempts = 0;
        
        while(!this.isSandAt(x, z, null)) {
            if (attempts > 10000) {
                ModernBeta.log(Level.INFO, "Exceeded spawn attempts, spawning anyway at 128,128..");
                
                x = 128;
                z = 128;
                break;
            }
            
            x += this.rand.nextInt(32) - this.rand.nextInt(32);
            z += this.rand.nextInt(32) - this.rand.nextInt(32);
            
            // Keep spawn pos within bounds of original PE world size
            if (x < 4) x += 32;
            if (x >= 251) x -= 32;
            
            if (z < 4) z += 32;
            if (z >= 251) z -= 32;
            
            attempts++;
        }
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        
        return Optional.of(new BlockPos(x, y, z));
    }

    private boolean isSandAt(int x, int z, HeightLimitView world) {
        double eighth = 0.03125D;
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        int y = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        
        RegistryEntry<Biome> biome = (this.chunkProvider.getChunkGenerator().getBiomeSource() instanceof ModernBetaBiomeSource oldBiomeSource) ? 
            oldBiomeSource.getBiomeForSpawn(x, y, z) :
            this.chunkProvider.getBiome(x >> 2, y >> 2, z >> 2, null);
        
        return 
            (biome.isIn(ModernBetaTagProviderBiome.SURFACE_CONFIG_SAND) && y >= seaLevel) || 
            (this.beachOctaveNoise.sample(x * eighth, z * eighth, 0.0) > 0.0 && y >= seaLevel && y <= seaLevel + 2);
    }
}
