package com.bespectacled.modernbeta;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.modernbeta.gen.BetaGeneratorType;
import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.gen.SkylandsGeneratorType;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.client.GoVote;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;

public class ModernBeta implements ModInitializer {
	public static final String ID = "modern_beta";
	
	public static final Logger LOGGER = LogManager.getLogger("ModernBeta");
	
	@Override
	public void onInitialize() {
	    ModernBetaConfig.loadConfig(); // Generate config if not present.
	    
		//BetaSurfaceBuilder.register(); Unused
		BetaDecorator.register();
		
		BetaFeature.reserveConfiguredFeatureIDs();
		BetaBiomes.reserveBiomeIDs();
		
		BetaBiomeSource.register();
		BetaChunkGenerator.register();
		
		SkylandsChunkGenerator.register();
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
		    GoVote.init();
			BetaGeneratorType.register();
			SkylandsGeneratorType.register();
		}
		
		// I am not a programmer, I am an ape smashing rocks together.....
	}

}
