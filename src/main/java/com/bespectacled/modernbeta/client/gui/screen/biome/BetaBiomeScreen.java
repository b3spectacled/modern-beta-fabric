package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeScreen extends BiomeScreen {
    private final ClimateType climateType;
    private final Map<String, Identifier> biomeSettingsMap;
    
    private BetaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings, ClimateType climateType) {
        super(parent, worldSetting, consumer, biomeSettings);
        
        // Create Beta biome map from existing biome settings
        this.climateType = climateType;
        this.biomeSettingsMap = new BetaClimateMap(this.biomeSettings.getNbt()).getMap(climateType);
    }
    
    private BetaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, ClimateType climateType) {
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(worldSetting)), climateType);
    }
    
    public static BetaBiomeScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new BetaBiomeScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putChanges(worldSetting, settings.getNbt()),
            ClimateType.LAND
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        CyclingOptionWrapper<ClimateType> climateTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.climateType",
            ClimateType.values(),
            () -> this.climateType,
            value -> {
                this.client.setScreen(
                    new BetaBiomeScreen(
                        (WorldScreen)this.parent,
                        this.worldSetting,
                        this.consumer,
                        this.biomeSettings,
                        value
                    )    
                );
            }
        );
        
        this.addOption(climateTypeOption);
        for (Entry<String, Identifier> e : this.biomeSettingsMap.entrySet()) {
            this.addBiomeButtonEntry(e.getKey(), GuiUtil.createTranslatableBiomeStringFromId(e.getValue()));
        }
    }
    
    private void addBiomeButtonEntry(String key, String biomeText) {
        TextOptionWrapper text = new TextOptionWrapper(GuiUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(key)), Formatting.GRAY);
        
        ActionOptionWrapper singleBiomeScreen = new ActionOptionWrapper(
            GuiUtil.createTranslatableBiomeStringFromId(this.biomeSettingsMap.get(key)), 
            "",
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    this.biomeSettings.putChange(key, NbtString.of(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString()));
                    this.biomeSettingsMap.put(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                }, 
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeSettingsMap.get(key))  
            ))
        );
        
        this.addDualOption(text, singleBiomeScreen);
    }
}
