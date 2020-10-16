package com.bespectacled.modernbeta.biome;

import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

public class IndevBiomeSource extends BiomeSource {

    public static final Codec<IndevBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(indevBiomeSource -> indevBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(indevBiomeSource -> indevBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(indevBiomeSource -> indevBiomeSource.settings)
            ).apply(instance, (instance).stable(IndevBiomeSource::new)));

    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "indev_edge")),
            RegistryKey.of(Registry.BIOME_KEY, new Identifier(ModernBeta.ID, "indev_normal")));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;

    public IndevBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(BIOMES.stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        if (settings == null) {
            ModernBeta.LOGGER.log(Level.ERROR, "Save file does not have generator settings, probably created before v0.4.");
            return;
        }


    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        if (inIndevRegion(biomeX, biomeZ)) {
            return biomeRegistry.get(new Identifier(ModernBeta.ID, "indev_normal"));
        }
        
        return biomeRegistry.get(new Identifier(ModernBeta.ID, "indev_edge"));
    }
    
    private boolean inIndevRegion(int biomeX, int biomeZ) {
        int biomeWidth = 256 >> 2;
        int biomeLength = 256 >> 2;
        
        if (biomeX >= 0 && biomeX < biomeWidth && biomeZ >= 0 && biomeZ < biomeLength)
            return true;
        
        return false;
    }
    

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return IndevBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new IndevBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(ModernBeta.ID, "indev_biome_source"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Indev biome source.");
    }

}
