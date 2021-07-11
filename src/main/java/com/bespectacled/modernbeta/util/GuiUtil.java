package com.bespectacled.modernbeta.util;

import net.minecraft.util.Identifier;

public class GuiUtil {
    public static String createTranslatableBiomeStringFromId(String biomeId) {
        return createTranslatableBiomeStringFromId(new Identifier(biomeId));
    }
    
    public static String createTranslatableBiomeStringFromId(Identifier biomeId) {
        return "biome." + biomeId.getNamespace() + "." + biomeId.getPath();
    }
}
