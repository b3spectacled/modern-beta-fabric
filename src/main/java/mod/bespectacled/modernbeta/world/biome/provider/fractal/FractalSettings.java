package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FractalSettings {
    public final List<BiomeInfo> biomes;
    public final List<ClimaticBiomeList<BiomeInfo>> climaticBiomes;
    public final Map<BiomeInfo, BiomeInfo> hillVariants;
    public final Map<BiomeInfo, BiomeInfo> edgeVariants;
    public final Map<BiomeInfo, BiomeInfo> mutatedVariants;
    public final Map<BiomeInfo, BiomeInfo> veryRareVariants;
    public final Map<BiomeInfo, List<BiomeInfo>> subVariants;
	public final RegistryEntry<Biome> plains;
	public final RegistryEntry<Biome> icePlains;
	public final int biomeScale;
	public final int hillScale;
	public final int subVariantScale;
	public final int subVariantSeed;
	public final int beachShrink;
	public final int oceanShrink;
    public final TerrainType terrainType;
    public final boolean oceans;
    public final boolean addRivers;
    public final boolean addSnow;
    public final boolean addMushroomIslands;
    public final boolean addBeaches;
    public final boolean addHills;
    public final boolean addSwampRivers;
    public final boolean addDeepOceans;
    public final boolean addMutations;
    public final boolean addClimaticOceans;
    public final boolean useClimaticBiomes;

	public FractalSettings(FractalSettings.Builder builder) {
		this.biomes = builder.biomes;
		this.climaticBiomes = builder.climaticBiomes;
		this.hillVariants = builder.hillVariants;
		this.edgeVariants = builder.edgeVariants;
		this.mutatedVariants = builder.mutatedVariants;
		this.veryRareVariants = builder.veryRareVariants;
		this.subVariants = builder.subVariants;
		this.plains = builder.plains;
		this.icePlains = builder.icePlains;
		this.biomeScale = builder.biomeScale;
		this.hillScale = builder.hillScale;
		this.subVariantScale = builder.subVariantScale;
		this.subVariantSeed = builder.subVariantSeed;
		this.beachShrink = builder.beachShrink;
		this.oceanShrink = builder.oceanShrink;
		this.terrainType = builder.terrainType;
		this.oceans = builder.oceans;
		this.addRivers = builder.addRivers;
		this.addSnow = builder.addSnow;
		this.addMushroomIslands = builder.addMushroomIslands;
		this.addBeaches = builder.addBeaches;
		this.addHills = builder.addHills;
		this.addSwampRivers = builder.addSwampRivers;
		this.addDeepOceans = builder.addDeepOceans;
		this.addMutations = builder.addMutations;
		this.addClimaticOceans = builder.addClimaticOceans;
		this.useClimaticBiomes = builder.useClimaticBiomes;
	}

	public static List<String> listFromReader(String tag, NbtReader reader, List<String> alternate) {
		if (reader.contains(tag)) {
			return reader.readListOrThrow(tag)
				.stream()
				.map(NbtElement::asString)
				.toList();
		}

		return List.copyOf(alternate);
	}

	public static NbtList listToNbt(List<String> list) {
		NbtListBuilder builder = new NbtListBuilder();
		list.forEach(b -> builder.add(NbtString.of(b)));

		return builder.build();
	}

	public static Map<String, String> mapFromReader(String tag, NbtReader reader, Map<String, String> alternate) {
		if (reader.contains(tag)) {
			Map<String, String> map = new HashMap<>();
			NbtCompound compound = reader.readCompoundOrThrow(tag);
			compound.getKeys().forEach(key -> map.put(key, compound.getString(key)));
			return map;
		}

		return Map.copyOf(alternate);
	}

	public static NbtCompound mapToNbt(Map<String, String> map) {
		NbtCompoundBuilder builder = new NbtCompoundBuilder();
		map.forEach(builder::putString);

		return builder.build();
	}

	public static Map<String, List<String>> mapOfListFromReader(String tag, NbtReader reader, Map<String, List<String>> alternate) {
		if (reader.contains(tag)) {
			Map<String, List<String>> map = new HashMap<>();
			NbtCompound compound = reader.readCompoundOrThrow(tag);
			NbtReader compoundReader = new NbtReader(compound);
			compound.getKeys().forEach(key -> map.put(key, compoundReader.readListOrThrow(key)
					.stream()
					.map(NbtElement::asString)
					.toList()));
			return map;
		}

		return Map.copyOf(alternate);
	}

	public static NbtCompound mapOfListToNbt(Map<String, List<String>> map) {
		NbtCompoundBuilder builder = new NbtCompoundBuilder();
		map.forEach((k, v) -> builder.putList(k, listToNbt(v)));

		return builder.build();
	}

	public static class Builder {
	    public List<BiomeInfo> biomes;
	    public List<ClimaticBiomeList<BiomeInfo>> climaticBiomes;
	    public Map<BiomeInfo, BiomeInfo> hillVariants;
	    public Map<BiomeInfo, BiomeInfo> edgeVariants;
	    public Map<BiomeInfo, BiomeInfo> mutatedVariants;
	    public Map<BiomeInfo, BiomeInfo> veryRareVariants;
	    public Map<BiomeInfo, List<BiomeInfo>> subVariants;
		public RegistryEntry<Biome> plains;
		public RegistryEntry<Biome> icePlains;
		public int biomeScale;
		public int hillScale;
		public int subVariantScale;
		public int subVariantSeed;
		public int beachShrink;
		public int oceanShrink;
	    public TerrainType terrainType;
	    public boolean oceans;
	    public boolean addRivers;
	    public boolean addSnow;
	    public boolean addMushroomIslands;
	    public boolean addBeaches;
	    public boolean addHills;
	    public boolean addSwampRivers;
	    public boolean addDeepOceans;
	    public boolean addMutations;
	    public boolean addClimaticOceans;
	    public boolean useClimaticBiomes;

		public Builder() {
			this.biomes = List.of();
			this.hillVariants = Map.of();
			this.edgeVariants = Map.of();
			this.mutatedVariants = Map.of();
			this.veryRareVariants = Map.of();
			this.subVariants = Map.of();
			this.plains = null;
			this.icePlains = null;
			this.biomeScale = 4;
			this.hillScale = 2;
			this.subVariantScale = 2;
			this.subVariantSeed = 3000;
			this.beachShrink = 1;
			this.oceanShrink = 0;
			this.terrainType = TerrainType.BETA;
			this.oceans = true;
			this.addRivers = true;
			this.addSnow = false;
			this.addMushroomIslands = false;
			this.addBeaches = false;
			this.addHills = false;
			this.addSwampRivers = false;
			this.addDeepOceans = false;
			this.addMutations = false;
			this.addClimaticOceans = false;
			this.useClimaticBiomes = false;
		}

        public FractalSettings build() {
            return new FractalSettings(this);
        }
	}

	public enum TerrainType {
		BETA("beta"),
		EARLY_RELEASE("early_release"),
		MAJOR_RELEASE("major_release")
        ;

		public final String id;

		private TerrainType(String id) {
			this.id = id;
		}

		public static TerrainType fromString(String id) {
			for (TerrainType fractalTerrain : values()) {
				if (fractalTerrain.id.equals(id)) {
					return fractalTerrain;
				}
			}

			return BETA;
		}
	}
}
