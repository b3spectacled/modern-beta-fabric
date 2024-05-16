package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;

import java.util.List;

public record ClimaticBiomeList<T>(List<T> normalBiomes, List<T> rareBiomes) {
    public static ClimaticBiomeList<String> EMPTY_STRING = new ClimaticBiomeList<>(List.of(), List.of());

    public static List<ClimaticBiomeList<String>> fromReader(String tag, NbtReader reader, List<ClimaticBiomeList<String>> alternate) {
        if (reader.contains(tag)) {
			return reader.readListOrThrow(tag)
				.stream()
				.map(ClimaticBiomeList::fromNbt)
				.toList();
		}

		return List.copyOf(alternate);
    }

    public static ClimaticBiomeList<String> fromNbt(NbtElement element) {
        if (!(element instanceof NbtCompound compound)) return EMPTY_STRING;
        NbtReader reader = new NbtReader(compound);

        List<String> normalBiomes = reader.readList("normal", new NbtList()).stream()
            .map(NbtElement::asString).toList();
        List<String> rareBiomes = reader.readList("rare", new NbtList()).stream()
            .map(NbtElement::asString).toList();
        return new ClimaticBiomeList<>(normalBiomes, rareBiomes);
    }

    public static NbtList listToNbt(List<ClimaticBiomeList<String>> list) {
		NbtListBuilder builder = new NbtListBuilder();
		list.forEach(b -> builder.add(ClimaticBiomeList.ofStringToNbt(b)));
		return builder.build();
	}

    public static NbtCompound ofStringToNbt(ClimaticBiomeList<String> list) {
        return new NbtCompoundBuilder()
            .putList("normal", FractalSettings.listToNbt(list.normalBiomes))
            .putList("rare", FractalSettings.listToNbt(list.rareBiomes))
            .build();
    }

    public static ClimaticBiomeList<BiomeInfo> qualify(ClimaticBiomeList<String> asString, RegistryEntryLookup<Biome> biomeLookup) {
        return new ClimaticBiomeList<>(
            asString.normalBiomes.stream().map(s -> BiomeInfo.fromId(s, biomeLookup)).toList(),
            asString.rareBiomes.stream().map(s -> BiomeInfo.fromId(s, biomeLookup)).toList()
        );
    }
}
