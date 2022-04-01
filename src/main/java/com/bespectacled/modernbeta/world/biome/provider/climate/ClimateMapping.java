package com.bespectacled.modernbeta.world.biome.provider.climate;

import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ClimateMapping {
    public enum ClimateType {
        LAND(NbtTags.BIOME),
        OCEAN(NbtTags.OCEAN_BIOME),
        DEEP_OCEAN(NbtTags.DEEP_OCEAN_BIOME);
        
        public final String tag;
        
        private ClimateType(String tag) {
            this.tag = tag;
        }
    }
    
    private final RegistryKey<Biome> biome;
    private final RegistryKey<Biome> oceanBiome;
    private final RegistryKey<Biome> deepOceanBiome;
    
    public ClimateMapping(RegistryKey<Biome> biome, RegistryKey<Biome> oceanBiome, RegistryKey<Biome> deepOceanBiome) {
        this.biome = biome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
    }
    
    public RegistryKey<Biome> biome() {
        return this.biome;
    }
    
    public RegistryKey<Biome> oceanBiome() {
        return this.oceanBiome;
    }
    
    public RegistryKey<Biome> deepOceanBiome() {
        return this.deepOceanBiome;
    }
    
    public RegistryKey<Biome> biomeByClimateType(ClimateType type) {
        return switch(type) {
            case LAND -> this.biome;
            case OCEAN -> this.oceanBiome;
            case DEEP_OCEAN -> this.deepOceanBiome;
        };
    }
    
    public static ClimateMapping fromCompound(NbtCompound compound) {
        return new ClimateMapping(
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.BIOME, compound))),
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound))),
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)))
        );
    }
    
    private static RegistryKey<Biome> key(Identifier id) {
        return RegistryKey.of(Registry.BIOME_KEY, id);
    }
}
