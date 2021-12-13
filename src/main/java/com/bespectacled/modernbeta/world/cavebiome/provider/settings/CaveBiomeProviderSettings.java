package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtListBuilder;
import com.bespectacled.modernbeta.util.NbtTags;

import net.minecraft.nbt.NbtCompound;

public class CaveBiomeProviderSettings {
    private static final ModernBetaConfigCaveBiome CONFIG = ModernBeta.CAVE_BIOME_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String caveBiomeType) {
        return new NbtCompoundBuilder().putString(NbtTags.CAVE_BIOME_TYPE, caveBiomeType);
    }
    
    public static NbtCompound createSettingsDefault(String caveBiomeType) {
        return createSettingsBase(caveBiomeType).build();
    }
    
    public static NbtCompound createSettingsNone() {
        return createSettingsBase(BuiltInTypes.CaveBiome.NONE.name).build();
    }
    
    public static NbtCompound createSettingsSingle() {
        return createSettingsBase(BuiltInTypes.CaveBiome.SINGLE.name)
            .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
            .build();
    }
    
    public static NbtCompound createSettingsVoronoi() {
        NbtListBuilder builder = new NbtListBuilder();
        CONFIG.voronoiPoints.forEach(p -> {
            builder.add(p.toCompound());
        });
        
        return createSettingsBase(BuiltInTypes.CaveBiome.VORONOI.name)
            .putInt(NbtTags.VERTICAL_NOISE_SCALE, CONFIG.verticalNoiseScale)
            .putInt(NbtTags.HORIZONTAL_NOISE_SCALE, CONFIG.horizontalNoiseScale)
            .putList(NbtTags.BIOMES, builder.build())
            .build();
    }
}
