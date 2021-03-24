package com.bespectacled.modernbeta.gen;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.bespectacled.modernbeta.biome.beta.*;
import com.bespectacled.modernbeta.biome.classic.*;
import com.bespectacled.modernbeta.biome.indev.*;
import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.biome.CaveBiomeType;
import com.bespectacled.modernbeta.gen.provider.*;
import com.bespectacled.modernbeta.gui.*;
import com.bespectacled.modernbeta.util.PentaFunction;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;

public enum WorldType {
    BETA("beta", true, false, true, BiomeType.BETA, CaveBiomeType.VANILLA, BetaBiomes.FOREST_ID, BetaChunkProvider::new, InfCustomizeLevelScreen::new),
    SKYLANDS("skylands", false, false, true, BiomeType.SINGLE, CaveBiomeType.NONE, BetaBiomes.SKY_ID, SkylandsChunkProvider::new, InfCustomizeLevelScreen::new),
    ALPHA("alpha", true, false, true, BiomeType.SINGLE, CaveBiomeType.NONE, ClassicBiomes.ALPHA_ID, AlphaChunkProvider::new, InfCustomizeLevelScreen::new),
    INFDEV_415("infdev_415", true, false, true, BiomeType.SINGLE, CaveBiomeType.NONE, ClassicBiomes.INFDEV_415_ID, Infdev415ChunkProvider::new, InfCustomizeLevelScreen::new),
    INFDEV_227("infdev_227", true, false, false, BiomeType.SINGLE, CaveBiomeType.NONE, ClassicBiomes.INFDEV_227_ID, Infdev227ChunkProvider::new, InfdevOldCustomizeLevelScreen::new),
    INDEV("indev", false, false, false, BiomeType.SINGLE, CaveBiomeType.NONE, IndevBiomes.INDEV_NORMAL_ID, IndevChunkProvider::new, IndevCustomizeLevelScreen::new);
    //RELEASE("release", true, false, true, BiomeType.BETA, ReleaseChunkProvider::new, InfCustomizeLevelScreen::new);
    
    private final String name;
    private final boolean showOceansOption;
    private final boolean showDeepOceansOption;
    private final boolean showNoiseOptions;
    private final BiomeType defaultBiomeType;
    private final CaveBiomeType defaultCaveBiomeType;
    private final Identifier defaultBiome;
    private final BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider;
    private final PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractCustomizeLevelScreen> customizeScreen;
    
    private WorldType(
        String name,
        boolean showOceansOption,
        boolean showDeepOceansOption,
        boolean showNoiseOptions,
        BiomeType defaultBiomeType,
        CaveBiomeType defaultCaveBiomeType,
        Identifier defaultBiome,
        BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider, 
        PentaFunction<CreateWorldScreen, DynamicRegistryManager, NbtCompound, NbtCompound, BiConsumer<NbtCompound, NbtCompound>, AbstractCustomizeLevelScreen> customizeScreen
    ) {
        this.name = name;
        this.showOceansOption = showOceansOption;
        this.showDeepOceansOption = showDeepOceansOption;
        this.showNoiseOptions = showNoiseOptions;
        this.defaultBiomeType = defaultBiomeType;
        this.defaultCaveBiomeType = defaultCaveBiomeType;
        this.defaultBiome = defaultBiome;
        this.chunkProvider = chunkProvider;
        this.customizeScreen = customizeScreen;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean showOceansOption() {
        return this.showOceansOption;
    }
    
    public boolean showDeepOceansOption() {
        return this.showDeepOceansOption;
    }
    
    public boolean showNoiseOptions() {
        return this.showNoiseOptions;
    }
    
    public BiomeType getDefaultBiomeType() {
        return this.defaultBiomeType;
    }
    
    public CaveBiomeType getDefaultCaveBiomeType() {
        return this.defaultCaveBiomeType;
    }
    
    public Identifier getDefaultBiome() {
        return this.defaultBiome;
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, OldGeneratorSettings settings) {
        return this.chunkProvider.apply(seed, settings);
    }
    
    public AbstractCustomizeLevelScreen createLevelScreen(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings,
        NbtCompound chunkProviderSettings, 
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        return this.customizeScreen.apply(parent, registryManager, biomeProviderSettings, chunkProviderSettings, consumer);
    }
    
    public static WorldType fromName(String name) {
        for (WorldType w : WorldType.values()) {
            if (w.name.equalsIgnoreCase(name)) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching name: " + name);
    }
    
    public static WorldType getWorldType(NbtCompound settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
}