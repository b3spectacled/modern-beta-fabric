package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.function.UnaryOperator;

public abstract class Layer {
	private long worldSeed;
	private long chunkSeed;
	private long baseSeed;
	protected Layer parent;

	public static Layer getLayer(RegistryEntryLookup<Biome> biomeLookup, long seed, FractalSettings settings) {
		RegistryEntry<Biome> ocean = biomeLookup.getOrThrow(BiomeKeys.OCEAN);
		RegistryEntry<Biome> plains = settings.plains;
		RegistryEntry<Biome> river = biomeLookup.getOrThrow(BiomeKeys.RIVER);
		RegistryEntry<Biome> frozenOcean = biomeLookup.getOrThrow(BiomeKeys.FROZEN_OCEAN);
		RegistryEntry<Biome> icePlains = settings.icePlains;
		RegistryEntry<Biome> frozenRiver = biomeLookup.getOrThrow(BiomeKeys.FROZEN_RIVER);
		RegistryEntry<Biome> mushroomIsland = biomeLookup.getOrThrow(BiomeKeys.MUSHROOM_FIELDS);
		RegistryEntry<Biome> beach = biomeLookup.getOrThrow(BiomeKeys.BEACH);

		List<RegistryEntry<Biome>> preservedBiomes = List.of(ocean, mushroomIsland);

		Layer land = new LayerInitLand(1, ocean, plains);
		land = new LayerFuzzyZoom(2000, land);
		if (!settings.oceans) {
			land = new LayerSingleBiome(plains);
		} else if (settings.largeIslands) {
			land = new LayerAddLandB18(1, land, ocean, plains, frozenOcean, icePlains);
			land = new LayerZoom(2001, land);
			land = new LayerAddLandB18(2, land, ocean, plains, frozenOcean, icePlains);
			land = new LayerZoom(2002, land);
			land = new LayerAddLandB18(3, land, ocean, plains, frozenOcean, icePlains);
			if (settings.addSnow) land = new LayerAddSnow(2, land, ocean, icePlains);
			land = new LayerZoom(2003, land);
			land = new LayerAddLandB18(3, land, ocean, plains, frozenOcean, icePlains);
			land = new LayerZoom(2004, land);
			land = new LayerAddLandB18(3, land, ocean, plains, frozenOcean, icePlains);
		} else {
			land = new LayerAddLand(1, land, ocean, plains, frozenOcean, icePlains);
			land = new LayerZoom(2001, land);
			land = new LayerAddLand(2, land, ocean, plains, frozenOcean, icePlains);
			if (settings.addSnow) land = new LayerAddSnow(2, land, ocean, icePlains);
			land = new LayerZoom(2002, land);
			land = new LayerAddLand(3, land, ocean, plains, frozenOcean, icePlains);
			land = new LayerZoom(2003, land);
			land = new LayerAddLand(4, land, ocean, plains, frozenOcean, icePlains);
		}
		if (settings.addMushroomIslands) land = new LayerAddMushroomIsland(5, land, ocean, mushroomIsland);

		Layer riverLayout = new LayerInitRiver(100, land, ocean, river);
		riverLayout = LayerZoom.multi(1000, riverLayout, settings.biomeScale + settings.hillScale);
		riverLayout = new LayerComputeRiver(1, riverLayout, ocean, river);
		riverLayout = new LayerSmooth(1000, riverLayout);

		Layer biomes = new LayerAddBiomes(200, land, settings.biomes, preservedBiomes, plains, icePlains);
		for (int i = 0; i < settings.hillScale; i++) {
			if (settings.subVariantScale == i && !settings.subVariants.isEmpty()) biomes = new LayerSubVariants(100, biomes, settings.subVariants);
			biomes = new LayerZoom(1000 + i, biomes);
		}
		if (settings.subVariantScale == settings.hillScale && !settings.subVariants.isEmpty()) biomes = new LayerSubVariants(100, biomes, settings.subVariants);
		if (settings.addHills) biomes = new LayerAddHills(1000, biomes, settings.hillVariants);

		for (int i = 0; i < settings.biomeScale; i++) {
			biomes = new LayerZoom(1000 + i, biomes);
			if (i == 0) {
				if (settings.oceans) biomes = settings.largeIslands
					? new LayerAddLandB18(3, biomes, ocean, plains, frozenOcean, icePlains)
					: new LayerAddLand(3, biomes, ocean, plains, frozenOcean, icePlains);
				if (!settings.addBeaches && settings.addMushroomIslands) biomes = new LayerMushroomIslandShore(biomes, mushroomIsland, ocean);
			}

			if (i == 1) {
				if (settings.addBeaches) biomes = new LayerAddShore(1000, biomes, beach, ocean, mushroomIsland);
				if (settings.addSwampRivers) biomes = new LayerAddSwampRivers(1000, biomes, river);
			}
		}

		biomes = new LayerSmooth(1000, biomes);
		if (settings.addRivers) biomes = new LayerApplyRiver(biomes, riverLayout, ocean, mushroomIsland, icePlains, frozenRiver);
		biomes.setWorldSeed(seed);
		return biomes;
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

	public abstract BiomeInfo[] getBiomes(int x, int z, int width, int length);

	protected BiomeInfo[] forEach(int x, int z, int width, int length, UnaryOperator<BiomeInfo> operator) {
		BiomeInfo[] input = this.parent != null ? this.parent.getBiomes(x, z, width, length) : null;
		BiomeInfo[] output = new BiomeInfo[width * length];

		int i = 0;
		for (int zz = 0; zz < length; zz++) {
			for (int xx = 0; xx < width; xx++) {
				this.setChunkSeed(xx + x, zz + z);
				output[i] = operator.apply(input != null ? input[i] : null);
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
				output[i] = operator.apply(inputPoint, neighbors);
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
}
