package mod.bespectacled.modernbeta.world.biome.provider.climate;

import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class BetaClimateMapping {
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
    
    public BetaClimateMapping(RegistryKey<Biome> biome, RegistryKey<Biome> oceanBiome, RegistryKey<Biome> deepOceanBiome) {
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
    
    public static BetaClimateMapping fromCompound(NbtCompound compound) {
        return new BetaClimateMapping(
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.BIOME, compound))),
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound))),
            key(new Identifier(NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound)))
        );
    }
    
    private static RegistryKey<Biome> key(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }
}
