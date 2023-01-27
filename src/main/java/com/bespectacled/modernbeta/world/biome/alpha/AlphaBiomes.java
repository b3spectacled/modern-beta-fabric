package com.bespectacled.modernbeta.world.biome.alpha;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.ModernBetaBiomes;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class AlphaBiomes {
    protected static final boolean ADD_LAKES_ALPHA = false;
    protected static final boolean ADD_SPRINGS_ALPHA = true;
    
    protected static final boolean ADD_ALTERNATE_STONES_ALPHA = false;
    protected static final boolean ADD_NEW_MINEABLES_ALPHA = false;
    
    public static final RegistryKey<Biome> ALPHA_KEY = ModernBetaBiomes.register(ModernBeta.createId("alpha"));
    public static final RegistryKey<Biome> ALPHA_WINTER_KEY = ModernBetaBiomes.register(ModernBeta.createId("alpha_winter"));
    
    public static void registerBiomes() {
        ModernBetaBiomes.register(ALPHA_KEY, Alpha.BIOME);
        ModernBetaBiomes.register(ALPHA_WINTER_KEY, AlphaWinter.BIOME);
    }
}
