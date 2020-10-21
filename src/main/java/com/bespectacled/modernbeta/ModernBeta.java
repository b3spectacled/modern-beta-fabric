package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.gui.widget.ButtonWidget;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.gen.type.AlphaGeneratorType;
import com.bespectacled.modernbeta.gen.type.BetaGeneratorType;
import com.bespectacled.modernbeta.gen.type.IndevGeneratorType;
import com.bespectacled.modernbeta.gen.type.SkylandsGeneratorType;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.MutableBlockColors;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.AlphaBiomes;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.biome.IndevBiomes;
import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.client.GoVote;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.AlphaChunkGenerator;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;
import com.bespectacled.modernbeta.gen.IndevChunkGenerator;

public class ModernBeta implements ModInitializer {
    public static final String ID = "modern_beta";
    public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
    public static final ModernBetaConfig BETA_CONFIG = AutoConfig
            .register(ModernBetaConfig.class, GsonConfigSerializer::new).getConfig();

    public static long SEED;

    // Ehh...
    public static void setBlockColorsSeed(long seed, boolean defaultColors) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            MutableBlockColors mutableBlockColors = MutableBlockColors
                    .inject(MinecraftClient.getInstance().getBlockColors());
            mutableBlockColors.setSeed(seed, defaultColors);
        }
    }

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Modern Beta...");

        // BetaSurfaceBuilder.register(); Unused
        BetaCarver.register();
        BetaDecorator.register();
        BetaFeature.reserveConfiguredFeatureIDs();
        BetaFeature.register();
        BetaStructure.register();

        BetaBiomes.reserveBiomeIDs();
        AlphaBiomes.reserveBiomeIDs();
        IndevBiomes.reserveBiomeIDs();

        BetaBiomeSource.register();
        AlphaBiomeSource.register();
        IndevBiomeSource.register();

        BetaChunkGenerator.register();
        AlphaChunkGenerator.register();
        SkylandsChunkGenerator.register();
        IndevChunkGenerator.register();
        

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            GoVote.init();
            
            BetaGeneratorType.register();
            AlphaGeneratorType.register();
            SkylandsGeneratorType.register();
            IndevGeneratorType.register();
            
        }

        LOGGER.log(Level.INFO, "Initialized Modern Beta!");

        // I am not a programmer, I am an ape smashing rocks together.....
    }

}
