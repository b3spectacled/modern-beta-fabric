package com.bespectacled.modernbeta.gen.type;

import java.util.Map;
import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gen.WorldType;
import com.bespectacled.modernbeta.gui.InfdevOldCustomizeLevelScreen;
import com.bespectacled.modernbeta.mixin.MixinGeneratorTypeAccessor;
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
public final class InfdevOldGeneratorType extends GeneratorType {
    public static final GeneratorType INSTANCE = new InfdevOldGeneratorType();
    
    public static final OldGeneratorSettings INFDEV_OLD_SETTINGS = new OldGeneratorSettings(new CompoundTag(), false);
    
    // Add to Screen Providers
    private static Map<Optional<GeneratorType>, ScreenProvider> NEW_SCREEN_PROVIDERS = 
        new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
            .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
            .put(
                Optional.<GeneratorType>of(INSTANCE), (createWorldScreen, generatorSettings) -> {
                    return new InfdevOldCustomizeLevelScreen(createWorldScreen, INFDEV_OLD_SETTINGS, "createWorld.customize.infdev.title", BiomeType.CLASSIC, true);
                }
            )
            .build();

    private InfdevOldGeneratorType() {
        super("infdev_old");
    }

    public static void register() {
        GeneratorType.VALUES.add(INSTANCE);
        MixinGeneratorTypeAccessor.setScreenProviders(NEW_SCREEN_PROVIDERS);
        
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev world type.");
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        INFDEV_OLD_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.INFDEV_OLD.getName(), BiomeType.CLASSIC.getName(), ModernBeta.BETA_CONFIG.generateOceans);
        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, INFDEV_OLD_SETTINGS.providerSettings), seed, INFDEV_OLD_SETTINGS);
    }
    
    
}
