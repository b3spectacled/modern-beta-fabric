package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

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
        
        Supplier<ChunkGeneratorSettings> chunkGenSettings = () -> this.registryManager
            .<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY)
            .get(ModernBeta.createId(BuiltInTypes.Chunk.INDEV.name));
            
        int topY = chunkGenSettings.get().getGenerationShapeConfig().getHeight() + chunkGenSettings.get().getGenerationShapeConfig().getMinimumY();
        
        CyclingOption<IndevTheme> levelTheme = 
            CyclingOption.create(
                "createWorld.customize.indev.levelTheme", 
                IndevTheme.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.levelTheme." + value.getName()), 
                (gameOptions) -> IndevTheme.fromName(NBTUtil.readString("levelTheme", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelTheme)), 
                (gameOptions, option, value) -> {
                    this.worldSettings.putSetting(WorldSetting.CHUNK, "levelTheme", NbtString.of(value.getName()));
                    
                    this.client.openScreen(
                        this.worldProvider.createWorldScreen(
                            this.parent,
                            this.worldSettings,
                            this.consumer
                    ));
                }
            );
    
        CyclingOption<IndevType> levelType = 
            CyclingOption.create(
                "createWorld.customize.indev.levelType", 
                IndevType.values(), 
                (value) -> new TranslatableText("createWorld.customize.indev.levelType." + value.getName()), 
                (gameOptions) -> IndevType.fromName(NBTUtil.readString("levelType", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelType)), 
                (gameOptions, option, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelType", NbtString.of(value.getName()))
            );
        
        DoubleOption levelWidth = 
            new DoubleOption(
                "createWorld.customize.indev.widthSlider", 
                128D, 1024D, 128f,
                (gameOptions) -> (double)NBTUtil.readInt("levelWidth", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelWidth), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelWidth", NbtInt.of(value.intValue())),
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
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelLength", NbtInt.of(value.intValue())),
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
                64D, (double)topY, 64F,
                (gameOptions) -> (double)NBTUtil.readInt("levelHeight", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevLevelHeight), // Getter
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "levelHeight", NbtInt.of(value.intValue())),
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
                (gameOptions, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "caveRadius", NbtFloat.of(value.floatValue())),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.indev.caveRadius"), 
                            Text.of(String.format("%.01f", NBTUtil.readFloat("caveRadius", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.indevCaveRadius)))  
                    });
                }
            );
        
        this.buttonList.addSingleOptionEntry(levelTheme);
        this.buttonList.addSingleOptionEntry(levelType);
        this.buttonList.addSingleOptionEntry(levelWidth);
        this.buttonList.addSingleOptionEntry(levelLength);        
        this.buttonList.addSingleOptionEntry(levelHeight);
        this.buttonList.addSingleOptionEntry(caveRadius);
    }
}
