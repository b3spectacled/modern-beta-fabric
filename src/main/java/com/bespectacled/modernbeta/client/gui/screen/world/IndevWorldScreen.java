package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class IndevWorldScreen extends InfWorldScreen {
    private static final String LEVEL_THEME_DISPLAY_STRING = "createWorld.customize.indev.levelTheme";
    private static final String LEVEL_TYPE_DISPLAY_STRING = "createWorld.customize.indev.levelType";
    private static final String LEVEL_WIDTH_DISPLAY_STRING = "createWorld.customize.indev.levelWidth";
    private static final String LEVEL_LENGTH_DISPLAY_STRING = "createWorld.customize.indev.levelLength";
    private static final String LEVEL_HEIGHT_DISPLAY_STRING = "createWorld.customize.indev.levelHeight";
    private static final String CAVE_RADIUS_DISPLAY_STRING = "createWorld.customize.indev.caveRadius";
    
    private static final String LEVEL_HEIGHT_TOOLTIP = "createWorld.customize.indev.levelHeight.tooltip";
    private static final String CAVE_RADIUS_TOOLTIP = "createWorld.customize.indev.caveRadius.tooltip";
    
    public IndevWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
        
        // Set default single biome per level theme
        String levelTheme = NBTUtil.toString(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelTheme"), ModernBeta.GEN_CONFIG.indevLevelTheme);
        this.setDefaultSingleBiome(IndevTheme.fromName(levelTheme).getDefaultBiome().toString());
    }
    
    @Override
    protected void init() {
        super.init();
        
        Supplier<ChunkGeneratorSettings> chunkGenSettings = () -> this.registryManager
            .<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY)
            .get(ModernBeta.createId(BuiltInTypes.Chunk.INDEV.name));
            
        int topY = chunkGenSettings.get().getGenerationShapeConfig().getHeight() + chunkGenSettings.get().getGenerationShapeConfig().getMinimumY();
        
        CyclingOptionWrapper<IndevTheme> levelTheme = new CyclingOptionWrapper<>(
            LEVEL_THEME_DISPLAY_STRING,
            IndevTheme.values(),
            () -> IndevTheme.fromName(NBTUtil.toString(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelTheme"), ModernBeta.GEN_CONFIG.indevLevelTheme)),
            value -> {
                this.worldSettings.putChange(WorldSetting.CHUNK, "levelTheme", NbtString.of(value.getName()));
                
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        (CreateWorldScreen)this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            },
            value -> value.getColor()
        );
        
        CyclingOptionWrapper<IndevType> levelType = new CyclingOptionWrapper<>(
            LEVEL_TYPE_DISPLAY_STRING, 
            IndevType.values(), 
            () -> IndevType.fromName(NBTUtil.toString(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelType"), ModernBeta.GEN_CONFIG.indevLevelType)), 
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelType", NbtString.of(value.getName()))
        );
        
        DoubleOptionWrapper<Integer> levelWidth = new DoubleOptionWrapper<>(
            LEVEL_WIDTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelWidth"), ModernBeta.GEN_CONFIG.indevLevelWidth),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelWidth", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelLength = new DoubleOptionWrapper<>(
            LEVEL_LENGTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelLength"), ModernBeta.GEN_CONFIG.indevLevelLength),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelLength", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelHeight = new DoubleOptionWrapper<>(
            LEVEL_HEIGHT_DISPLAY_STRING, 
            "blocks",
            64D, (double)topY, 64F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelHeight"), ModernBeta.GEN_CONFIG.indevLevelHeight),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelHeight", NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(LEVEL_HEIGHT_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> caveRadius = new DoubleOptionWrapper<>(
            CAVE_RADIUS_DISPLAY_STRING,
            "",
            1D, 3D, 0.1f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "caveRadius"), ModernBeta.GEN_CONFIG.indevCaveRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "caveRadius", NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CAVE_RADIUS_TOOLTIP), 200)
        );
        
        this.addOption(levelTheme);
        this.addOption(levelType);
        this.addOption(levelWidth);
        this.addOption(levelLength);
        this.addOption(levelHeight);
        this.addOption(caveRadius);
    }
}
