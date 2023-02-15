package mod.bespectacled.modernbeta.util.chunk;

import mod.bespectacled.modernbeta.util.function.TriFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class ChunkBiome {
    private final BiomeSection biomeSections[];
    private final int worldMinY;
    
    public ChunkBiome(int chunkX, int chunkZ, int worldMinY, int sections, TriFunction<Integer, Integer, Integer, RegistryEntry<Biome>> chunkFunc) {
        this.biomeSections = new BiomeSection[sections];
        this.worldMinY = worldMinY;
        
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int startBiomeX = startX >> 2;
        int startBiomeZ = startZ >> 2;
        
        int minBiomeY = worldMinY >> 2;
        
        for (int section = 0; section < sections; ++section) {
            BiomeSection biomeSection = new BiomeSection();
            int biomeYOffset = section << 2;
            
            int ndx = 0;
            for (int localBiomeX = 0; localBiomeX <  4; ++localBiomeX) {
                for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeX = startBiomeX + localBiomeX;
                        int biomeZ = startBiomeZ + localBiomeZ;
                        int biomeY = (minBiomeY + biomeYOffset) + localBiomeY;
                        
                        biomeSection.setBiome(ndx++, chunkFunc.apply(biomeX, biomeY, biomeZ));
                    }
                }
            }
            
            this.biomeSections[section] = biomeSection;
        }
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        int sectionNdx = biomeY - (this.worldMinY >> 2);
        BiomeSection biomeSection = this.biomeSections[sectionNdx];
        
        int biomeNdxX = biomeX & 0x3;
        int biomeNdxY = biomeY & 0x3;
        int biomeNdxZ = biomeZ & 0x3;
        
        return biomeSection.getBiome(biomeNdxX * 4 + biomeNdxZ * 4 + biomeNdxY);
    }
    
    private class BiomeSection {
        private final Object biomes[] = new Object[64];
        
        public void setBiome(int ndx, RegistryEntry<Biome> biome) {
            this.biomes[ndx] = biome;
        }
        
        @SuppressWarnings("unchecked")
        public RegistryEntry<Biome> getBiome(int ndx) {
            return (RegistryEntry<Biome>)this.biomes[ndx];
        }
    }
}
