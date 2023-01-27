package com.bespectacled.modernbeta.world.biome.infdev;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomes;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class InfdevBiomes {
    protected static final boolean ADD_LAKES_INF_611 = false;
    protected static final boolean ADD_SPRINGS_INF_611 = false;
    
    protected static final boolean ADD_LAKES_INF_420 = false;
    protected static final boolean ADD_SPRINGS_INF_420 = false;
    
    protected static final boolean ADD_LAKES_INF_415 = false;
    protected static final boolean ADD_SPRINGS_INF_415 = false;
    
    protected static final boolean ADD_LAKES_INF_227 = false;
    protected static final boolean ADD_SPRINGS_INF_227 = false;

    public static final RegistryKey<Biome> INFDEV_611_KEY = ModernBetaBiomes.register(ModernBeta.createId("infdev_611"));

    public static final RegistryKey<Biome> INFDEV_420_KEY = ModernBetaBiomes.register(ModernBeta.createId("infdev_420"));
    
    public static final RegistryKey<Biome> INFDEV_415_KEY = ModernBetaBiomes.register(ModernBeta.createId("infdev_415"));
    
    public static final RegistryKey<Biome> INFDEV_227_KEY = ModernBetaBiomes.register(ModernBeta.createId("infdev_227"));

    public static void registerBiomes() {
        ModernBetaBiomes.register(INFDEV_611_KEY, Infdev611.BIOME);
        ModernBetaBiomes.register(INFDEV_420_KEY, Infdev420.BIOME);
        ModernBetaBiomes.register(INFDEV_415_KEY, Infdev415.BIOME);
        ModernBetaBiomes.register(INFDEV_227_KEY, Infdev227.BIOME);
    }
}
