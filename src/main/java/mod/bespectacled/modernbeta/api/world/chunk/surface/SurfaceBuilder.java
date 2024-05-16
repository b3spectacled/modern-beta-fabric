package mod.bespectacled.modernbeta.api.world.chunk.surface;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class SurfaceBuilder {    
    private final Map<RegistryEntry<Biome>, SurfaceConfig> surfaceConfigs;
    
    public SurfaceBuilder(BiomeSource biomeSource) {
        this.surfaceConfigs = new LinkedHashMap<>();
        
        this.initMap(biomeSource);
    }
    
    public SurfaceConfig getSurfaceConfig(RegistryEntry<Biome> biome) {
        return this.surfaceConfigs.computeIfAbsent(biome, (k) -> SurfaceConfig.getSurfaceConfig(biome));
    }
    
    private void initMap(BiomeSource biomeSource) {
        biomeSource.getBiomes().stream().forEach(biome -> {
            this.surfaceConfigs.put(biome, SurfaceConfig.getSurfaceConfig(biome));
        });
    }
}
