package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Map;
import java.util.function.UnaryOperator;

public abstract class Layer {
	private long worldSeed;
	private long chunkSeed;
	private long baseSeed;
	private int cacheX;
	private int cacheZ;
	private int cacheWidth;
	private int cacheLength;
	private BiomeInfo[] cacheData;
	private final Object cacheLock = new Object();
	protected Layer parent;

	public static Layer getLayer(RegistryEntryLookup<Biome> biomeLookup, long seed, FractalSettings settings) {
		RegistryEntry<Biome> ocean = biomeLookup.getOrThrow(BiomeKeys.OCEAN);
		RegistryEntry<Biome> deepOcean = biomeLookup.getOrThrow(BiomeKeys.DEEP_OCEAN);
		RegistryEntry<Biome> plains = settings.plains;
		RegistryEntry<Biome> river = biomeLookup.getOrThrow(BiomeKeys.RIVER);
		RegistryEntry<Biome> frozenOcean = biomeLookup.getOrThrow(BiomeKeys.FROZEN_OCEAN);
		RegistryEntry<Biome> icePlains = settings.icePlains;
		RegistryEntry<Biome> frozenRiver = biomeLookup.getOrThrow(BiomeKeys.FROZEN_RIVER);
		RegistryEntry<Biome> mushroomIsland = biomeLookup.getOrThrow(BiomeKeys.MUSHROOM_FIELDS);
		RegistryEntry<Biome> beach = biomeLookup.getOrThrow(BiomeKeys.BEACH);

		Map<BiomeInfo, BiomeInfo> replacementBiomes = Map.ofEntries(
			Map.entry(DummyBiome.OCEAN.biomeInfo, BiomeInfo.of(ocean)),
			Map.entry(DummyBiome.FROZEN_OCEAN.biomeInfo, BiomeInfo.of(icePlains)),
			Map.entry(DummyBiome.ICE_PLAINS.biomeInfo, BiomeInfo.of(icePlains)),
			Map.entry(DummyBiome.MUSHROOM_ISLAND.biomeInfo, BiomeInfo.of(mushroomIsland)),
			Map.entry(DummyBiome.DEEP_OCEAN.biomeInfo, BiomeInfo.of(deepOcean))
		);

		Layer land;
		if (!settings.oceans) {
			land = new LayerSingleBiome(DummyBiome.PLAINS);
			land = addSnow(land, settings, Integer.MIN_VALUE);
			if (settings.addSnow || settings.useClimaticBiomes) {
				land = new LayerZoom(2002, land);
				land = new LayerZoom(2003, land);
			}
		} else {
			land = new LayerInitLand(1);
			switch (settings.terrainType) {
				case BETA:
					for (int i = 0; i < 5 - settings.oceanShrink; i++) {
						land = addSnow(land, settings, 3 - i);
						land = i == 0 ? new LayerFuzzyZoom(2000, land) : new LayerZoom(2000 + i, land);
						land = new LayerAddLandB18(Math.min(i + 1, 3), land);
					}
					break;

				case EARLY_RELEASE:
					for (int i = 0; i < 4 - settings.oceanShrink; i++) {
						land = addSnow(land, settings, 2 - i);
						land = i == 0 ? new LayerFuzzyZoom(2000, land) : new LayerZoom(2000 + i, land);
						land = new LayerAddLand(i + 1, land);
					}
					break;

				case MAJOR_RELEASE:
					land = addSnow(land, settings, 2);
					if (settings.oceanShrink <= 3) {
						land = new LayerFuzzyZoom(2000, land);
						land = new LayerAddLand(1, land);
					}
					land = addSnow(land, settings, 1);
					if (settings.oceanShrink <= 2) {
						land = new LayerZoom(2001, land);
						land = new LayerAddLand(2, land);
						land = new LayerAddLand(50, land);
						land = new LayerAddLand(70, land);
						land = new LayerReduceOcean(2, land);
						land = maybeAddSnow(land, settings, 0);
						land = new LayerAddLand(3, land);
						land = maybeAddClimateEdge(land, settings, 0);
					}
					if (settings.oceanShrink <= 1) {
						land = new LayerZoom(2002, land);
					}
					land = addSnow(land, settings, -1);
					if (settings.oceanShrink <= 0) {
						land = new LayerZoom(2003, land);
						land = new LayerAddLand(4, land);
					}
					for (int i = 0; i < -settings.oceanShrink; i++) {
						land = addSnow(land, settings, -2 - i);
						land = new LayerZoom(2004 + i, land);
						land = new LayerAddLand(5 + i, land);
					}
					break;
			}

			if (settings.addMushroomIslands) land = new LayerAddMushroomIsland(5, land);
			if (settings.addDeepOceans) land = new LayerDeepenOcean(4, land);
		}

		Layer riverLayout = new LayerInitRiver(100, land);
		Layer mutationLayout = new LayerInitMutation(100, land);
		mutationLayout = LayerZoom.multi(1000, mutationLayout, settings.hillScale);
		if (settings.terrainType == FractalSettings.TerrainType.MAJOR_RELEASE) {
			riverLayout = LayerZoom.multi(1000, mutationLayout, settings.biomeScale);
		} else {
			riverLayout = LayerZoom.multi(1000, riverLayout, settings.biomeScale + settings.hillScale);
		}
		riverLayout = new LayerComputeRiver(1, riverLayout, settings.terrainType != FractalSettings.TerrainType.MAJOR_RELEASE);
		riverLayout = new LayerSmooth(1000, riverLayout);

		Layer biomes = new LayerAddBiomes(200, land, settings.biomes, replacementBiomes, settings.climaticBiomes);
		for (int i = 0; i < settings.hillScale; i++) {
			if (settings.subVariantScale == i && !settings.subVariants.isEmpty()) biomes = new LayerSubVariants(settings.subVariantSeed, biomes, settings.subVariants);

			if (settings.beachShrink == i - 2 - settings.hillScale && settings.addBeaches) {
				biomes = new LayerAddEdge(1000, biomes, beach, ocean, mushroomIsland,
					settings.useClimaticBiomes ? null : settings.edgeVariants, settings.useClimaticBiomes ? null : biomeLookup);
			}

			biomes = new LayerZoom(1000 + i, biomes);
		}
		if (settings.subVariantScale == settings.hillScale && !settings.subVariants.isEmpty()) biomes = new LayerSubVariants(settings.subVariantSeed, biomes, settings.subVariants);
		if (settings.useClimaticBiomes) biomes = new LayerAddEdge(1000, biomes, settings.edgeVariants, biomeLookup);
		if (settings.addHills) {
			int neighborRequirement = settings.terrainType == FractalSettings.TerrainType.MAJOR_RELEASE ? 3 : 4;
			biomes = settings.addMutations
				? new LayerAddHills(1000, biomes, settings.hillVariants, biomeLookup, neighborRequirement, mutationLayout, settings.mutatedVariants)
				: new LayerAddHills(1000, biomes, settings.hillVariants, biomeLookup, neighborRequirement);
		}
		if (!settings.veryRareVariants.isEmpty()) biomes = new LayerAddRareBiomes(1001, biomes, settings.veryRareVariants);

		if (settings.beachShrink == -1 && settings.addBeaches) {
			biomes = new LayerAddEdge(1000, biomes, beach, ocean, mushroomIsland,
				settings.useClimaticBiomes ? null : settings.edgeVariants, settings.useClimaticBiomes ? null : biomeLookup);
		}

		for (int i = 0; i < settings.biomeScale; i++) {
			biomes = new LayerZoom(1000 + i, biomes);
			if (i == 0) {
				if (settings.oceans) biomes = settings.terrainType == FractalSettings.TerrainType.BETA
					? new LayerAddLandB18(3, biomes, ocean, plains, frozenOcean, icePlains)
					: new LayerAddLand(3, biomes, ocean, plains, frozenOcean, icePlains);
				if (!settings.addBeaches && settings.addMushroomIslands) biomes = new LayerMushroomIslandShore(biomes, mushroomIsland, ocean);
			}

			if (i == settings.beachShrink && settings.addBeaches) {
				biomes = new LayerAddEdge(1000, biomes, beach, ocean, mushroomIsland,
					settings.useClimaticBiomes ? null : settings.edgeVariants, settings.useClimaticBiomes ? null : biomeLookup);
			}
			if (i == 1 && settings.addSwampRivers) biomes = new LayerAddSwampRivers(1000, biomes, river);
		}

		biomes = new LayerSmooth(1000, biomes);
		if (settings.addRivers) biomes = new LayerApplyRiver(biomes, riverLayout, ocean, deepOcean, river, mushroomIsland, icePlains, frozenRiver);
		if (settings.addClimaticOceans && settings.oceans) {
			Layer oceanTemperature = new LayerOceanTemperature();
			oceanTemperature = LayerZoom.multi(2001, oceanTemperature, 6);
			biomes = new LayerApplyOceanTemperature(biomes, oceanTemperature, biomeLookup);
		}
		biomes.setWorldSeed(seed);
		return biomes;
	}

	private static Layer addSnow(Layer layer, FractalSettings settings, int oceanShrink) {
		if (settings.oceanShrink != oceanShrink && oceanShrink != Integer.MIN_VALUE) return layer;
		if (settings.useClimaticBiomes) {
			layer = new LayerAddClimate(2, layer);
			layer = new LayerCoolWarmEdge(2, layer);
			layer = new LayerHeatIceEdge(2, layer);
			layer = new LayerRareClimate(3, layer);
		} else if (settings.addSnow) layer = new LayerAddSnow(2, layer);
		return layer;
	}

	private static Layer maybeAddSnow(Layer layer, FractalSettings settings, int oceanShrink) {
		if (settings.oceanShrink != oceanShrink) return layer;
		if (settings.useClimaticBiomes) return new LayerAddClimate(2, layer);
		if (settings.addSnow) return new LayerAddSnow(2, layer);
		return layer;
	}

	private static Layer maybeAddClimateEdge(Layer layer, FractalSettings settings, int oceanShrink) {
		if (settings.oceanShrink != oceanShrink) return layer;
		if (settings.useClimaticBiomes) {
			layer = new LayerAddClimate(2, layer);
			layer = new LayerCoolWarmEdge(2, layer);
			layer = new LayerHeatIceEdge(2, layer);
			layer = new LayerRareClimate(3, layer);
		}
		return layer;
	}

	public Layer(long seed) {
		this.baseSeed = seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
		this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
		this.baseSeed += seed;
	}

	public Layer(long seed, Layer parent) {
		this(seed);
		this.parent = parent;
	}

	public void setWorldSeed(long seed) {
		this.worldSeed = seed;
		if(this.parent != null) {
			this.parent.setWorldSeed(seed);
		}

		this.worldSeed *= this.worldSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldSeed += this.baseSeed;
		this.worldSeed *= this.worldSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldSeed += this.baseSeed;
		this.worldSeed *= this.worldSeed * 6364136223846793005L + 1442695040888963407L;
		this.worldSeed += this.baseSeed;
	}

	public void setChunkSeed(long x, long z) {
		this.chunkSeed = this.worldSeed;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += x;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += z;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += x;
		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += z;
	}

	protected int nextInt(int bound) {
		int var2 = (int)((this.chunkSeed >> 24) % (long)bound);
		if(var2 < 0) {
			var2 += bound;
		}

		this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
		this.chunkSeed += this.worldSeed;
		return var2;
	}

	protected abstract BiomeInfo[] getNewBiomes(int x, int z, int width, int length);

	public BiomeInfo[] getBiomes(int x, int z, int width, int length) {
		synchronized (this.cacheLock) {
			if (this.cacheData == null || x != this.cacheX || z != this.cacheZ || width != this.cacheWidth || length != this.cacheLength) {
				this.cacheX = x;
				this.cacheZ = z;
				this.cacheWidth = width;
				this.cacheLength = length;
				this.cacheData = this.getNewBiomes(x, z, width, length);
			}
			return this.cacheData;
		}
	}

	protected BiomeInfo[] forEach(int x, int z, int width, int length, UnaryOperator<BiomeInfo> operator) {
		return forEach(x, z, width, length, (input, ix, iz) -> operator.apply(input));
	}

	protected BiomeInfo[] forEach(int x, int z, int width, int length, LayerOperator operator) {
		BiomeInfo[] input = this.parent != null ? this.parent.getBiomes(x, z, width, length) : null;
		BiomeInfo[] output = new BiomeInfo[width * length];

		int i = 0;
		for (int zz = 0; zz < length; zz++) {
			for (int xx = 0; xx < width; xx++) {
				this.setChunkSeed(xx + x, zz + z);
				output[i] = operator.apply(input != null ? input[i] : null, xx, zz);
				i++;
			}
		}

		return output;
	}

	protected BiomeInfo[] forEachWithNeighbors(int x, int z, int width, int length, NeighborLayerOperator operator) {
		return forEachWithNeighbors(x, z, width, length, operator, false);
	}

	protected BiomeInfo[] forEachWithNeighbors(int x, int z, int width, int length, NeighborLayerOperator operator, boolean diagonal) {
		int inX = x - 1;
		int inZ = z - 1;
		int inWidth = width + 2;
		int inLength = length + 2;

		BiomeInfo[] input = this.parent != null ? this.parent.getBiomes(inX, inZ, inWidth, inLength) : null;
		BiomeInfo[] output = new BiomeInfo[width * length];
		BiomeInfo[] neighbors = new BiomeInfo[4];

		int i = 0;
		for (int zz = 0; zz < length; zz++) {
			for (int xx = 0; xx < width; xx++) {
				BiomeInfo inputPoint = null;
				if (input != null) {
					inputPoint = input[xx + 1 + (zz + 1) * inWidth];
					if (diagonal) {
						neighbors[0] = input[xx + zz * inWidth];
						neighbors[1] = input[xx + 2 + zz * inWidth];
						neighbors[2] = input[xx + (zz + 2) * inWidth];
						neighbors[3] = input[xx + 2 + (zz + 2) * inWidth];
					} else {
						neighbors[0] = input[xx + (zz + 1) * inWidth];
						neighbors[1] = input[xx + 2 + (zz + 1) * inWidth];
						neighbors[2] = input[xx + 1 + zz * inWidth];
						neighbors[3] = input[xx + 1 + (zz + 2) * inWidth];
					}
				}

				this.setChunkSeed(xx + x, zz + z);
				output[i] = operator.apply(inputPoint, xx, zz, neighbors);
				i++;
			}
		}

		return output;
	}

	protected static boolean allNeighborsEqual(BiomeInfo[] neighbors, BiomeInfo i) {
		return neighbors[0].equals(i) && neighbors[1].equals(i) && neighbors[2].equals(i) && neighbors[3].equals(i);
	}

	protected static boolean neighborsContain(BiomeInfo[] neighbors, BiomeInfo i) {
		return neighbors[0].equals(i) || neighbors[1].equals(i) || neighbors[2].equals(i) || neighbors[3].equals(i);
	}

	protected static int countMatchingNeighbors(BiomeInfo[] neighbors, BiomeInfo i) {
		int count = 0;
		for (BiomeInfo neighbor : neighbors) {
			if (neighbor.equals(i)) {
				count++;
			}
		}
		return count;
	}

	@FunctionalInterface
	public interface LayerOperator {
		BiomeInfo apply(BiomeInfo input, int ix, int iz);
	}

	@FunctionalInterface
	public interface NeighborLayerOperator {
		BiomeInfo apply(BiomeInfo input, int ix, int iz, BiomeInfo... neighbors);
	}
}
