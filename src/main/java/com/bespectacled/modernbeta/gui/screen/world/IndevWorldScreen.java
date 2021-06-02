package com.bespectacled.modernbeta.gui.screen.world;

import java.util.Arrays;
import java.util.function.Consumer;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.gui.CyclingOptionWrapper;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class IndevWorldScreen extends WorldScreen {
    public IndevWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
        
        // Set default single biome per level theme
        String levelTheme = NBTUtil.readString("levelTheme", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelTheme);
        this.setDefaultSingleBiome(IndevTheme.fromName(levelTheme).getDefaultBiome().toString());
    }
    
    @Override
    protected void init() {
        super.init();
        
        CyclingOptionWrapper<IndevTheme> levelTheme = new CyclingOptionWrapper<>(
            "createWorld.customize.indev.levelTheme", 
            Arrays.asList(IndevTheme.values()),
            IndevTheme.fromName(NBTUtil.readString("levelTheme", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelTheme)),
            value -> {
                this.worldSettings.putSetting(WorldSetting.CHUNK, "levelTheme", StringTag.of(value.getName()));
                
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            }
        );
    
        CyclingOptionWrapper<IndevType> levelType = new CyclingOptionWrapper<>(
            "createWorld.customize.indev.levelType", 
            Arrays.asList(IndevType.values()),
            IndevType.fromName(NBTUtil.readString("levelType", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelType)), 
            value -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelType", StringTag.of(value.getName()))
        );
    
        DoubleOption levelWidth = 
            new DoubleOption(
                "createWorld.customize.indev.widthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> (double)NBTUtil.readInt("levelWidth", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelWidth), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelWidth", IntTag.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelWidth"), 
                            Text.of(String.valueOf(NBTUtil.readInt("levelWidth", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelWidth))) 
                    });
                }
            );
        
        DoubleOption levelLength =
            new DoubleOption(
                "createWorld.customize.indev.lengthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> (double)NBTUtil.readInt("levelLength", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelLength), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelLength", IntTag.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelLength"), 
                            Text.of(String.valueOf(NBTUtil.readInt("levelLength", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelLength))) 
                    });
                }
            );
        
        DoubleOption levelHeight =
            new DoubleOption(
                "createWorld.customize.indev.heightSlider", 
                64D, 256, 64F,
                (gameOptions) -> (double)NBTUtil.readInt("levelHeight", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelHeight), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelHeight", IntTag.of(value.intValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.levelHeight"), 
                            Text.of(String.valueOf(NBTUtil.readInt("levelHeight", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelHeight))) 
                    });
                }
            );
        
        DoubleOption caveRadius =
            new DoubleOption(
                "createWorld.customize.indev.caveRadiusSlider", 
                1D, 3D, 0.1f,
                (gameOptions) -> (double)NBTUtil.readFloat("caveRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevCaveRadius), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "caveRadius", FloatTag.of(value.floatValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.caveRadius"), 
                            Text.of(String.format("%.01f", NBTUtil.readFloat("caveRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevCaveRadius)))  
                    });
                }
            );
        
        this.buttonList.addSingleOptionEntry(levelTheme.create());
        this.buttonList.addSingleOptionEntry(levelType.create());
        this.buttonList.addSingleOptionEntry(levelWidth);
        this.buttonList.addSingleOptionEntry(levelLength);        
        this.buttonList.addSingleOptionEntry(levelHeight);
        this.buttonList.addSingleOptionEntry(caveRadius);
    }
}
