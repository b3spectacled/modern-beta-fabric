package com.bespectacled.modernbeta.gen.type;

import java.util.Map;
import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.CustomizeInfdevLevelScreen;
import com.bespectacled.modernbeta.mixin.client.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Environment(EnvType.CLIENT)
public final class InfdevGeneratorType extends GeneratorType {
    public static final GeneratorType INSTANCE = new InfdevGeneratorType();
    
    public static final OldGeneratorSettings infdevSettings = new OldGeneratorSettings(new CompoundTag(), false);
    
    // Add to Screen Providers
    private static Map<Optional<GeneratorType>, ScreenProvider> NEW_SCREEN_PROVIDERS = 
        new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
            .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
            .put(
                Optional.<GeneratorType>of(INSTANCE), (createWorldScreen, generatorSettings) -> {
                    return new CustomizeInfdevLevelScreen(createWorldScreen, infdevSettings);
                }
            )
            .build();

    private InfdevGeneratorType() {
        super("infdev");
    }

    public static void register() {
        GeneratorType.VALUES.add(INSTANCE);
        MixinGeneratorTypeAccessor.setScreenProviders(NEW_SCREEN_PROVIDERS);
        
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev world type.");
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        infdevSettings.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV.getName(), BiomeType.CLASSIC.getName(), ModernBeta.BETA_CONFIG.generateOceans);
        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, infdevSettings.providerSettings), seed, infdevSettings);
    }
    
    
}
