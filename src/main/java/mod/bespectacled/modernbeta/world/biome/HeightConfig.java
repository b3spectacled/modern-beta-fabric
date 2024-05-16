package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.List;

public record HeightConfig(float depth, float scale, byte type) {
	public static final HeightConfig DEFAULT = new HeightConfig(0.1F, 0.3F);
	public static final HeightConfig OCEAN = new HeightConfig(-1.0F, 0.4F);
	public static final HeightConfig DESERT = new HeightConfig(0.1F, 0.2F);
	public static final HeightConfig EXTREME_HILLS = new HeightConfig(0.2F, 1.3F);
	public static final HeightConfig BETA_HILLS = new HeightConfig(0.2F, 1.8F);
	public static final HeightConfig TAIGA = new HeightConfig(0.1F, 0.4F);
	public static final HeightConfig SWAMPLAND = new HeightConfig(-0.2F, 0.1F);
	public static final HeightConfig RIVER = new HeightConfig(-0.5F, 0.0F);
	public static final HeightConfig MOUNTAINS = new HeightConfig(0.2F, 1.2F, 1);
	public static final HeightConfig MUSHROOM_ISLAND = new HeightConfig(0.2F, 1.0F);
	public static final HeightConfig MUSHROOM_ISLAND_SHORE = new HeightConfig(-1.0F, 0.1F, 1);
	public static final HeightConfig BEACH = new HeightConfig(0.0F, 0.1F);
	public static final HeightConfig HILLS = new HeightConfig(0.2F, 0.7F, 1);
	public static final HeightConfig SHORT_HILLS = new HeightConfig(0.2F, 0.6F, 1);
	public static final HeightConfig EXTREME_HILLS_EDGE = new HeightConfig(0.2F, 0.8F, 1);
	public static final HeightConfig JUNGLE = new HeightConfig(0.2F, 0.4F);
	public static final HeightConfig JUNGLE_HILLS = new HeightConfig(1.8F, 0.2F, 1);
	public static final HeightConfig PLATEAU = new HeightConfig(1.8F, 0.2F);
	public static final HeightConfig SWAMPLAND_HILLS = new HeightConfig(-0.1F, 0.5F, 1);
	public static final HeightConfig PLATEAU_HILL = new HeightConfig(1.8F, 0.2F, 1);
	public static final HeightConfig DEEP_OCEAN = new HeightConfig(-1.8F, 0.2F);

	public HeightConfig(float depth, float scale) {
		this(depth, scale, 0);
	}

	public HeightConfig(float depth, float scale, int type) {
		this(depth, scale, (byte) type);
	}

	public static HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        List<HeightConfig> configs = ModernBetaRegistries.HEIGHT_CONFIG.getKeySet()
            .stream()
            .filter(id -> biomeInfo.biome().isIn(keyOf(id)))
	        .map(ModernBetaRegistries.HEIGHT_CONFIG::get)
            .filter(config -> config.type == biomeInfo.type())
	        .toList();
		return configs.stream().filter(config -> biomeInfo.type() == config.type).findAny()
				.orElse(configs.stream().findAny().orElse(DEFAULT));
	}

    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(id));
    }

	@Override
	public String toString() {
		return String.format(
			"[depth=%.3f, scale=%.3f]",
			this.depth,
			this.scale
		);
	}
}
