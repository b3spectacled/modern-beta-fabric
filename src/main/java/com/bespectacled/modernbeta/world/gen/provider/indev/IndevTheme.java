package com.bespectacled.modernbeta.world.gen.provider.indev;

import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;

import net.minecraft.util.Identifier;

public enum IndevTheme {
    NORMAL(0, "normal", IndevBiomes.INDEV_NORMAL_ID),
    HELL(1, "hell", IndevBiomes.INDEV_HELL_ID),
    PARADISE(2, "paradise", IndevBiomes.INDEV_PARADISE_ID),
    WOODS(3, "woods", IndevBiomes.INDEV_WOODS_ID);
    
    private final int id;
    private final String name;
    private final Identifier defaultBiome;
    
    private IndevTheme(int id, String name, Identifier defaultBiome) {
        this.id = id;
        this.name = name;
        this.defaultBiome = defaultBiome;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Identifier getDefaultBiome() {
        return this.defaultBiome;
    }
    
    public static IndevTheme fromId(int id) {
        for (IndevTheme t : IndevTheme.values()) {
            if (t.id == id) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Theme matching id: " + id);
    }
    
    public static IndevTheme fromName(String name) {
        for (IndevTheme t : IndevTheme.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        
        throw new IllegalArgumentException("No Indev Theme matching name: " + name);
    }
    
    public static IndevTheme fromString(String str) {
        IndevTheme theme;
        
        if (str.matches("-?\\d+")) {
            theme = fromId(Integer.parseInt(str));
        } else {
            theme = fromName(str);
        }
        
        return theme;
    }
}