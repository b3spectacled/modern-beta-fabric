package com.bespectacled.modernbeta.util;

import java.util.List;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.InfBiomes;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class WorldEnum {
    
    public enum WorldType {
        BETA(0, "beta"),
        SKYLANDS(1, "skylands"),
        ALPHA(2, "alpha"),
        INFDEV(3, "infdev"),
        INFDEV_OLD(4, "infdev_old"),
        INDEV(5, "indev");
        
        private final int id;
        private final String name;
        
        private WorldType(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static WorldType fromId(int id) {
            for (WorldType w : WorldType.values()) {
                if (w.id == id) {
                    return w;
                }
            }
            
            throw new IllegalArgumentException("No world type matching id: " + id);
        }
        
        public static WorldType fromName(String name) {
            for (WorldType w : WorldType.values()) {
                if (w.name.equalsIgnoreCase(name)) {
                    return w;
                }
            }
            
            throw new IllegalArgumentException("No world type matching name: " + name);
        }
    }
    
    public enum BiomeType {
        BETA(0, "beta"),
        //ICE_DESERT(1, "ice_desert"),
        SKY(2, "sky"),
        CLASSIC(3, "classic"),
        WINTER(4, "winter"),
        PLUS(5, "plus"),
        VANILLA(6, "vanilla");
        
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
            
            throw new IllegalArgumentException("No biome type matching id: " + id);
        }
        
        public static BiomeType fromName(String name) {
            for (BiomeType t : BiomeType.values()) {
                if (t.name.equalsIgnoreCase(name)) {
                    return t;
                }
            }
            
            throw new IllegalArgumentException("No biome type matching name: " + name);
        }
        
        public static List<RegistryKey<Biome>> getBiomeRegistryList(CompoundTag settings) {
            WorldType worldType = getWorldType(settings);
            BiomeType biomeType = getBiomeType(settings);
            
            if (biomeType == BiomeType.VANILLA)
                return BiomeUtil.VANILLA_BIOMES;
            
            if (biomeType == BiomeType.BETA || biomeType == BiomeType.SKY)
                return BetaBiomes.getBiomeRegistryList();
            
            return InfBiomes.getBiomeRegistryList(worldType);
        }
        
    }
    
    public static WorldType getWorldType(CompoundTag settings) {
        WorldType type = WorldType.BETA;
        
        if (settings.contains("worldType"))
            type = WorldType.fromName(settings.getString("worldType"));
        
        return type;
    }
    
    public static BiomeType getBiomeType(CompoundTag settings) {
        BiomeType type = BiomeType.BETA;
        
        if (settings.contains("biomeType")) 
            type = BiomeType.fromName(settings.getString("biomeType"));
        
        return type;
    }
}
