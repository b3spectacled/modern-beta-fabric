package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateType;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class ClimateBiomeScreen extends SettingsScreen {
    private static final String LAND_BIOME_DISPLAY_STRING = "createWorld.customize.climateType.land";
    private static final String OCEAN_BIOME_DISPLAY_STRING = "createWorld.customize.climateType.ocean";
    
    private final Map<String, Identifier> landBiomeMap;
    private final Map<String, Identifier> oceanBiomeMap;
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings) {
        super(parent, worldSetting, consumer, biomeSettings);
        
        // Create Beta biome map from existing biome settings
        BetaClimateMap climateMap = new BetaClimateMap(this.settings.getNbt());
        
        this.landBiomeMap = climateMap.getMap(ClimateType.LAND);
        this.oceanBiomeMap = climateMap.getMap(ClimateType.OCEAN);
    }
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(WorldSetting.BIOME)));
    }
    
    public static ClimateBiomeScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new ClimateBiomeScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putChanges(WorldSetting.BIOME, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        TextOptionWrapper landBiomeText = new TextOptionWrapper(LAND_BIOME_DISPLAY_STRING, Formatting.YELLOW);
        TextOptionWrapper oceanBiomeText = new TextOptionWrapper(OCEAN_BIOME_DISPLAY_STRING, Formatting.YELLOW);
        
        this.addOption(landBiomeText);
        this.landBiomeMap.entrySet().forEach(
            e -> this.addBiomeButtonEntry(e.getKey(), GuiUtil.createTranslatableBiomeStringFromId(e.getValue()), this.landBiomeMap)
        );
        
        this.addOption(oceanBiomeText);
        this.oceanBiomeMap.entrySet().forEach(
            e -> this.addBiomeButtonEntry(e.getKey(), GuiUtil.createTranslatableBiomeStringFromId(e.getValue()), this.oceanBiomeMap)
        );
    }
    
    private void addBiomeButtonEntry(String key, String biomeText, Map<String, Identifier> biomeMap) {
        TextOptionWrapper text = new TextOptionWrapper(GuiUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(key)), Formatting.GRAY);
        
        ActionOptionWrapper singleBiomeScreen = new ActionOptionWrapper(
            GuiUtil.createTranslatableBiomeStringFromId(biomeMap.get(key)), 
            "",
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    // Queue change
                    this.settings.putChange(key, NbtString.of(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString()));
                    
                    // Update map for display
                    biomeMap.put(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                }, 
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(biomeMap.get(key))  
            ))
        );
        
        this.addDualOption(text, singleBiomeScreen);
    }
}
