package com.bespectacled.modernbeta.world.biome.indev;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomes;

import net.minecraft.util.Identifier;

public class IndevBiomes {
    public static final Identifier INDEV_NORMAL_ID = ModernBeta.createId("indev_normal");
    public static final Identifier INDEV_HELL_ID = ModernBeta.createId("indev_hell");
    public static final Identifier INDEV_PARADISE_ID = ModernBeta.createId("indev_paradise");
    public static final Identifier INDEV_WOODS_ID = ModernBeta.createId("indev_woods");
    public static final Identifier INDEV_SNOWY_ID = ModernBeta.createId("indev_snowy");

    public static void registerBiomes() {
        OldBiomes.register(INDEV_NORMAL_ID, IndevNormal.BIOME);
        OldBiomes.register(INDEV_HELL_ID, IndevHell.BIOME);
        OldBiomes.register(INDEV_PARADISE_ID, IndevParadise.BIOME);
        OldBiomes.register(INDEV_WOODS_ID, IndevWoods.BIOME);
        OldBiomes.register(INDEV_SNOWY_ID, IndevSnowy.BIOME);
    }
}
