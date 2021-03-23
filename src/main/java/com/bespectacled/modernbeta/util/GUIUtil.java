package com.bespectacled.modernbeta.util;

import net.minecraft.util.Identifier;

public class GUIUtil {
    public static String createTranslatableBiomeString(Identifier biomeId) {
        return "biome." + biomeId.getNamespace() + "." + biomeId.getPath();
    }
}
