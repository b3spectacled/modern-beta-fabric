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
    BETA("beta", BetaChunkProvider::new),
    SKYLANDS("skylands", SkylandsChunkProvider::new),
    ALPHA("alpha", AlphaChunkProvider::new),
    INFDEV("infdev", InfdevChunkProvider::new),
    INFDEV_OLD("infdev_old", InfdevOldChunkProvider::new),
    INDEV("indev", IndevChunkProvider::new),
    FLAT("flat", FlatChunkProvider::new),
    NETHER("nether", NetherChunkProvider::new);
    
    private final String name;
    private final BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider;
    
    private WorldType(String name, BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider) {
        this.name = name;
        this.chunkProvider = chunkProvider;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AbstractChunkProvider createChunkProvider(long seed, OldGeneratorSettings settings) {
        return this.chunkProvider.apply(seed, settings);
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