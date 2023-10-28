package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.List;

public record HeightConfig(float surfaceHeight, float heightStretch, boolean special) {
	public static final HeightConfig DEFAULT = new HeightConfig(0.1F, 0.3F);
	public static final HeightConfig OCEAN = new HeightConfig(-1.0F, 0.4F);
	public static final HeightConfig BETA_OCEAN = new HeightConfig(-1.0F, 0.5F);
	public static final HeightConfig DESERT = new HeightConfig(0.1F, 0.2F);
	public static final HeightConfig EXTREME_HILLS = new HeightConfig(0.2F, 1.3F);
	public static final HeightConfig BETA_HILLS = new HeightConfig(0.2F, 1.8F);
	public static final HeightConfig TAIGA = new HeightConfig(0.1F, 0.4F);
	public static final HeightConfig SWAMPLAND = new HeightConfig(-0.2F, 0.1F);
	public static final HeightConfig RIVER = new HeightConfig(-0.5F, 0.0F);
	public static final HeightConfig MOUNTAINS = new HeightConfig(0.2F, 1.2F, true);
	public static final HeightConfig MUSHROOM_ISLAND = new HeightConfig(0.2F, 1.0F);
	public static final HeightConfig MUSHROOM_ISLAND_SHORE = new HeightConfig(-1.0F, 0.1F, true);
	public static final HeightConfig BEACH = new HeightConfig(0.0F, 0.1F);
	public static final HeightConfig HILLS = new HeightConfig(0.2F, 0.7F, true);
	public static final HeightConfig SHORT_HILLS = new HeightConfig(0.2F, 0.6F, true);
	public static final HeightConfig HILLS_1_3 = new HeightConfig(0.3F, 0.8F, true);
	public static final HeightConfig SHORT_HILLS_1_3 = new HeightConfig(0.3F, 0.7F, true);
	public static final HeightConfig EXTREME_HILLS_EDGE = new HeightConfig(0.2F, 0.8F, true);
	public static final HeightConfig JUNGLE = new HeightConfig(0.2F, 0.4F);
	public static final HeightConfig JUNGLE_HILLS = new HeightConfig(1.8F, 0.2F, true);
	public static final HeightConfig PLATEAU = new HeightConfig(1.8F, 0.2F);
	public static final HeightConfig SWAMPLAND_HILLS = new HeightConfig(-0.1F, 0.5F, true);

	public HeightConfig(float surfaceHeight, float heightStretch) {
		this(surfaceHeight, heightStretch, false);
	}

	public static HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        List<HeightConfig> configs = ModernBetaRegistries.HEIGHT_CONFIG.getKeySet()
            .stream()
            .filter(id -> biomeInfo.biome().isIn(keyOf(id)))
	        .map(ModernBetaRegistries.HEIGHT_CONFIG::get)
            .filter(config -> !config.special || biomeInfo.special())
	        .toList();
		return configs.stream().filter(config -> biomeInfo.special() == config.special).findAny()
				.orElse(configs.stream().filter(config -> !config.special).findAny().orElse(DEFAULT));
	}

	public static boolean hasSpecialVariant(RegistryEntry<Biome> biome) {
		return ModernBetaRegistries.HEIGHT_CONFIG.getKeySet()
            .stream()
            .filter(id -> biome.isIn(keyOf(id)))
	        .map(ModernBetaRegistries.HEIGHT_CONFIG::get)
			.anyMatch(HeightConfig::special);
	}

    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }
}
