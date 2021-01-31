package com.bespectacled.modernbeta.gen;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.gen.provider.AbstractChunkProvider;
import com.bespectacled.modernbeta.gen.provider.AlphaChunkProvider;
import com.bespectacled.modernbeta.gen.provider.BetaChunkProvider;
import com.bespectacled.modernbeta.gen.provider.FlatChunkProvider;
import com.bespectacled.modernbeta.gen.provider.IndevChunkProvider;
import com.bespectacled.modernbeta.gen.provider.InfdevChunkProvider;
import com.bespectacled.modernbeta.gen.provider.InfdevOldChunkProvider;
import com.bespectacled.modernbeta.gen.provider.NetherChunkProvider;
import com.bespectacled.modernbeta.gen.provider.SkylandsChunkProvider;

import net.minecraft.nbt.CompoundTag;

public enum WorldType {
    BETA(0, "beta", BetaChunkProvider::new),
    SKYLANDS(1, "skylands", SkylandsChunkProvider::new),
    ALPHA(2, "alpha", AlphaChunkProvider::new),
    INFDEV(3, "infdev", InfdevChunkProvider::new),
    INFDEV_OLD(4, "infdev_old", InfdevOldChunkProvider::new),
    INDEV(5, "indev", IndevChunkProvider::new),
    FLAT(10, "flat", FlatChunkProvider::new),
    NETHER(99, "nether", NetherChunkProvider::new);
    
    private final int id;
    private final String name;
    private final BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider;
    
    private WorldType(int id, String name, BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider) {
        this.id = id;
        this.name = name;
        this.chunkProvider = chunkProvider;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> getChunkProvider() {
        return this.chunkProvider;
    }
    
    public static WorldType fromId(int id) {
        for (WorldType w : WorldType.values()) {
            if (w.id == id) {
                return w;
            }
        }
        
        throw new IllegalArgumentException("[Modern Beta] No world type matching id: " + id);
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