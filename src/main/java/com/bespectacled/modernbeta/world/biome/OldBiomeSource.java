package com.bespectacled.modernbeta.world.biome;

import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.biome.BiomeHeightSampler;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.api.world.biome.BiomeResolver;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeProviderSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final NbtCompound biomeProviderSettings;
    //private final Optional<NbtCompound> caveBiomeProviderSettings;
    
    private final BiomeProvider biomeProvider;
    @SuppressWarnings("unused")
    private BiomeHeightSampler biomeHeightSampler;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, NbtCompound settings) {
        super(Registries.BIOME.get(NbtUtil.readStringOrThrow(NbtTags.BIOME_TYPE, settings))
            .apply(seed, settings)
            .getBiomesForRegistry()
            .stream()
            .map((registryKey) -> (Biome) biomeRegistry.get(registryKey))
            .collect(Collectors.toList())
        );
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.biomeProviderSettings = settings;
        
        this.biomeProvider = Registries.BIOME.get(NbtUtil.readStringOrThrow(NbtTags.BIOME_TYPE, settings)).apply(seed, settings);
        this.biomeHeightSampler = BiomeHeightSampler.DEFAULT;
    }
    
    public void setBiomeHeightSampler(BiomeHeightSampler biomeHeightSampler) {
        this.biomeHeightSampler = biomeHeightSampler;
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {    
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(int x, int y, int z) {
        if (this.biomeProvider instanceof BiomeResolver biomeResolver) {
            return biomeResolver.getBiome(this.biomeRegistry, x, y, z);
        }
        
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, x >> 2, y >> 2, z >> 2);
    }
    
    public Biome getBiomeForSurfaceGen(ChunkRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeResolver biomeResolver)
            return biomeResolver.getBiome(this.biomeRegistry, pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }
    
    public long getWorldSeed() {
        return this.seed;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return new NbtCompound().copyFrom(this.biomeProviderSettings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.biomeProviderSettings);
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}