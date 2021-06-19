package com.bespectacled.modernbeta.gui.screen.biome;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.gui.option.ActionOption;
import com.bespectacled.modernbeta.gui.option.TextOption;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMapCustomizable;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeScreen extends BiomeScreen {
    private final Map<String, Identifier> biomeSettingsMap;
    
    private BetaBiomeScreen(WorldScreen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super(parent, worldSettings, consumer);
        
        this.biomeSettingsMap = new BetaClimateMapCustomizable(this.worldSettings.getSettings(WorldSetting.BIOME)).getMap();
    }
    
    public static BetaBiomeScreen create(WorldScreen worldScreen) {
        return new BetaBiomeScreen(
            worldScreen,
            worldScreen.getWorldSettings(),
            worldSettings -> {}
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        for (Entry<String, Identifier> e : this.biomeSettingsMap.entrySet()) {
            this.addBiomeButtonEntry(e.getKey(), GUIUtil.createTranslatableBiomeStringFromId(e.getValue()));
        }
    }
    
    private void addBiomeButtonEntry(String key, String biomeText) {
        this.buttonList.addOptionEntry(
            new TextOption(GUIUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(key))),
            new ActionOption(
                GUIUtil.createTranslatableBiomeStringFromId(this.biomeSettingsMap.get(key)),
                "",
                buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                  this,
                  this.registryManager,
                  biome -> {
                      //this.biomeProviderSettings.putString(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                      this.biomeSettingsMap.put(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                  }, 
                  this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeSettingsMap.get(key))  
                ))
            )
        );
    }
}
