package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.IndevUtil;
import com.bespectacled.modernbeta.util.IndevUtil.Theme;
import com.bespectacled.modernbeta.util.IndevUtil.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class IndevBiomeSource extends BiomeSource {

    public static final Codec<IndevBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.LONG.fieldOf("seed").stable().forGetter(indevBiomeSource -> indevBiomeSource.seed),
                RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(indevBiomeSource -> indevBiomeSource.biomeRegistry),
                CompoundTag.CODEC.fieldOf("settings").forGetter(indevBiomeSource -> indevBiomeSource.settings)
            ).apply(instance, (instance).stable(IndevBiomeSource::new)));

    private final long seed;
    public final Registry<Biome> biomeRegistry;
    private final CompoundTag settings;
    
    private Type type;
    private Theme theme;
   
    private int width;
    private int length;
    private int height;

    public IndevBiomeSource(long seed, Registry<Biome> registry, CompoundTag settings) {
        super(IndevBiomes.getBiomeList().stream().map((registryKey) -> () -> (Biome) registry.get(registryKey)));

        this.seed = seed;
        this.biomeRegistry = registry;
        this.settings = settings;
        
        this.theme = Theme.NORMAL;
        this.type = Type.ISLAND;

        this.width = 256;
        this.length = 256;
        this.height = 128;
 
        if (this.settings.contains("levelType")) this.type = Type.values()[settings.getInt("levelType")];
        if (this.settings.contains("levelTheme")) this.theme = Theme.values()[settings.getInt("levelTheme")];
        
        if (this.settings.contains("levelWidth")) this.width = settings.getInt("levelWidth");
        if (this.settings.contains("levelLength")) this.length = settings.getInt("levelLength");
        if (this.settings.contains("levelHeight")) this.height = settings.getInt("levelHeight");
    }

    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        Biome biome;
        
        if (IndevUtil.inIndevRegion(absX, absZ, this.width, this.length)) {
            switch(theme) {
                case NORMAL:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_NORMAL_ID);
                    break;
                case HELL:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_HELL_ID);
                    break;
                case PARADISE:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_PARADISE_ID);
                    break;
                case WOODS:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_WOODS_ID);
                    break;  
                case SNOWY:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_SNOWY_ID);
                    break;
                default:
                    biome = biomeRegistry.get(IndevBiomes.INDEV_NORMAL_ID);
            }
        } else {
            switch(theme) {
            case NORMAL:
                biome = biomeRegistry.get(IndevBiomes.INDEV_EDGE_ID);
                break;
            case HELL:
                biome = biomeRegistry.get(IndevBiomes.INDEV_HELL_EDGE_ID);
                break;
            case PARADISE:
                biome = biomeRegistry.get(IndevBiomes.INDEV_PARADISE_EDGE_ID);
                break; 
            case WOODS:
                biome = biomeRegistry.get(IndevBiomes.INDEV_WOODS_EDGE_ID);
                break;  
            case SNOWY:
                biome = biomeRegistry.get(IndevBiomes.INDEV_SNOWY_EDGE_ID);
                break;
            default:
                biome = biomeRegistry.get(IndevBiomes.INDEV_EDGE_ID);
        }
        }
        
        return biome;
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
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Indev biome source.");
    }

}
