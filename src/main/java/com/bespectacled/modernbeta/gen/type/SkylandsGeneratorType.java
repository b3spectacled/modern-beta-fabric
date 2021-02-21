package com.bespectacled.modernbeta.gen.type;

import java.util.Map;
import java.util.Optional;

import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gen.WorldType;
import com.bespectacled.modernbeta.gui.InfCustomizeLevelScreen;
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
public final class SkylandsGeneratorType extends GeneratorType {
    public static final GeneratorType INSTANCE = new SkylandsGeneratorType();
    
    public static OldGeneratorSettings SKYLANDS_SETTINGS = new OldGeneratorSettings(new CompoundTag(), false);

    // Add to Screen Providers
    private static Map<Optional<GeneratorType>, ScreenProvider> NEW_SCREEN_PROVIDERS = 
        new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
            .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
            .put(
                Optional.<GeneratorType>of(INSTANCE), (createWorldScreen, generatorSettings) -> {
                    return new InfCustomizeLevelScreen(createWorldScreen, SKYLANDS_SETTINGS, "createWorld.customize.skylands.title", BiomeType.SKY, false);
                }
                
            )
            .build();
    
    
    private SkylandsGeneratorType() {
        super("skylands");
    }

    public static void register() {
        GeneratorType.VALUES.add(INSTANCE);
        MixinGeneratorTypeAccessor.setScreenProviders(NEW_SCREEN_PROVIDERS);
        
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Skylands world type.");
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        SKYLANDS_SETTINGS.providerSettings = OldGeneratorSettings.createInfSettings(WorldType.SKYLANDS.getName(), BiomeType.SKY.getName(), false);
        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, SKYLANDS_SETTINGS.providerSettings), seed, SKYLANDS_SETTINGS);
    }
}
