package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public record BiomeInfo(RegistryEntry<Biome> biome, int type) {
	public static BiomeInfo of(RegistryEntry<Biome> biome) {
		return new BiomeInfo(biome, 0);
	}

	public static BiomeInfo of(RegistryEntry<Biome> biome, int type) {
		return new BiomeInfo(biome, type);
	}

	public static BiomeInfo fromLookup(RegistryEntryLookup<Biome> biomeLookup, RegistryKey<Biome> key) {
		return fromLookup(biomeLookup, key, 0);
	}

	public static BiomeInfo fromLookup(RegistryEntryLookup<Biome> biomeLookup, RegistryKey<Biome> key, int type) {
		if (biomeLookup == null) return BiomeInfo.of(null, type);
		return BiomeInfo.of(biomeLookup.getOptional(key).orElse(null), type);
	}

	public BiomeInfo asSpecial() {
		return withType(1);
	}

	public BiomeInfo asSpecial(boolean special) {
		return withType(special ? 1 : 0);
	}

	public BiomeInfo withType(int type) {
		return new BiomeInfo(biome, type);
	}

	public static BiomeInfo fromId(String id, RegistryEntryLookup<Biome> biomeRegistry) {
		int type = 0;
		int typeIndex = id.indexOf('*');
		if (typeIndex == 0) {
			type = 1;
			id = id.substring(1);
		} else if (typeIndex > 0) {
			try {
				type = Integer.parseInt(id.substring(0, typeIndex));
			} catch (NumberFormatException ignored) {
			}
			id = id.substring(typeIndex + 1);
		}

		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
		RegistryEntry<Biome> biome = biomeRegistry.getOrThrow(key);
		return new BiomeInfo(biome, type);
	}

	public String getId() {
		String id = biome.getKey().orElse(BiomeKeys.PLAINS).getValue().toString();
		if (type == 1) {
			id = "*" + id;
		} else if (type != 0) {
			id = type + "*" + id;
		}
		return id;
	}

	public String toString() {
		return this.getId();
	}
}
