package com.bespectacled.modernbeta.world.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.IBiomeResolver;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry;
import com.bespectacled.modernbeta.api.registry.BiomeProviderRegistry.BuiltInBiomeType;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;
import com.bespectacled.modernbeta.world.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.world.biome.provider.SingleBiomeProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.RegistryLookupCodec;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class OldBiomeSource extends BiomeSource {
    
    public static final Codec<OldBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.LONG.fieldOf("seed").stable().forGetter(biomeSource -> biomeSource.seed),
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(biomeSource -> biomeSource.biomeRegistry),
            CompoundTag.CODEC.fieldOf("settings").forGetter(biomeSource -> biomeSource.biomeProviderSettings)
        ).apply(instance, (instance).stable(OldBiomeSource::new)));
    
    private final long seed;
    private final Registry<Biome> biomeRegistry;
    private final CompoundTag biomeProviderSettings;
    
    private final String biomeProviderType;
    private final AbstractBiomeProvider biomeProvider;
    
    public OldBiomeSource(long seed, Registry<Biome> biomeRegistry, CompoundTag settings) {
        super(
            loadBiomeProvider(seed, settings)
            .getBiomesForRegistry()
            .stream()
            .map((registryKey) -> () -> (Biome) biomeRegistry.get(registryKey))
        );
        
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        this.biomeProviderSettings = settings;
        
        this.biomeProviderType = BiomeProviderRegistry.getBiomeProviderType(settings);
        this.biomeProvider = loadBiomeProvider(seed, settings);
        
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
            return ((IBiomeResolver)this.biomeProvider).getBiome(this.biomeRegistry, pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    public boolean isVanilla() {
        return this.biomeProviderType.equals(BuiltInBiomeType.VANILLA.name);
    }
    
    public boolean isBeta() {
        return this.biomeProviderType.equals(BuiltInBiomeType.BETA.name);
    }
    
    public boolean isSingle() {
        return this.biomeProviderType.equals(BuiltInBiomeType.SINGLE.name);
    }
    
    public String getBiomeProviderType() {
        return this.biomeProviderType;
    }
    
    public AbstractBiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CompoundTag getProviderSettings() {
        return this.biomeProviderSettings;
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
    
    private static AbstractBiomeProvider loadBiomeProvider(long seed, CompoundTag settings) {
        String oldWorldType = settings.getString("worldType");
        String oldBiomeType = settings.getString("biomeType");
        String oldIndevTheme = settings.getString("levelTheme");

        Identifier biomeId = null;
        
        // Load legacy Indev settings if present
        if (oldWorldType.equals("indev")) {
            if (oldIndevTheme.equals(IndevTheme.HELL.getName()))
                biomeId = IndevBiomes.INDEV_HELL_ID;
            else if (oldIndevTheme.equals(IndevTheme.PARADISE.getName()))
                biomeId = IndevBiomes.INDEV_PARADISE_ID;
            else if (oldIndevTheme.equals(IndevTheme.WOODS.getName()))
                biomeId = IndevBiomes.INDEV_WOODS_ID;
            else if (oldIndevTheme.equals(IndevTheme.NORMAL.getName()))
                biomeId = IndevBiomes.INDEV_SNOWY_ID;
            else 
                biomeId = IndevBiomes.INDEV_NORMAL_ID;
        }
        
        // Load legacy Classic settings if present
        if (oldWorldType.equals("alpha")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.name) || oldBiomeType.equals(BuiltInBiomeType.PLUS.name)) 
                biomeId = ClassicBiomes.ALPHA_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.name)) 
                biomeId = ClassicBiomes.ALPHA_WINTER_ID;
        } else if (oldWorldType.equals("infdev")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.name) || oldBiomeType.equals(BuiltInBiomeType.PLUS.name)) 
                biomeId = ClassicBiomes.INFDEV_415_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.name)) 
                biomeId = ClassicBiomes.INFDEV_415_WINTER_ID;
        } else if (oldWorldType.equals("alpha")) {
            if (oldBiomeType.equals(BuiltInBiomeType.CLASSIC.name) || oldBiomeType.equals(BuiltInBiomeType.PLUS.name)) 
                biomeId = ClassicBiomes.INFDEV_227_ID;
            if (oldBiomeType.equals(BuiltInBiomeType.WINTER.name)) 
                biomeId = ClassicBiomes.INFDEV_227_WINTER_ID;
        }
        
        // Load legacy Sky settings if present
        if (oldBiomeType.equals(BuiltInBiomeType.SKY.name)) 
            biomeId = BetaBiomes.SKY_ID;
        
        if (biomeId != null) {
            settings.putString("singleBiome", biomeId.toString());
            return new SingleBiomeProvider(seed, settings);
        }
        
        return BiomeProviderRegistry.get(BiomeProviderRegistry.getBiomeProviderType(settings)).apply(seed, settings);
    }
}
