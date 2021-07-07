package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.world.biome.beta.BetaClimateMapCustomizable;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeScreen extends BiomeScreen {
    private final Map<String, Identifier> biomeSettingsMap;
    
    private BetaBiomeScreen(WorldScreen parent, Consumer<Settings> consumer) {
        super(parent, consumer);
        
        this.biomeSettingsMap = new BetaClimateMapCustomizable(this.biomeSettings.getNbt()).getMap();
    }
    
    public static BetaBiomeScreen create(WorldScreen worldScreen) {
        return new BetaBiomeScreen(
            worldScreen,
            settings -> worldScreen.getWorldSettings().putChanges(WorldSetting.BIOME, settings.getNbt())
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
        TextOptionWrapper text = new TextOptionWrapper(GUIUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(key)), Formatting.GRAY);
        
        ActionOptionWrapper singleBiomeScreen = new ActionOptionWrapper(
            GUIUtil.createTranslatableBiomeStringFromId(this.biomeSettingsMap.get(key)), 
            "",
            buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
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
