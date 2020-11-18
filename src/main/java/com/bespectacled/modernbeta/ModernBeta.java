package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.gen.type.AlphaGeneratorType;
import com.bespectacled.modernbeta.gen.type.BetaGeneratorType;
import com.bespectacled.modernbeta.gen.type.IndevGeneratorType;
import com.bespectacled.modernbeta.gen.type.InfdevGeneratorType;
import com.bespectacled.modernbeta.gen.type.InfdevOldGeneratorType;
import com.bespectacled.modernbeta.gen.type.SkylandsGeneratorType;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.MutableBlockColors;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.biome.IndevBiomes;
import com.bespectacled.modernbeta.biome.PreBetaBiomeSource;
import com.bespectacled.modernbeta.biome.PreBetaBiomes;
import com.bespectacled.modernbeta.biome.layer.VanillaBiomeModifier;
import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.AlphaChunkGenerator;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;
import com.bespectacled.modernbeta.gen.IndevChunkGenerator;
import com.bespectacled.modernbeta.gen.InfdevChunkGenerator;
import com.bespectacled.modernbeta.gen.InfdevOldChunkGenerator;

public class ModernBeta implements ModInitializer {
    public static final String ID = "modern_beta";
    public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
    public static final ModernBetaConfig BETA_CONFIG = AutoConfig
            .register(ModernBetaConfig.class, GsonConfigSerializer::new).getConfig();

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
        
        LOGGER.log(Level.INFO, "Registered Modern Beta features!");

        BetaBiomes.reserveBiomeIDs();
        PreBetaBiomes.reserveBiomeIDs();
        IndevBiomes.reserveBiomeIDs();
        
        LOGGER.log(Level.INFO, "Registered Modern Beta biomes!");

        BetaBiomeSource.register();
        PreBetaBiomeSource.register();
        IndevBiomeSource.register();
        
        LOGGER.log(Level.INFO, "Registered Modern Beta biome providers!");

        BetaChunkGenerator.register();
        SkylandsChunkGenerator.register();
        AlphaChunkGenerator.register();
        InfdevChunkGenerator.register();
        InfdevOldChunkGenerator.register();
        IndevChunkGenerator.register();
        
        LOGGER.log(Level.INFO, "Registered Modern Beta chunk generators!");
        
        VanillaBiomeModifier.addShrineToOceans();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetaGeneratorType.register();
            SkylandsGeneratorType.register();
            AlphaGeneratorType.register();
            InfdevGeneratorType.register();
            InfdevOldGeneratorType.register();
            IndevGeneratorType.register();
            
            LOGGER.log(Level.INFO, "Registered Modern Beta world types!");            
        }

        LOGGER.log(Level.INFO, "Initialized Modern Beta!");

        // I am not a programmer, I am an ape smashing rocks together.....
    }

}
