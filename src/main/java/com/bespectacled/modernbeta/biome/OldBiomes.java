package com.bespectacled.modernbeta.biome;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.classic.ClassicBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevBiomes;

public class OldBiomes {
    public static void register() {
        BetaBiomes.registerBiomes();
        ClassicBiomes.registerAlphaBiomes();
        ClassicBiomes.registerInfdevBiomes();
        ClassicBiomes.registerInfdevOldBiomes();
        IndevBiomes.registerBiomes();
    }
}
