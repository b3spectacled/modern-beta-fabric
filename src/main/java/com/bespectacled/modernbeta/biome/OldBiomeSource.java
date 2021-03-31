package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.biome.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.biome.BiomeProviderType;
import com.bespectacled.modernbeta.api.biome.IBiomeResolver;
import com.bespectacled.modernbeta.api.biome.BiomeProviderType.BuiltInBiomeType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            NbtCompound.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.providerSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final NbtCompound providerSettings;
    
    private final String biomeProviderType;
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, NbtCompound settings) {
        super(
            BiomeProviderType.getProvider(BiomeProviderType.getBiomeProviderType(settings))
            .apply(seed, settings)
            .getBiomesForRegistry()
            .stream()
            .map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey))
        );
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.providerSettings = settings;
        
        this.biomeProviderType = BiomeProviderType.getBiomeProviderType(settings);
        this.biomeProvider = BiomeProviderType.getProvider(this.biomeProviderType).apply(seed, settings);
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }

    public Biome getOceanBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeProvider.getOceanBiomeForNoiseGen(this.biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    public Biome getBiomeForSurfaceGen(ChunkRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof IBiomeResolver)
            return ((IBiomeResolver)this.biomeProvider).getBiome(this.biomeRegistry, pos.getX(), 0, pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return this.biomeProviderType.equals(BuiltInBiomeType.VANILLA.id);
    }
    
    public boolean isBeta() {
        return this.biomeProviderType.equals(BuiltInBiomeType.BETA.id);
    }
    
    public boolean isSingle() {
        return this.biomeProviderType.equals(BuiltInBiomeType.SINGLE.id);
    }
    
    public String getBiomeType() {
        return this.biomeProviderType;
    }
    
    public AbstractBiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public NbtCompound getProviderSettings() {
        return this.providerSettings;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public BiomeSource withSeed(long seed) {
        return new OldBiomeSource(seed, this.biomeRegistry, this.providerSettings);
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return OldBiomeSource.CODEC;
    }

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, ModernBeta.createId("old"), CODEC);
    }
}
