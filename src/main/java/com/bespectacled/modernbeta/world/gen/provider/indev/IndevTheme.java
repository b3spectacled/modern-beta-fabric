package com.bespectacled.modernbeta.world.gen.provider.indev;

import com.bespectacled.modernbeta.world.biome.indev.IndevBiomes;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public enum IndevTheme {
    NORMAL(0, "normal", IndevBiomes.INDEV_NORMAL_KEY.getValue(), Formatting.GREEN),
    HELL(1, "hell", IndevBiomes.INDEV_HELL_KEY.getValue(), Formatting.RED),
    PARADISE(2, "paradise", IndevBiomes.INDEV_PARADISE_KEY.getValue(), Formatting.YELLOW),
    WOODS(3, "woods", IndevBiomes.INDEV_WOODS_KEY.getValue(), Formatting.AQUA);
    
    private static final String PREFIX = "createWorld.customize.indev.levelTheme";
    
    private final int id;
    private final String name;
    private final Identifier defaultBiome;
    private final Formatting color;
    
    private IndevTheme(int id, String name, Identifier defaultBiome, Formatting color) {
        this.id = id;
        this.name = name;
        this.defaultBiome = defaultBiome;
        this.color = color;
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
    
    public Formatting getColor() {
        return this.color;
    }
    
    public String getDescription() {
        return String.format("%s.%s.description", PREFIX, this.name);
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