package mod.bespectacled.modernbeta.world.cavebiome.provider.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.settings.ImmutableSettings;
import mod.bespectacled.modernbeta.util.settings.Settings;

public class CaveBiomeProviderSettings {
    private static final ModernBetaConfigCaveBiome CONFIG = ModernBeta.CAVE_BIOME_CONFIG;
    
    private static NbtCompoundBuilder createSettingsBase(String caveBiomeType) {
        return new NbtCompoundBuilder().putString(NbtTags.CAVE_BIOME_PROVIDER, caveBiomeType);
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
                .putFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, CONFIG.voronoiVerticalNoiseScale)
                .putFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, CONFIG.voronoiHorizontalNoiseScale)
                .putList(NbtTags.BIOMES, builder.build())
                .build()
        );
    }
}
