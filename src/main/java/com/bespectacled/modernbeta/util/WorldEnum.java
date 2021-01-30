package com.bespectacled.modernbeta.util;

import java.util.function.BiFunction;

import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.gen.provider.*;

import net.minecraft.nbt.CompoundTag;

public class WorldEnum {
    
    public enum WorldType {
        FLAT(-1, "flat", false, FlatChunkProvider::new),
        BETA(0, "beta", true, BetaChunkProvider::new),
        SKYLANDS(1, "skylands", false, SkylandsChunkProvider::new),
        ALPHA(2, "alpha", true, AlphaChunkProvider::new),
        INFDEV(3, "infdev", true, InfdevChunkProvider::new),
        INFDEV_OLD(4, "infdev_old", true, InfdevOldChunkProvider::new),
        INDEV(5, "indev", false, IndevChunkProvider::new),
        NETHER(99, "nether", false, NetherChunkProvider::new);
        
        private final int id;
        private final String name;
        private final boolean hasOceans;
        private final BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider;
        
        private WorldType(int id, String name, boolean hasOceans, BiFunction<Long, OldGeneratorSettings, AbstractChunkProvider> chunkProvider) {
            this.id = id;
            this.name = name;
            this.hasOceans = hasOceans;
            this.chunkProvider = chunkProvider;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean hasOceans() {
            return this.hasOceans;
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
    
    public enum BiomeType {
        BETA(0, "beta"),
        SKY(1, "sky"),
        CLASSIC(2, "classic"),
        WINTER(3, "winter"),
        PLUS(4, "plus"),
        VANILLA(5, "vanilla");
        //NETHER(7, "nether");
        
        private final int id;
        private final String name;
        
        private BiomeType(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BiomeType fromId(int id) {
            for (BiomeType t : BiomeType.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("[Modern Beta] No biome type matching id: " + id);
        }
        
        public static BiomeType fromName(String name) {
            for (BiomeType t : BiomeType.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("[Modern Beta] No biome type matching name: " + name);
        }
        
        public static BiomeType getBiomeType(CompoundTag settings) {
            BiomeType type = BiomeType.BETA;
            
            if (settings.contains("biomeType")) 
                type = BiomeType.fromName(settings.getString("biomeType"));
            
            return type;
        }
    }
}
