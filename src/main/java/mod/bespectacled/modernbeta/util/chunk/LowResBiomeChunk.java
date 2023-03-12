package mod.bespectacled.modernbeta.util.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LowResBiomeChunk {
    private final List<RegistryEntry<Biome>> biomes = new ArrayList<>(16);
    
    public LowResBiomeChunk(int chunkX, int chunkZ, BiFunction<Integer, Integer, RegistryEntry<Biome>> chunkFunc) {
        int startBiomeX = chunkX << 2;
        int startBiomeZ = chunkZ << 2;
        
        int ndx = 0;
        for (int biomeX = startBiomeX; biomeX < startBiomeX + 4; ++biomeX) {
            for (int biomeZ = startBiomeZ; biomeZ < startBiomeZ + 4; ++biomeZ) {
                this.biomes.add(ndx, chunkFunc.apply(biomeX, biomeZ));
                ndx++;
            }
        }
    }
    
    public RegistryEntry<Biome> sampleBiome(int biomeX, int biomeZ) {
        return this.biomes.get((biomeZ & 0x3) + (biomeX & 0x3) * 4);
    }
}
