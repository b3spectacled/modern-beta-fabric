package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public record BiomeInfo(RegistryEntry<Biome> biome, boolean special) {
	public static BiomeInfo of(RegistryEntry<Biome> biome) {
		return new BiomeInfo(biome, false);
	}

	public BiomeInfo asSpecial() {
		return asSpecial(true);
	}

	public BiomeInfo asSpecial(boolean special) {
		return new BiomeInfo(biome, special);
	}

	public static BiomeInfo fromId(String id, RegistryEntryLookup<Biome> biomeRegistry) {
		boolean special = id.charAt(0) == '*';
		if (special) id = id.substring(1);

		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
		RegistryEntry<Biome> biome = biomeRegistry.getOrThrow(key);
		return new BiomeInfo(biome, special);
	}

	public String getId() {
		String id = biome.getKey().orElse(BiomeKeys.PLAINS).getValue().toString();
		if (special) {
			id = "*" + id;
		}
		return id;
	}
}
