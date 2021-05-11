package com.bespectacled.modernbeta.compat;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.loader.api.FabricLoader;

public class Compat {
    public static void setupCompat() {
        try {
            if (FabricLoader.getInstance().isModLoaded("techreborn")) CompatTechReborn.addCompat();
            
        } catch (Exception e) {
            ModernBeta.log(Level.ERROR, "Something went wrong when attempting to add mod compatibility!");
            e.printStackTrace();
        }
    }
    
}