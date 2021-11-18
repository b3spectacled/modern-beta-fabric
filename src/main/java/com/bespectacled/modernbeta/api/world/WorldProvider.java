package com.bespectacled.modernbeta.api.world;

public final class WorldProvider {
    private final String chunkProvider;
    private final String worldScreen;

    private final String biomeProvider;
    private final String caveBiomeProvider;
    private final String singleBiome;
    
    private final boolean generateOceanShrines;
    private final boolean generateMonuments;
    
    private final boolean generateDeepslate;
    private final boolean showGenerateDeepslate;

    public WorldProvider(
        String chunkProvider,
        String biomeProvider,
        String caveBiomeProvider,
        String singleBiome,
        String worldScreen,
        boolean generateOceanShrines,
        boolean generateMonuments,
        boolean generateDeepslate,
        boolean showGenerateDeepslate
    ) {
        this.chunkProvider = chunkProvider;
        this.worldScreen = worldScreen;
        
        this.biomeProvider = biomeProvider;
        this.caveBiomeProvider = caveBiomeProvider;
        this.singleBiome = singleBiome;
        
        this.generateOceanShrines = generateOceanShrines;
        this.generateMonuments = generateMonuments;
        
        this.generateDeepslate = generateDeepslate;
        this.showGenerateDeepslate = showGenerateDeepslate;
    }
    
    public String getChunkProvider() {
        return this.chunkProvider;
    }

    public String getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public String getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public String getSingleBiome() {
        return this.singleBiome;
    }
    
    public String getWorldScreen() {
        return this.worldScreen;
    }
    
    public boolean generateOceanShrines() {
        return this.generateOceanShrines;
    }
    
    public boolean generateMonuments() {
        return this.generateMonuments;
    }
    
    public boolean generateDeepslate() {
        return this.generateDeepslate;
    }
    
    public boolean showGenerateDeepslate() {
        return this.showGenerateDeepslate;
    }
    
    @Override
    public String toString() {
        return this.chunkProvider;
    }
    
    public String asString() {
        return String.format(
            "[World Provider]\n" +
                "* Chunk Provider: %s\n" +
                "* Biome Provider: %s\n" +
                "* Cave Biome Provider: %s\n" +
                "* Single Biome: %s\n" +
                "* World Screen: %s",
            this.chunkProvider,
            this.biomeProvider, 
            this.caveBiomeProvider, 
            this.singleBiome, 
            this.worldScreen
        );
    }
}
