package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.util.MutableBlockColors;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.OldBiomes;
import com.bespectacled.modernbeta.world.biome.vanilla.VanillaBiomeModifier;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.OldChunkGeneratorSettings;
import com.bespectacled.modernbeta.world.gen.OldGeneratorType;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.config.ModernBetaConfig;

public class ModernBeta implements ModInitializer {
    public static final String MOD_ID = "modern_beta";
    public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
    public static final ModernBetaConfig BETA_CONFIG = AutoConfig.register(ModernBetaConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new)).getConfig();

    // Ehh...
    public static void setBlockColorsSeed(long seed, boolean useBetaColors) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            MutableBlockColors mutableBlockColors = MutableBlockColors.inject(MinecraftClient.getInstance().getBlockColors());
            mutableBlockColors.setSeed(seed, useBetaColors);
        }
    }
    
    public static Identifier createId(String name) {
        return new Identifier(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Modern Beta...");

        // Register mod stuff
        OldStructures.register();
        OldBiomes.register();
        OldBiomeSource.register();
        OldChunkGenerator.register();
        OldChunkGeneratorSettings.register();
        
        VanillaBiomeModifier.addShrineToOceans();

        // Set up mod compatibility
        Compat.setupCompat();
        
        // Register default providers
        ModernBetaDefaultProviders.registerChunkProviders();
        ModernBetaDefaultProviders.registerChunkProviderSettings();
        ModernBetaDefaultProviders.registerBiomeProviders();
        ModernBetaDefaultProviders.registerWorldProviders();
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            OldGeneratorType.register();
            
            // Register default screen providers
            ModernBetaDefaultProviders.registerWorldScreenProviders();
            ModernBetaDefaultProviders.registerBiomeScreenProviders();
        }
        
        // Serialize various world gen stuff to JSON
        //OldGeneratorSettings.export();
        //OldChunkGenerator.export();

        LOGGER.log(Level.INFO, "Initialized Modern Beta!");

        // Man, I am not good at this...
    }

}
