package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.util.GUITags;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import com.bespectacled.modernbeta.world.gen.provider.indev.IndevType;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class IndevWorldScreen extends SettingsScreen {
    protected IndevWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }

    public static IndevWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new IndevWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().replace(worldSetting, settings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Generator settings governs generation height.
        // Dimension settings governs logical height.
        // Both need to be checked.
        
        ChunkGeneratorSettings generatorSettings = this.registryManager
            .<ChunkGeneratorSettings>get(Registry.CHUNK_GENERATOR_SETTINGS_KEY)
            .get(ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.name));
        
        DimensionType dimensionType = this.registryManager
            .<DimensionType>get(Registry.DIMENSION_TYPE_KEY)
            .get(DimensionType.OVERWORLD_ID);
            
        int genTopY = generatorSettings.generationShapeConfig().height() + generatorSettings.generationShapeConfig().minimumY();
        int dimTopY = dimensionType.getHeight() + dimensionType.getMinimumY();
        
        int topY = Math.min(genTopY, dimTopY);
        
        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateMonuments = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );

        CyclingOptionWrapper<IndevTheme> levelTheme = new CyclingOptionWrapper<>(
            GUITags.LEVEL_THEME_DISPLAY_STRING,
            IndevTheme.values(),
            () -> IndevTheme.fromName(NbtUtil.toStringOrThrow(this.getSetting(NbtTags.LEVEL_THEME))),
            value -> this.putSetting(NbtTags.LEVEL_THEME, NbtString.of(value.getName())),
            value -> value.getColor(),
            value -> this.client.textRenderer.wrapLines(new TranslatableText(value.getDescription()).formatted(value.getColor()), 250)
        );
        
        CyclingOptionWrapper<IndevType> levelType = new CyclingOptionWrapper<>(
            GUITags.LEVEL_TYPE_DISPLAY_STRING, 
            IndevType.values(), 
            () -> IndevType.fromName(NbtUtil.toStringOrThrow(this.getSetting(NbtTags.LEVEL_TYPE))), 
            value -> this.putSetting(NbtTags.LEVEL_TYPE, NbtString.of(value.getName()))
        );
        
        DoubleOptionWrapper<Integer> levelWidth = new DoubleOptionWrapper<>(
            GUITags.LEVEL_WIDTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_WIDTH)),
            value -> this.putSetting(NbtTags.LEVEL_WIDTH, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelLength = new DoubleOptionWrapper<>(
            GUITags.LEVEL_LENGTH_DISPLAY_STRING,
            "blocks",
            128D, 1024D, 128f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_LENGTH)),
            value -> this.putSetting(NbtTags.LEVEL_LENGTH, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> levelHeight = new DoubleOptionWrapper<>(
            GUITags.LEVEL_HEIGHT_DISPLAY_STRING, 
            "blocks",
            64D, (double)topY, 64F,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.LEVEL_HEIGHT)),
            value -> this.putSetting(NbtTags.LEVEL_HEIGHT, NbtInt.of(value.intValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(GUITags.LEVEL_HEIGHT_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Float> caveRadius = new DoubleOptionWrapper<>(
            GUITags.CAVE_RADIUS_DISPLAY_STRING,
            "",
            1D, 3D, 0.1f,
            () -> NbtUtil.toFloatOrThrow(this.getSetting(NbtTags.LEVEL_CAVE_RADIUS)),
            value -> this.putSetting(NbtTags.LEVEL_CAVE_RADIUS, NbtFloat.of(value.floatValue())),
            this.client.textRenderer.wrapLines(new TranslatableText(GUITags.CAVE_RADIUS_TOOLTIP), 200)
        );
        
        this.addOption(generateOceanShrines);
        this.addOption(generateMonuments);

        this.addOption(levelTheme);
        this.addOption(levelType);
        this.addOption(levelWidth);
        this.addOption(levelLength);
        this.addOption(levelHeight);
        this.addOption(caveRadius);
    }
}
