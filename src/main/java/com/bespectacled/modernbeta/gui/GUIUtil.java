package com.bespectacled.modernbeta.gui;

import java.util.Iterator;

import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class GUIUtil {
    public static final Text TEXT_BIOME_TYPE = new TranslatableText("createWorld.customize.type.biomeType");
    
    public static final Text TEXT_ICE_DESERT = new TranslatableText("createWorld.customize.type.ice_desert");
    public static final Text TEXT_SKY = new TranslatableText("createWorld.customize.type.sky");
    
    public static final Text TEXT_CLASSIC = new TranslatableText("createWorld.customize.type.classic");
    public static final Text TEXT_WINTER = new TranslatableText("createWorld.customize.type.winter");
    public static final Text TEXT_PLUS = new TranslatableText("createWorld.customize.type.plus");
    public static final Text TEXT_VANILLA = new TranslatableText("createWorld.customize.type.vanilla");
    public static final Text TEXT_BETA = new TranslatableText("createWorld.customize.type.beta");
    public static final Text TEXT_RELEASE = new TranslatableText("createWorld.customize.type.release");
    
    public static final Text TEXT_NORMAL = new TranslatableText("createWorld.customize.indev.theme.normal");
    public static final Text TEXT_HELL = new TranslatableText("createWorld.customize.indev.theme.hell");
    public static final Text TEXT_PARADISE = new TranslatableText("createWorld.customize.indev.theme.paradise");
    public static final Text TEXT_WOODS = new TranslatableText("createWorld.customize.indev.theme.woods");
    public static final Text TEXT_SNOWY = new TranslatableText("createWorld.customize.indev.theme.snowy");
    
    public static final Text TEXT_ISLAND = new TranslatableText("createWorld.customize.indev.type.island");
    public static final Text TEXT_FLOATING = new TranslatableText("createWorld.customize.indev.type.floating");
    public static final Text TEXT_INLAND = new TranslatableText("createWorld.customize.indev.type.inland");
    
    public static final Text TEXT_UNKNOWN = new TranslatableText("createworld.customize.unknown");
    
    public static BiomeType iterateToBiomeType(BiomeType typeToGet, Iterator<BiomeType> typeIterator) {
        BiomeType type = typeToGet;
        
        while (typeIterator.hasNext() && (type = typeIterator.next()) != typeToGet);
        
        return type;
    }
    
    public static Text getBiomeTypeText(BiomeType type) {
        Text typeText;
        
        switch(type) {
            case BETA:
                typeText = GUIUtil.TEXT_BETA;
                break;
            case SKY:
                typeText = GUIUtil.TEXT_SKY;
                break;
            case CLASSIC:
                typeText = GUIUtil.TEXT_CLASSIC;
                break;
            case WINTER:
                typeText = GUIUtil.TEXT_WINTER;
                break;
            case PLUS:
                typeText = GUIUtil.TEXT_PLUS;
                break;
            case VANILLA:
                typeText = GUIUtil.TEXT_VANILLA;
                break;
            default:
                typeText = GUIUtil.TEXT_UNKNOWN;
        }   
        
        return typeText;
    }
    
}
