package mod.bespectacled.modernbeta.world.biome;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.api.world.biome.BiomeBlockResolver;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.OceanBiomeResolver;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.settings.ModernBetaBiomeSettings;
import mod.bespectacled.modernbeta.settings.ModernBetaCaveBiomeSettings;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public class ModernBetaBiomeSource extends BiomeSource {
    public static final Codec<ModernBetaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            RegistryOps.getEntryLookupCodec(RegistryKeys.BIOME),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings),
            NbtCompound.CODEC.fieldOf("cave_provider_settings").forGetter(biomeSource -> biomeSource.caveBiomeSettings)
        ).apply(instance, (instance).stable(ModernBetaBiomeSource::new)));
    
    private final NbtCompound biomeSettings;
    private final NbtCompound caveBiomeSettings;
    
    private final BiomeProvider biomeProvider;
    private final CaveBiomeProvider caveBiomeProvider;
    
    private boolean initializedBiomeProvider;
    private boolean initializedCaveBiomeProvider;
    
    private static List<RegistryEntry<Biome>> getBiomesForRegistry(
        RegistryEntryLookup<Biome> biomeRegistry,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        ModernBetaBiomeSettings modernBetaBiomeSettings = new ModernBetaBiomeSettings.Builder(biomeSettings).build();
        ModernBetaCaveBiomeSettings modernBetaCaveBiomeSettings = new ModernBetaCaveBiomeSettings.Builder(caveBiomeSettings).build();
        
        BiomeProvider biomeProvider  = Registries.BIOME
            .get(modernBetaBiomeSettings.biomeProvider)
            .apply(biomeSettings, biomeRegistry);
        
        CaveBiomeProvider caveBiomeProvider = Registries.CAVE_BIOME
            .get(modernBetaCaveBiomeSettings.biomeProvider)
            .apply(caveBiomeSettings, biomeRegistry);
        
        biomeProvider.initProvider(0L);
        caveBiomeProvider.initProvider(0L);
        
        List<RegistryEntry<Biome>> biomes = new ArrayList<>();
        biomes.addAll(biomeProvider.getBiomesForRegistry());
        biomes.addAll(caveBiomeProvider.getBiomesForRegistry());
        
        return biomes;
    }
    
    public ModernBetaBiomeSource(
        RegistryEntryLookup<Biome> biomeRegistry,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        super(getBiomesForRegistry(biomeRegistry, biomeSettings, caveBiomeSettings));
        
        this.biomeSettings = biomeSettings;
        this.caveBiomeSettings = caveBiomeSettings;
        
        ModernBetaBiomeSettings modernBetaBiomeSettings = new ModernBetaBiomeSettings.Builder(this.biomeSettings).build();
        ModernBetaCaveBiomeSettings modernBetaCaveBiomeSettings = new ModernBetaCaveBiomeSettings.Builder(this.caveBiomeSettings).build();
        
        this.biomeProvider = Registries.BIOME
            .get(modernBetaBiomeSettings.biomeProvider)
            .apply(biomeSettings, biomeRegistry);
        
        this.caveBiomeProvider = Registries.CAVE_BIOME
            .get(modernBetaCaveBiomeSettings.biomeProvider)
            .apply(caveBiomeSettings, biomeRegistry);
        
        this.initializedBiomeProvider = false;
        this.initializedCaveBiomeProvider = false;
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        this.initializeBiomeProvider();

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
        this.initializeCaveBiomeProvider();
        
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
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CaveBiomeProvider getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public NbtCompound getBiomeSettings() {
        return this.biomeSettings;
    }
    
    public NbtCompound getCaveBiomeSettings() {
        return this.caveBiomeSettings;
    }
    
    public long getWorldSeed() {
        return ModernBeta.getWorldSeed();
    }

    public static void register() {
        Registry.register(net.minecraft.registry.Registries.BIOME_SOURCE, ModernBeta.createId(ModernBeta.MOD_ID), CODEC);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return ModernBetaBiomeSource.CODEC;
    }
    
    private void initializeBiomeProvider() {
        if (!this.initializedBiomeProvider)
            this.initializedBiomeProvider = this.biomeProvider.initProvider(this.getWorldSeed());
    }
    
    private void initializeCaveBiomeProvider() {
        if (!this.initializedCaveBiomeProvider)
            this.initializedCaveBiomeProvider = this.caveBiomeProvider.initProvider(this.getWorldSeed());
    }
}