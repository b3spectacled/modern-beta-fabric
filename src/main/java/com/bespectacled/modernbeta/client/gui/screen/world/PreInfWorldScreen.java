package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class PreInfWorldScreen extends InfWorldScreen {
    private static final String LEVEL_WIDTH_DISPLAY_STRING = "createWorld.customize.indev.levelWidth";
    private static final String LEVEL_LENGTH_DISPLAY_STRING = "createWorld.customize.indev.levelLength";
    private static final String LEVEL_HEIGHT_DISPLAY_STRING = "createWorld.customize.indev.levelHeight";
    private static final String CAVE_RADIUS_DISPLAY_STRING = "createWorld.customize.indev.caveRadius";
    
    private static final String LEVEL_HEIGHT_TOOLTIP = "createWorld.customize.indev.levelHeight.tooltip";
    private static final String CAVE_RADIUS_TOOLTIP = "createWorld.customize.indev.caveRadius.tooltip";

    protected PreInfWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    public static PreInfWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new PreInfWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putCompound(worldSetting, settings.getNbt()),
            new Settings(worldScreen.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        Supplier<ChunkGeneratorSettings> chunkGenSettings = () -> this.registryManager
            .<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY)
            .get(ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.name));
            
        int topY = chunkGenSettings.get().getGenerationShapeConfig().height() + chunkGenSettings.get().getGenerationShapeConfig().minimumY();
        
        DoubleOptionWrapper<Integer> levelWidth = new DoubleOptionWrapper<>(
            LEVEL_WIDTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_WIDTH)),
            value -> this.putSetting(NbtTags.LEVEL_WIDTH, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelLength = new DoubleOptionWrapper<>(
            LEVEL_LENGTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_LENGTH)),
            value -> this.putSetting(NbtTags.LEVEL_LENGTH, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelHeight = new DoubleOptionWrapper<>(
            LEVEL_HEIGHT_DISPLAY_STRING, 
            "blocks",
            64D, (double)topY, 64F,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_HEIGHT)),
            value -> this.putSetting(NbtTags.LEVEL_HEIGHT, NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(LEVEL_HEIGHT_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> caveRadius = new DoubleOptionWrapper<>(
            CAVE_RADIUS_DISPLAY_STRING,
            "",
            1D, 3D, 0.1f,
            () -> NbtUtil.toFloatOrThrow(this.getSetting(NbtTags.LEVEL_CAVE_RADIUS)),
            value -> this.putSetting(NbtTags.LEVEL_CAVE_RADIUS, NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(CAVE_RADIUS_TOOLTIP), 200)
        );
        
        this.addOption(levelWidth);
        this.addOption(levelLength);
        this.addOption(levelHeight);
        this.addOption(caveRadius);
    }
}
