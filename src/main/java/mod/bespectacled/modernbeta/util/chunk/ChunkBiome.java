package mod.bespectacled.modernbeta.util.chunk;

import java.util.ArrayList;
import java.util.List;

import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import net.minecraft.data.client.BlockStateVariantMap.QuadFunction;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class ChunkBiome {
    private final BiomeSection biomeSections[];
    private final int worldMinY;
    
    public ChunkBiome(int chunkX, int chunkZ, int worldMinY, int sections, QuadFunction<BiomeInjectionContext, Integer, Integer, Integer, RegistryEntry<Biome>> chunkFunc) {
        this.biomeSections = new BiomeSection[sections];
        this.worldMinY = worldMinY;
        
        int startX = chunkX << 4;
        int startZ = chunkZ << 4;
        
        int startBiomeX = startX >> 2;
        int startBiomeZ = startZ >> 2;
        
        for (int section = 0; section < sections; ++section) {
            BiomeSection biomeSection = new BiomeSection();

            int ndx = 0;
            for (int biomeX = startBiomeX; biomeX < startBiomeX + 4; ++biomeX) {
                for (int biomeZ = startBiomeZ; biomeZ < startBiomeZ + 4; ++biomeZ) {
                    for (int biomeY = 0; biomeY < 4; ++biomeY) {
                        biomeSection.setBiome(ndx++, chunkFunc.apply(null, biomeX, biomeY, biomeZ));
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
        private final List<RegistryEntry<Biome>> biomes = new ArrayList<>(64);
        
        public void setBiome(int ndx, RegistryEntry<Biome> biome) {
            this.biomes.set(ndx, biome);
        }
        
        public RegistryEntry<Biome> getBiome(int ndx) {
            return this.biomes.get(ndx);
        }
    }
}
