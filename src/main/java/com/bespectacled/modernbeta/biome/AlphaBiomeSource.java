package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

public class AlphaBiomeSource extends BiomeSource {
	
	public static final Codec<AlphaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("seed").stable().forGetter(betaBiomeSource -> betaBiomeSource.seed), 
		RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(betaBiomeSource -> betaBiomeSource.biomeRegistry)
	).apply(instance, (instance).stable(AlphaBiomeSource::new)));
	
	private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "alpha")),
        RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "alpha_winter"))
	);
			
	private final long seed;
	public final Registry<Biome> biomeRegistry;
	private final boolean alpha_winter_mode = ModernBetaConfig.loadConfig().alpha_winter_mode;

	public AlphaBiomeSource(long seed, Registry<Biome> registry) {
	    super(BIOMES.stream().map((registryKey) -> () -> (Biome)registry.get(registryKey)));
		
		this.seed = seed;
		this.biomeRegistry = registry;
		
	}
	
	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
	    String biomeName = alpha_winter_mode ? "alpha_winter" : "alpha";
	    
	    return this.biomeRegistry.get(new Identifier(ModernBeta.ID, biomeName));
	}
	
	public Biome getOceanBiomeForNoiseGen(int x, int y, int z) {
	    String biomeName = alpha_winter_mode ? "alpha_winter" : "ocean";
	    
	    return this.biomeRegistry.get(new Identifier(ModernBeta.ID, biomeName));
    }

	
	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return AlphaBiomeSource.CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public BiomeSource withSeed(long seed) {
		return new AlphaBiomeSource(seed, this.biomeRegistry);
	}

	public static void register() {
		ModernBeta.LOGGER.log(Level.INFO, "Registering Alpha biome source...");
		Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "alpha_biome_source"), CODEC);
		ModernBeta.LOGGER.log(Level.INFO, "Registered Alpha biome source.");
	}
	
}
