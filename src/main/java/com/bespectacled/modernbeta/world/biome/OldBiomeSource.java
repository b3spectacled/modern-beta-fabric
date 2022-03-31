package com.bespectacled.modernbeta.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.biome.BiomeBlockResolver;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.OceanBiomeResolver;
import com.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.ImmutableSettings;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class OldBiomeSource extends BiomeSource {
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            ImmutableSettings.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings),
            ImmutableSettings.CODEC.fieldOf("cave_provider_settings").forGetter(biomeSource -> biomeSource.caveBiomeSettings),
            Codec.INT.optionalFieldOf("version").forGetter(generator -> generator.version)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final ImmutableSettings biomeSettings;
    private final ImmutableSettings caveBiomeSettings;
    private final Optional<Integer> version;
    
    private final BiomeProvider biomeProvider;
    private final CaveBiomeProvider caveBiomeProvider;

    private static List<RegistryEntry<Biome>> getBiomesForRegistry(
        long seed,
        Registry<Biome> biomeRegistry, 
        ImmutableSettings biomeSettings,
        ImmutableSettings caveBiomeSettings
    ) {
        List<RegistryEntry<Biome>> mainBiomes = Registries.BIOME
            .get(NbtUtil.toStringOrThrow(biomeSettings.get(NbtTags.BIOME_TYPE)))
            .apply(seed, biomeSettings, biomeRegistry)
            .getBiomesForRegistry();
        
        List<RegistryEntry<Biome>> caveBiomes = Registries.CAVE_BIOME
            .get(NbtUtil.toStringOrThrow(caveBiomeSettings.get(NbtTags.CAVE_BIOME_TYPE)))
            .apply(seed, caveBiomeSettings, biomeRegistry)
            .getBiomesForRegistry();
        
        List<RegistryEntry<Biome>> biomes = new ArrayList<>();
        biomes.addAll(mainBiomes);
        biomes.addAll(caveBiomes);
        
        return biomes;
    }
    
    public OldBiomeSource(
        long seed,
        Registry<Biome> biomeRegistry,
        ImmutableSettings biomeSettings,
        ImmutableSettings caveBiomeSettings,
        Optional<Integer> version
    ) {
        super(getBiomesForRegistry(seed, biomeRegistry, biomeSettings, caveBiomeSettings));
        
        // Validate mod version
        ModernBeta.validateVersion(version);
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.biomeSettings = biomeSettings;
        this.caveBiomeSettings = caveBiomeSettings;
        this.version = version;
        
        this.biomeProvider = Registries.BIOME
            .get(NbtUtil.toStringOrThrow(biomeSettings.get(NbtTags.BIOME_TYPE)))
            .apply(seed, biomeSettings, biomeRegistry);
        
        this.caveBiomeProvider = Registries.CAVE_BIOME
            .get(NbtUtil.toStringOrThrow(caveBiomeSettings.get(NbtTags.CAVE_BIOME_TYPE)))
            .apply(seed, caveBiomeSettings, biomeRegistry);
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(
            seed,
            this.biomeRegistry,
            this.biomeSettings,
            this.caveBiomeSettings,
            this.version
        );
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {    
        return this.biomeProvider.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }

    public RegistryEntry<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        if (this.biomeProvider instanceof OceanBiomeResolver oceanBiomeResolver)
            return oceanBiomeResolver.getOceanBiomeForNoiseGen(biomeX, biomeY, biomeZ);
        
        return this.biomeProvider.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        if (this.biomeProvider instanceof OceanBiomeResolver oceanBiomeResolver)
            return oceanBiomeResolver.getDeepOceanBiomeForNoiseGen(biomeX, biomeY, biomeZ);
        
        return this.biomeProvider.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getCaveBiome(int biomeX, int biomeY, int biomeZ) {
        return this.caveBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getBiomeForSurfaceGen(int x, int y, int z) {
        if (this.biomeProvider instanceof BiomeBlockResolver biomeResolver) {
            return biomeResolver.getBiomeAtBlock(x, y, z);
        }
        
        return this.biomeProvider.getBiomeForNoiseGen(x >> 2, y >> 2, z >> 2);
    }
    
    public RegistryEntry<Biome> getBiomeForSurfaceGen(ChunkRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeBlockResolver biomeResolver)
            return biomeResolver.getBiomeAtBlock(pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CaveBiomeProvider getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public Settings getBiomeSettings() {
        return this.biomeSettings;
    }
    
    public Settings getCaveBiomeSettings() {
        return this.caveBiomeSettings;
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }
}