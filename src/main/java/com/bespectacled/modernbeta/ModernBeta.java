package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.color.block.BlockColors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.gen.BetaGeneratorType;
import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.gen.SkylandsGeneratorType;
import com.bespectacled.modernbeta.util.MutableBlockColors;
import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.AlphaBiomes;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.client.GoVote;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.AlphaChunkGenerator;
import com.bespectacled.modernbeta.gen.AlphaGeneratorType;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;

public class ModernBeta implements ModInitializer {
	public static final String ID = "modern_beta";
	public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
	public static long SEED;

	// Ehh...
	public static void setBlockColorsSeed(long seed, boolean defaultColors) {
	    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
	        MutableBlockColors mutableBlockColors = MutableBlockColors.inject(MinecraftClient.getInstance().getBlockColors());
	        mutableBlockColors.setSeed(seed, defaultColors);
	    }
	}
	
	@Override
	public void onInitialize() {
	    LOGGER.log(Level.INFO, "Initializing Modern Beta...");
	    
	    ModernBetaConfig.loadConfig(); // Generate config if not present.
	    
		//BetaSurfaceBuilder.register(); Unused
	    BetaCarver.register();
		BetaDecorator.register();
		BetaFeature.reserveConfiguredFeatureIDs();
		BetaFeature.register();
		
		BetaBiomes.reserveBiomeIDs();
		AlphaBiomes.reserveBiomeIDs();
		
		BetaBiomeSource.register();
		AlphaBiomeSource.register();
		
		BetaChunkGenerator.register();
		SkylandsChunkGenerator.register();
		AlphaChunkGenerator.register();
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
		    GoVote.init();
			BetaGeneratorType.register();
			SkylandsGeneratorType.register();
			AlphaGeneratorType.register();
		}
		
		LOGGER.log(Level.INFO, "Initialized Modern Beta!");
		
		// I am not a programmer, I am an ape smashing rocks together.....
	}

}
