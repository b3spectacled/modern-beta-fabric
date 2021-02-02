package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.structure.OldStructures;
import com.bespectacled.modernbeta.util.MutableBlockColors;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.OldBiomes;
import com.bespectacled.modernbeta.biome.vanilla.VanillaBiomeModifier;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.gen.OldGeneratorType;

public class ModernBeta implements ModInitializer {
    public static final String MOD_ID = "modern_beta";
    public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
    public static final ModernBetaConfig BETA_CONFIG = AutoConfig.register(ModernBetaConfig.class, GsonConfigSerializer::new).getConfig();

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

        OldStructures.register();
        OldBiomes.register();
        OldBiomeSource.register();
        OldChunkGenerator.register();
        
        VanillaBiomeModifier.addShrineToOceans();
        
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            OldGeneratorType.register();
        }

        LOGGER.log(Level.INFO, "Initialized Modern Beta!");

        // I am not a programmer, I am an ape smashing rocks together.....
    }

}
