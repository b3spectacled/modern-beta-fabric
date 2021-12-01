package com.bespectacled.modernbeta;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.client.color.BlockColors;
import com.bespectacled.modernbeta.command.DebugProviderSettingsCommand;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import com.bespectacled.modernbeta.config.ModernBetaConfigCompat;
import com.bespectacled.modernbeta.config.ModernBetaConfigGeneration;
import com.bespectacled.modernbeta.config.ModernBetaConfigRendering;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.world.biome.OldBiomeModifier;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.OldBiomes;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;
import com.bespectacled.modernbeta.world.gen.OldGeneratorType;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class ModernBeta implements ModInitializer {
    public static final String MOD_ID = "modern_beta";
    public static final String MOD_NAME = "Modern Beta";
    public static final boolean DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();
    
    public static final ModernBetaConfig CONFIG = AutoConfig.register(
        ModernBetaConfig.class, 
        PartitioningSerializer.wrap(GsonConfigSerializer::new)
    ).getConfig();
    
    public static final ModernBetaConfigGeneration GEN_CONFIG = CONFIG.generationConfig;
    public static final ModernBetaConfigBiome BIOME_CONFIG = CONFIG.biomeConfig;
    public static final ModernBetaConfigRendering RENDER_CONFIG = CONFIG.renderingConfig;
    public static final ModernBetaConfigCompat COMPAT_CONFIG = CONFIG.compatConfig;

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    public static Identifier createId(String name) {
        return new Identifier(MOD_ID, name);
    }
    
    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] {}", message);
    }
    
    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing Modern Beta...");

        // Register mod stuff
        OldStructures.register();
        OldBiomes.register();
        OldBiomeSource.register();
        OldChunkGenerator.register();
        OldChunkGeneratorSettings.register();
        
        // Add Ocean Shrine to vanilla oceans, when using vanilla biome type.
        OldBiomeModifier.addShrineToOceans();
        
        // Set up mod compatibility
        Compat.setupCompat();
        
        // Register default providers
        ModernBetaBuiltInProviders.registerChunkProviders();
        ModernBetaBuiltInProviders.registerChunkSettings();
        ModernBetaBuiltInProviders.registerBiomeProviders();
        ModernBetaBuiltInProviders.registerBiomeSettings();
        ModernBetaBuiltInProviders.registerWorldProviders();
        
        // Register client-only stuff, i.e. GUI, block colors, etc.
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            OldGeneratorType.register();
            
            // Register default screen providers
            ModernBetaBuiltInProviders.registerWorldScreens();
            ModernBetaBuiltInProviders.registerBiomeScreens();
            
            // Override default biome grass/foliage colors
            BlockColors.register();
        }
        
        // Register dev-only stuff, i.e. commands, etc.
        if (DEV_ENV) {
            DebugProviderSettingsCommand.register();
        }
        
        // Serialize various world gen stuff to JSON
        //OldChunkGeneratorSettings.export();
        //OldChunkGenerator.export();
        
        log(Level.INFO, "Initialized Modern Beta!");
        
        // Man, I am not good at this...
    }
}
