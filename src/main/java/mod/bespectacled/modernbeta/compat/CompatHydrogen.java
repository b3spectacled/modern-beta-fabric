package mod.bespectacled.modernbeta.compat;

import org.apache.logging.log4j.Level;

import mod.bespectacled.modernbeta.ModernBeta;

public class CompatHydrogen {
    public static void addCompat() {
        ModernBeta.log(Level.WARN, "Hydrogen detected! 'Assign Ocean Biomes' option is disabled.");   
    }
}
