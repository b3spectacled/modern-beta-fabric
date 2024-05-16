package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import com.mojang.datafixers.util.Either;
import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

class DummyBiome implements RegistryEntry<Biome> {
	public static final DummyBiome
		OCEAN = makeDummy("ocean"),
		PLAINS = makeDummy("plains"),
		RIVER = makeDummy("river"),
		FROZEN_OCEAN = makeDummy("frozen_ocean"),
		ICE_PLAINS = makeDummy("ice_plains"),
		MUSHROOM_ISLAND = makeDummy("mushroom_island"),
		DEEP_OCEAN = makeDummy("deep_ocean"),
		CLIMATE = makeDummy("climate"),
		WARM_OCEAN = makeDummy("warm_ocean"),
		LUKEWARM_OCEAN = makeDummy("lukewarm_ocean"),
		COLD_OCEAN = makeDummy("cold_ocean");

	public final Identifier id;
	public final BiomeInfo biomeInfo;

	public DummyBiome(Identifier id) {
		this.id = id;
		this.biomeInfo = BiomeInfo.of(this);
	}

	private static DummyBiome makeDummy(String id) {
		return new DummyBiome(ModernBeta.createId(id));
	}

	@Override
	public Biome value() {
		return null;
	}

	@Override
	public boolean hasKeyAndValue() {
		return false;
	}

	@Override
	public boolean matchesId(Identifier id) {
		return this.id.equals(id);
	}

	@Override
	public boolean matchesKey(RegistryKey<Biome> key) {
		return key.getValue().equals(this.id);
	}

	@Override
	public boolean matches(Predicate<RegistryKey<Biome>> predicate) {
		return false;
	}

	@Override
	public boolean isIn(TagKey<Biome> tag) {
		return false;
	}

	@Override
	public Stream<TagKey<Biome>> streamTags() {
		return Stream.empty();
	}

	@Override
	public Either<RegistryKey<Biome>, Biome> getKeyOrValue() {
		return null;
	}

	@Override
	public Optional<RegistryKey<Biome>> getKey() {
		return Optional.empty();
	}

	@Override
	public Type getType() {
		return null;
	}

	@Override
	public boolean ownerEquals(RegistryEntryOwner<Biome> owner) {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DummyBiome that)) return false;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
