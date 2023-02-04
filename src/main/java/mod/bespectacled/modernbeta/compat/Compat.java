package mod.bespectacled.modernbeta.compat;

import org.apache.logging.log4j.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.loader.api.FabricLoader;

public class Compat {
    public static void setupCompat() {
        try {
            
        } catch (Exception e) {
            ModernBeta.log(Level.ERROR, "Something went wrong when attempting to add mod compatibility!");
            e.printStackTrace();
        }
    }
    
    public static boolean isLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}