package mod.bespectacled.modernbeta.world.biome.provider.climate;

import mod.bespectacled.modernbeta.util.NbtTags;

public enum ClimateType {
    LAND(NbtTags.BIOME),
    OCEAN(NbtTags.OCEAN_BIOME),
    DEEP_OCEAN(NbtTags.DEEP_OCEAN_BIOME)
    ;
    
    public final String tag;
    
    private ClimateType(String tag) {
        this.tag = tag;
    }
}