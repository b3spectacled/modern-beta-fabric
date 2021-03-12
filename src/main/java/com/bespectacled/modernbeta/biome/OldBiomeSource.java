package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.beta.BetaClimateMap.BetaBiomeType;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.provider.*;
import com.bespectacled.modernbeta.gen.WorldType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            CompoundTag.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.settings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    
    private final WorldType worldType;
    private final BiomeType biomeType;
    
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, CompoundTag settings) {
        super(getBiomeProvider(seed, settings).getBiomesForRegistry().stream().map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey)));
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.settings = settings;
        
        this.worldType = WorldType.getWorldType(settings);
        this.biomeType = BiomeType.getBiomeType(settings);
        
        this.biomeProvider = getBiomeProvider(seed, settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(int x, int y, int z) {
        return this.biomeProvider.getBiomeForSurfaceGen(this.biomeRegistry, x, y, z);
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return this.biomeType == BiomeType.VANILLA;
    }
    
    public boolean isBeta() {
        return this.biomeType == BiomeType.BETA;
    }
    
    public boolean isSky() {
        return this.biomeType == BiomeType.SKY;
    }
    
    public boolean isIndev() {
        return this.worldType == WorldType.INDEV;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.settings);
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
    
    private static AbstractBiomeProvider getBiomeProvider(long seed, CompoundTag settings) {
        WorldType worldType = WorldType.getWorldType(settings);
        BiomeType biomeType = BiomeType.getBiomeType(settings);
        
        if (worldType == WorldType.INDEV)
            return new SingleBiomeProvider(seed, IndevBiomes.BIOMES.get(IndevTheme.fromName(settings.getString("levelTheme"))));
        
        switch(biomeType) {
            case BETA: return new BetaBiomeProvider(seed, BetaBiomeType.LAND);
            case SKY: return new SingleBiomeProvider(seed, BetaBiomes.SKY_ID);
            case PLUS: return new PlusBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType));
            case CLASSIC: return new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.CLASSIC));
            case WINTER: return new SingleBiomeProvider(seed, ClassicBiomes.getBiomeMap(worldType).get(BiomeType.WINTER));
            case VANILLA: return new VanillaBiomeProvider(seed);
            //case LUSH: return new SingleBiomeProvider(seed, new Identifier("lush_caves"));
            //case RELEASE: return new ReleaseBiomeProvider(seed);
            //case NETHER: return new NetherBiomeProvider(seed);
            default: throw new IllegalArgumentException("[Modern Beta] No biome provider matching biome type.  This shouldn't happen!");
        }
    }
}
