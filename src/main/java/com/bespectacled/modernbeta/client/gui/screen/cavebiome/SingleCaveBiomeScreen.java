package com.bespectacled.modernbeta.client.gui.screen.cavebiome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleCaveBiomeScreen extends SettingsScreen {
    private static final String USE_NOISE_DISPLAY_STRING = "createWorld.customize.caveBiome.useNoise";
    private static final String VERTICAL_SCALE_DISPLAY_STRING = "createWorld.customize.caveBiome.verticalNoiseScale";
    private static final String HORIZONTAL_SCALE_DISPLAY_STRING = "createWorld.customize.caveBiome.horizontalNoiseScale";
    
    private static final String USE_NOISE_TOOLTIP = "createWorld.customize.caveBiome.useNoise.tooltip";
    
    private Identifier biomeId;
    
    private SingleCaveBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings) {
        super(parent, worldSetting, consumer, biomeSettings);
        
        this.biomeId = new Identifier(NbtUtil.readStringOrThrow(NbtTags.SINGLE_BIOME, biomeSettings.getNbt()));
    }
    
    public static SingleCaveBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new SingleCaveBiomeScreen(
            parent, 
            worldSetting, 
            settings -> parent.getWorldSettings().putCompound(worldSetting, settings.getNbt()),
            new Settings(parent.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        boolean useNoise = NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.USE_NOISE));
       
        ActionOptionWrapper singleBiomeOption = new ActionOptionWrapper(
            "createWorld.customize.biomeType.biome", // Key
            GuiUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getSetting(NbtTags.SINGLE_BIOME))),
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    this.settings.putElement(NbtTags.SINGLE_BIOME, NbtString.of(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString()));
                    this.biomeId = this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome);
                }, 
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeId)  
            ))
        );
        
        BooleanCyclingOptionWrapper useNoiseOption = new BooleanCyclingOptionWrapper(
            USE_NOISE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.USE_NOISE)),
            value -> {
                this.putSetting(NbtTags.USE_NOISE, NbtByte.of(value));
                
                this.client.setScreen(
                    new SingleCaveBiomeScreen(
                        (WorldScreen)this.parent,
                        this.worldSetting,
                        this.consumer,
                        this.settings
                    )
                );
            },
            this.client.textRenderer.wrapLines(new TranslatableText(USE_NOISE_TOOLTIP), 200)
        );
        
        DoubleOptionWrapper<Integer> verticalScaleOption = new DoubleOptionWrapper<>(
            VERTICAL_SCALE_DISPLAY_STRING,
            1D, 64D, 1f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.VERTICAL_NOISE_SCALE)),
            value -> this.putSetting(NbtTags.VERTICAL_NOISE_SCALE, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> horizontalScaleOption = new DoubleOptionWrapper<>(
            HORIZONTAL_SCALE_DISPLAY_STRING,
            1D, 64D, 1f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.HORIZONTAL_NOISE_SCALE)),
            value -> this.putSetting(NbtTags.HORIZONTAL_NOISE_SCALE, NbtInt.of(value.intValue()))
        );
        
        this.addOption(singleBiomeOption);
        this.addOption(useNoiseOption);
        
        this.addOption(verticalScaleOption, useNoise);
        this.addOption(horizontalScaleOption, useNoise);
    }
}
