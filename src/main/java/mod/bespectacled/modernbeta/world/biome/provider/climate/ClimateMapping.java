package mod.bespectacled.modernbeta.world.biome.provider.climate;

import java.util.LinkedHashMap;
import java.util.Map;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ClimateMapping {
    public String biome;
    public String oceanBiome;
    public String deepOceanBiome;
    
    public ClimateMapping(String biome, String oceanBiome) {
        this(biome, oceanBiome, oceanBiome);
    }
    
    public ClimateMapping(String biome, String oceanBiome, String deepOceanBiome) {
        this.biome = biome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
    }
    
    public RegistryKey<Biome> biomeByClimateType(ClimateType type) {
        return switch(type) {
            case LAND -> keyOf(this.biome);
            case OCEAN -> keyOf(this.oceanBiome);
            case DEEP_OCEAN -> keyOf(this.deepOceanBiome);
        };
    }
    
    public RegistryKey<Biome> biome() {
        return keyOf(this.biome);
    }
    
    public RegistryKey<Biome> oceanBiome() {
        return keyOf(this.oceanBiome);
    }
    
    public RegistryKey<Biome> deepOceanBiome() {
        return keyOf(this.deepOceanBiome);
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME, this.biome)
            .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
            .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
            .build();
    }

    public static ClimateMapping fromCompound(NbtCompound compound) {
        return new ClimateMapping(
            NbtUtil.readStringOrThrow(NbtTags.BIOME, compound),
            NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound),
            NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)
        );
    }
    
    public static Map<String, ClimateMapping> mapFromCompound(NbtCompound compound, Map<String, ClimateMapping> alternate) {
        if (compound.contains(NbtTags.CLIMATE_MAPPINGS)) {
            Map<String, ClimateMapping> map = new LinkedHashMap<>();
            NbtCompound biomes = NbtUtil.readCompoundOrThrow(NbtTags.CLIMATE_MAPPINGS, compound);
            biomes.getKeys().forEach(key -> map.put(key, ClimateMapping.fromCompound(NbtUtil.readCompoundOrThrow(key, biomes))));
            
            return map;
        }
        
        return Map.copyOf(alternate);
    }
    
    public static NbtCompound mapToCompound(Map<String, ClimateMapping> mappings) {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        mappings.keySet().forEach(key -> builder.putCompound(key, mappings.get(key).toCompound()));
        
        return builder.build();
    }
    
    private static RegistryKey<Biome> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
    }

}
