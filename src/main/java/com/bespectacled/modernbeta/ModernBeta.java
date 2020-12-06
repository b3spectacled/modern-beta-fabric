package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.gen.type.AlphaGeneratorType;
import com.bespectacled.modernbeta.gen.type.BetaGeneratorType;
import com.bespectacled.modernbeta.gen.type.BetaVoronoiGeneratorType;
import com.bespectacled.modernbeta.gen.type.IndevGeneratorType;
import com.bespectacled.modernbeta.gen.type.InfdevGeneratorType;
import com.bespectacled.modernbeta.gen.type.InfdevOldGeneratorType;
import com.bespectacled.modernbeta.gen.type.SkylandsGeneratorType;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.MutableBlockColors;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.VanillaBiomeModifier;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;

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
    
    public static Identifier createId(String name) {
        return new Identifier(ID, name);
    }

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Modern Beta...");

        BetaStructure.register();
        
        //LOGGER.log(Level.INFO, "Registered Modern Beta features!");
        
        BetaBiomes.registerBiomes();
        ClassicBiomes.registerAlphaBiomes();
        ClassicBiomes.registerInfdevBiomes();
        IndevBiomes.registerBiomes();
        ClassicBiomes.registerInfdevOldBiomes();
        
        //LOGGER.log(Level.INFO, "Registered Modern Beta biomes!");

        OldBiomeSource.register();
        
        //LOGGER.log(Level.INFO, "Registered Modern Beta biome provider!");
        
        OldChunkGenerator.register();
        
        //LOGGER.log(Level.INFO, "Registered Modern Beta chunk generator!");
        
        VanillaBiomeModifier.addShrineToOceans();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetaGeneratorType.register();
            SkylandsGeneratorType.register();
            AlphaGeneratorType.register();
            InfdevGeneratorType.register();
            InfdevOldGeneratorType.register();
            IndevGeneratorType.register();
            BetaVoronoiGeneratorType.register();
            //LOGGER.log(Level.INFO, "Registered Modern Beta world types!");            
        }

        LOGGER.log(Level.INFO, "Initialized Modern Beta!");
        

        // I am not a programmer, I am an ape smashing rocks together.....
    }

}
