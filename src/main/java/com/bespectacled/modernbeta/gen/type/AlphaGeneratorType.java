package com.bespectacled.modernbeta.gen.type;

import java.util.Map;
import java.util.Optional;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.gui.CustomizeAlphaLevelScreen;
import com.bespectacled.modernbeta.mixin.MixinGeneratorTypeAccessor;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

@Environment(EnvType.CLIENT)
public final class AlphaGeneratorType extends GeneratorType {
    public static final GeneratorType INSTANCE = new AlphaGeneratorType();

    public static final StructuresConfig structures = new StructuresConfig(true);
    public static final NoiseSamplingConfig noiseSampler = new NoiseSamplingConfig(1.0, 1.0, 40.0, 22.0);
    public static final GenerationShapeConfig noise = new GenerationShapeConfig(256, noiseSampler,
            new SlideConfig(-10, 3, 0), new SlideConfig(-30, 0, 0), 1, 2, 1.0, -60.0 / (256.0 / 2.0), true, true, false,
            false);

    public static final ChunkGeneratorSettings type = new ChunkGeneratorSettings(structures, noise,
            Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(), -10, 0, 64, false);
    
    public static final OldGeneratorSettings alphaSettings = new OldGeneratorSettings(type, new CompoundTag());
    
    // Add to Screen Providers
    private static Map<Optional<GeneratorType>, ScreenProvider> NEW_SCREEN_PROVIDERS = 
        new ImmutableMap.Builder<Optional<GeneratorType>, ScreenProvider>()
            .putAll(MixinGeneratorTypeAccessor.getScreenProviders())
            .put(
                Optional.<GeneratorType>of(INSTANCE), (createWorldScreen, generatorSettings) -> {
                    return new CustomizeAlphaLevelScreen(createWorldScreen, alphaSettings);
                }
            )
            .build();

    private AlphaGeneratorType() {
        super("alpha");
    }

    public static void register() {
        GeneratorType.VALUES.add(INSTANCE);
        MixinGeneratorTypeAccessor.setScreenProviders(NEW_SCREEN_PROVIDERS);
        
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev world type.");
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomes, Registry<ChunkGeneratorSettings> genSettings, long seed) {
        alphaSettings.settings = OldGeneratorSettings.createAlphaSettings(BiomeType.CLASSIC.getName());
        return new OldChunkGenerator(new OldBiomeSource(seed, biomes, alphaSettings.settings), seed, alphaSettings);
    }
    
    
}
