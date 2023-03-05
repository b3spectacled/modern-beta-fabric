package mod.bespectacled.modernbeta.client.gui.screen;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ModernBetaWorldScreenProvider {
    public static GeneratorOptionsHolder.RegistryAwareModifier createModifier(
        NbtCompound chunkSettings,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        return (dynamicRegistryManager, dimensionsRegistryHolder) -> {
            ModernBetaSettingsChunk modernBetaSettingsChunk = ModernBetaSettingsChunk.fromCompound(chunkSettings); 
            
            Registry<ChunkGeneratorSettings> registrySettings = dynamicRegistryManager.get(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
            RegistryEntry.Reference<ChunkGeneratorSettings> settings = registrySettings.entryOf(keyOfSettings(ModernBeta.createId(modernBetaSettingsChunk.chunkProvider)));
            RegistryEntryLookup<Biome> registryBiome = dynamicRegistryManager.getWrapperOrThrow(RegistryKeys.BIOME);
            
            ModernBetaChunkGenerator chunkGenerator = new ModernBetaChunkGenerator(
                new ModernBetaBiomeSource(
                    registryBiome,
                    biomeSettings,
                    caveBiomeSettings
                ),
                settings,
                chunkSettings
            );
            
            return dimensionsRegistryHolder.with(dynamicRegistryManager, chunkGenerator);
        };
    }
    
    private static RegistryKey<ChunkGeneratorSettings> keyOfSettings(Identifier id) {
        return RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, id);
    }
}
