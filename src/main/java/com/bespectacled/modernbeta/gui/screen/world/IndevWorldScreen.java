package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.api.gui.wrapper.DoubleOptionWrapper;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class IndevWorldScreen extends InfWorldScreen {
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
            "createWorld.customize.indev.levelTheme",
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
            "createWorld.customize.indev.levelType", 
            IndevType.values(), 
            () -> IndevType.fromName(NBTUtil.toString(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelType"), ModernBeta.GEN_CONFIG.indevLevelType)), 
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelType", NbtString.of(value.getName()))
        );
        
        DoubleOptionWrapper<Integer> levelWidth = new DoubleOptionWrapper<>(
            "createWorld.customize.indev.levelWidth",
            "blocks",
            128D, 1024D, 128f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelWidth"), ModernBeta.GEN_CONFIG.indevLevelWidth),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelWidth", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelLength = new DoubleOptionWrapper<>(
            "createWorld.customize.indev.levelLength",
            "blocks",
            128D, 1024D, 128f,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelLength"), ModernBeta.GEN_CONFIG.indevLevelLength),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelLength", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelHeight = new DoubleOptionWrapper<>(
            "createWorld.customize.indev.levelHeight", 
            "blocks",
            64D, (double)topY, 64F,
            () -> NBTUtil.toInt(this.worldSettings.getSetting(WorldSetting.CHUNK, "levelHeight"), ModernBeta.GEN_CONFIG.indevLevelHeight),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "levelHeight", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Float> caveRadius = new DoubleOptionWrapper<>(
            "createWorld.customize.indev.caveRadius",
            "",
            1D, 3D, 0.1f,
            () -> NBTUtil.toFloat(this.worldSettings.getSetting(WorldSetting.CHUNK, "caveRadius"), ModernBeta.GEN_CONFIG.indevCaveRadius),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "caveRadius", NbtFloat.of(value.floatValue()))
        );
        
        this.addOption(levelTheme);
        this.addOption(levelType);
        this.addOption(levelWidth);
        this.addOption(levelLength);
        this.addOption(levelHeight);
        this.addOption(caveRadius);
    }
}
