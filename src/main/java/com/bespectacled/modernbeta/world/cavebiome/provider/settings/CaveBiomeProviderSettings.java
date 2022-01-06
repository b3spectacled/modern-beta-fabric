package com.bespectacled.modernbeta.world.cavebiome.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtListBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.util.settings.Settings;

public class CaveBiomeProviderSettings {
    private static final ModernBetaConfigCaveBiome CONFIG = ModernBeta.CAVE_BIOME_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String caveBiomeType) {
        return new NbtCompoundBuilder().putString(NbtTags.CAVE_BIOME_TYPE, caveBiomeType);
    }
    
    public static Settings createSettingsDefault(String caveBiomeType) {
        return new ImmutableSettings(createSettingsBase(caveBiomeType).build());
    }
    
    public static Settings createSettingsNone() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.CaveBiome.NONE.name).build()
        );
    }
    
    public static Settings createSettingsSingle() {
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name)
                .putString(NbtTags.SINGLE_BIOME, CONFIG.singleBiome)
                .build()
        );
    }
    
    public static Settings createSettingsVoronoi() {
        NbtListBuilder builder = new NbtListBuilder();
        CONFIG.voronoiPoints.forEach(p -> {
            builder.add(p.toCompound());
        });
        
        return new ImmutableSettings(
            createSettingsBase(ModernBetaBuiltInTypes.CaveBiome.VORONOI.name)
                .putInt(NbtTags.VERTICAL_NOISE_SCALE, CONFIG.verticalNoiseScale)
                .putInt(NbtTags.HORIZONTAL_NOISE_SCALE, CONFIG.horizontalNoiseScale)
                .putList(NbtTags.BIOMES, builder.build())
                .build()
        );
    }
}
