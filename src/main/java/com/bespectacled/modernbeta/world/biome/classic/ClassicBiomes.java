package com.bespectacled.modernbeta.world.biome.classic;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;

import net.minecraft.util.Identifier;

public class ClassicBiomes {
    public static final Identifier ALPHA_ID = ModernBeta.createId("alpha");
    public static final Identifier ALPHA_WINTER_ID = ModernBeta.createId("alpha_winter");
    
    public static final Identifier INFDEV_415_ID = ModernBeta.createId("infdev_415");
    public static final Identifier INFDEV_415_WINTER_ID = ModernBeta.createId("infdev_415_winter");
    
    public static final Identifier INFDEV_227_ID = ModernBeta.createId("infdev_227");
    public static final Identifier INFDEV_227_WINTER_ID = ModernBeta.createId("infdev_227_winter");
    
    public static void registerAlphaBiomes() {
        OldBiomes.register(ALPHA_ID, Alpha.BIOME);
        OldBiomes.register(ALPHA_WINTER_ID, AlphaWinter.BIOME);
    }
    
    public static void registerInfdev415Biomes() {
        OldBiomes.register(INFDEV_415_ID, Infdev415.BIOME);
        OldBiomes.register(INFDEV_415_WINTER_ID, Infdev415Winter.BIOME);
    }
    
    public static void registerInfdev227Biomes() {
        OldBiomes.register(INFDEV_227_ID, Infdev227.BIOME);
        OldBiomes.register(INFDEV_227_WINTER_ID, Infdev227Winter.BIOME);
    }
}
