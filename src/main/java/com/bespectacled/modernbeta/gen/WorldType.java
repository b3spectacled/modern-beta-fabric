package com.bespectacled.modernbeta.gen;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.biome.BiomeType;
import com.bespectacled.modernbeta.gen.provider.*;
import com.bespectacled.modernbeta.gui.*;
import com.bespectacled.modernbeta.util.TriFunction;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.CompoundTag;

public enum WorldType {
    BETA("beta", true, true, BiomeType.BETA, BetaChunkProvider::new, InfCustomizeLevelScreen::new),
    SKYLANDS("skylands", false, true, BiomeType.SKY, SkylandsChunkProvider::new, InfCustomizeLevelScreen::new),
    ALPHA("alpha", true, true, BiomeType.CLASSIC, AlphaChunkProvider::new, InfCustomizeLevelScreen::new),
    INFDEV_415("infdev_415", true, true, BiomeType.CLASSIC, Infdev415ChunkProvider::new, InfCustomizeLevelScreen::new),
    INFDEV_227("infdev_227", true, false, BiomeType.CLASSIC, Infdev227ChunkProvider::new, InfdevOldCustomizeLevelScreen::new),
    INDEV("indev", false, false, null, IndevChunkProvider::new, IndevCustomizeLevelScreen::new);
    
    private final String name;
    private final boolean hasOceans;
    private final boolean isDensityBased;
    private final BiomeType defaultBiomeType;
    private final BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider;
    private final TriFunction<CreateWorldScreen, CompoundTag, Consumer<CompoundTag>, AbstractCustomizeLevelScreen> customizeScreen;
    
    private WorldType(
        String name,
        boolean hasOceans,
        boolean isDensityBased,
        BiomeType defaultBiomeType, 
        BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider, 
        TriFunction<CreateWorldScreen, CompoundTag, Consumer<CompoundTag>, AbstractCustomizeLevelScreen> customizeScreen
    ) {
        this.name = name;
        this.hasOceans = hasOceans;
        this.isDensityBased = isDensityBased;
        this.defaultBiomeType = defaultBiomeType;
        this.chunkProvider = chunkProvider;
        this.customizeScreen = customizeScreen;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean hasOceans() {
        return this.hasOceans;
    }
    
    public boolean isDensityBased() {
        return this.isDensityBased;
    }
    
    public BiomeType getDefaultBiomeType() {
        return this.defaultBiomeType;
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, OldGeneratorSettings settings) {
        return this.chunkProvider.apply(seed, settings);
    }
    
    public AbstractCustomizeLevelScreen createLevelScreen(CreateWorldScreen parent, CompoundTag providerSettings, Consumer<CompoundTag> consumer) {
        return this.customizeScreen.apply(parent, providerSettings, consumer);
    }
    
    public static WorldType fromName(String name) {
        for (WorldType w : WorldType.values()) {
            if (w.name.equalsIgnoreCase(name)) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching name: " + name);
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
}