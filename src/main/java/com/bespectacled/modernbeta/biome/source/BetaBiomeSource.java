package com.bespectacled.modernbeta.biome.source;

import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.biome.beta.Desert;
import com.bespectacled.modernbeta.biome.beta.Forest;
import com.bespectacled.modernbeta.biome.beta.IceDesert;
import com.bespectacled.modernbeta.biome.beta.Plains;
import com.bespectacled.modernbeta.biome.beta.Rainforest;
import com.bespectacled.modernbeta.biome.beta.Savanna;
import com.bespectacled.modernbeta.biome.beta.SeasonalForest;
import com.bespectacled.modernbeta.biome.beta.Shrubland;
import com.bespectacled.modernbeta.biome.beta.Swampland;
import com.bespectacled.modernbeta.biome.beta.Taiga;
import com.bespectacled.modernbeta.biome.beta.Tundra;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class BetaBiomeSource extends BiomeSource implements IOldBiomeSource {
    public static final Codec<BetaBiomeSource> CODEC;
    private static final double[] TEMP_HUMID_POINT = new double[2];
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    
    private final Supplier<Biome> desertBiome;
    private final Supplier<Biome> forestBiome;
    private final Supplier<Biome> iceDesertBiome;
    private final Supplier<Biome> plainsBiome;
    private final Supplier<Biome> rainforestBiome;
    private final Supplier<Biome> savannaBiome;
    private final Supplier<Biome> shrublandBiome;
    private final Supplier<Biome> seasonalForestBiome;
    private final Supplier<Biome> swamplandBiome;
    private final Supplier<Biome> taigaBiome;
    private final Supplier<Biome> tundraBiome;
    
    public BetaBiomeSource(long seed, Registry<Biome> biomeRegistry) {
        this(
            seed,
            biomeRegistry,
            () -> Desert.BIOME,
            () -> Forest.BIOME,
            () -> IceDesert.BIOME,
            () -> Plains.BIOME,
            () -> Rainforest.BIOME,
            () -> Savanna.BIOME,
            () -> SeasonalForest.BIOME,
            () -> Shrubland.BIOME,
            () -> Swampland.BIOME,
            () -> Taiga.BIOME,
            () -> Tundra.BIOME
        );
    }

    public BetaBiomeSource(
        long seed, 
        Registry<Biome> biomeRegistry,
        Supplier<Biome> desertBiome,
        Supplier<Biome> forestBiome,
        Supplier<Biome> iceDesertBiome,
        Supplier<Biome> plainsBiome,
        Supplier<Biome> rainforestBiome,
        Supplier<Biome> savannaBiome,
        Supplier<Biome> seasonalForestBiome,
        Supplier<Biome> shrublandBiome,
        Supplier<Biome> swamplandBiome,
        Supplier<Biome> taigaBiome,
        Supplier<Biome> tundraBiome
    ) {
        super(List.of(
            desertBiome.get(),
            forestBiome.get(),
            iceDesertBiome.get(),
            plainsBiome.get(),
            rainforestBiome.get(),
            savannaBiome.get(),
            seasonalForestBiome.get(),
            shrublandBiome.get(),
            swamplandBiome.get(),
            taigaBiome.get(),
            tundraBiome.get()
        ));

        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.desertBiome = desertBiome;
        this.forestBiome = forestBiome;
        this.iceDesertBiome = iceDesertBiome;
        this.plainsBiome = plainsBiome;
        this.rainforestBiome = rainforestBiome;
        this.savannaBiome = savannaBiome;
        this.seasonalForestBiome = seasonalForestBiome;
        this.shrublandBiome = shrublandBiome;
        this.swamplandBiome = swamplandBiome;
        this.taigaBiome = taigaBiome;
        this.tundraBiome = tundraBiome;
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        double temp = BetaClimateSampler.INSTANCE.sampleTemp(absX, absZ);
        double humid = BetaClimateSampler.INSTANCE.sampleHumid(absX, absZ) * temp;
        
        if (temp < 0.1F) {
            return this.tundraBiome.get();
        }

        if (humid < 0.2F) {
            if (temp < 0.5F) {
                return this.tundraBiome.get();
            }
            if (temp < 0.95F) {
                return this.savannaBiome.get();
            } else {
                return this.desertBiome.get();
            }
        }

        if (humid > 0.5F && temp < 0.7F) {
            return this.swamplandBiome.get();
        }

        if (temp < 0.5F) {
            return this.taigaBiome.get();
        }

        if (temp < 0.97F) {
            if (humid < 0.35F) {
                return this.shrublandBiome.get();
            } else {
                return this.forestBiome.get();
            }
        }

        if (humid < 0.45F) {
            return this.plainsBiome.get();
        }

        if (humid < 0.9F) {
            return this.seasonalForestBiome.get();
        } else {
            return this.rainforestBiome.get();
        }
    }
    
    @Override
    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public BiomeSource withSeed(long seed) {
        return new BetaBiomeSource(
            seed, 
            this.biomeRegistry,
            this.desertBiome,
            this.forestBiome,
            this.iceDesertBiome,
            this.plainsBiome,
            this.rainforestBiome,
            this.savannaBiome,
            this.seasonalForestBiome,
            this.shrublandBiome,
            this.swamplandBiome,
            this.taigaBiome,
            this.tundraBiome
        );
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
                Biome.REGISTRY_CODEC.fieldOf("desertBiome").stable().forGetter(biomeSource -> biomeSource.desertBiome),
                Biome.REGISTRY_CODEC.fieldOf("forestBiome").stable().forGetter(biomeSource -> biomeSource.forestBiome),
                Biome.REGISTRY_CODEC.fieldOf("iceDesertBiome").stable().forGetter(biomeSource -> biomeSource.iceDesertBiome),
                Biome.REGISTRY_CODEC.fieldOf("plainsBiome").stable().forGetter(biomeSource -> biomeSource.plainsBiome),
                Biome.REGISTRY_CODEC.fieldOf("rainforestBiome").stable().forGetter(biomeSource -> biomeSource.rainforestBiome),
                Biome.REGISTRY_CODEC.fieldOf("savannaBiome").stable().forGetter(biomeSource -> biomeSource.savannaBiome),
                Biome.REGISTRY_CODEC.fieldOf("seasonalForestBiome").stable().forGetter(biomeSource -> biomeSource.seasonalForestBiome),
                Biome.REGISTRY_CODEC.fieldOf("shrublandBiome").stable().forGetter(biomeSource -> biomeSource.shrublandBiome),
                Biome.REGISTRY_CODEC.fieldOf("swamplandBiome").stable().forGetter(biomeSource -> biomeSource.swamplandBiome),
                Biome.REGISTRY_CODEC.fieldOf("taigaBiome").stable().forGetter(biomeSource -> biomeSource.taigaBiome),
                Biome.REGISTRY_CODEC.fieldOf("tundraBiome").stable().forGetter(biomeSource -> biomeSource.tundraBiome)
            ).apply(instance, (instance).stable(BetaBiomeSource::new)));
    }
}
