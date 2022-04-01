package com.bespectacled.modernbeta.world.biome.indev;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class IndevBiomes {
    protected static final boolean ADD_LAKES = false;
    protected static final boolean ADD_SPRINGS = false;
    
    public static final RegistryKey<Biome> INDEV_NORMAL_KEY = OldBiomes.register(ModernBeta.createId("indev_normal"));
    public static final RegistryKey<Biome> INDEV_HELL_KEY = OldBiomes.register(ModernBeta.createId("indev_hell"));
    public static final RegistryKey<Biome> INDEV_PARADISE_KEY = OldBiomes.register(ModernBeta.createId("indev_paradise"));
    public static final RegistryKey<Biome> INDEV_WOODS_KEY = OldBiomes.register(ModernBeta.createId("indev_woods"));
    public static final RegistryKey<Biome> INDEV_SNOWY_KEY = OldBiomes.register(ModernBeta.createId("indev_snowy"));

    public static void registerBiomes() {
        OldBiomes.register(INDEV_NORMAL_KEY, IndevNormal.BIOME);
        OldBiomes.register(INDEV_HELL_KEY, IndevHell.BIOME);
        OldBiomes.register(INDEV_PARADISE_KEY, IndevParadise.BIOME);
        OldBiomes.register(INDEV_WOODS_KEY, IndevWoods.BIOME);
        OldBiomes.register(INDEV_SNOWY_KEY, IndevSnowy.BIOME);
    }
}
