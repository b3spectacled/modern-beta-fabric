package mod.bespectacled.modernbeta.settings;

import org.slf4j.event.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.nbt.NbtCompound;

public interface ModernBetaSettings {
    NbtCompound toCompound();
    
    public static void datafix(String tag, NbtCompound compound, Runnable datafixer) {
        if (compound.contains(tag)) {
            ModernBeta.log(Level.INFO, String.format("Found old tag '%s', datafixing..", tag));
            datafixer.run();
        }
    }
}
