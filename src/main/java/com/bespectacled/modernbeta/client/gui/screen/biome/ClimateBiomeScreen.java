package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.world.biome.BiomeClimatePoint;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class ClimateBiomeScreen extends OceanBiomeScreen {
    private static final String LAND_BIOME_DISPLAY_STRING = "createWorld.customize.biome.climateType.land";
    private static final String OCEAN_BIOME_DISPLAY_STRING = "createWorld.customize.biome.climateType.ocean";
    private static final String DEEP_OCEAN_BIOME_DISPLAY_STRING = "createWorld.customize.biome.climateType.deepOcean";
    
    private final Map<String, BiomeClimatePoint> biomeMap;
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
        
        // Create Beta biome map from existing biome settings
        this.biomeMap = new BetaClimateMap(this.settings.getNbt()).getMap();
    }
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(worldSetting)));
    }
    
    public static ClimateBiomeScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new ClimateBiomeScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putCompound(worldSetting, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.biomeMap.entrySet().forEach(
            e -> this.addBiomePointEntry(e.getKey(), e.getValue())
        );
    }
    
    private void addBiomePointEntry(String biomePointKey, BiomeClimatePoint biomePoint) {
        TextOptionWrapper header = new TextOptionWrapper(GuiUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(biomePointKey)));
        header.formatting(Formatting.YELLOW).formatting(Formatting.BOLD);
        
        TextOptionWrapper landBiomeText = new TextOptionWrapper(LAND_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper oceanBiomeText = new TextOptionWrapper(OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper deepOceanBiomeText = new TextOptionWrapper(DEEP_OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        
        this.addOption(header);
        this.addDualOption(landBiomeText, this.createBiomeSelectionScreen(biomePointKey, biomePoint.landBiome(), NbtTags.LAND_BIOME));
        this.addDualOption(oceanBiomeText, this.createBiomeSelectionScreen(biomePointKey, biomePoint.oceanBiome(), NbtTags.OCEAN_BIOME));
        this.addDualOption(deepOceanBiomeText, this.createBiomeSelectionScreen(biomePointKey, biomePoint.deepOceanBiome(), NbtTags.DEEP_OCEAN_BIOME));
    }
    
    private ActionOptionWrapper createBiomeSelectionScreen(String biomePointKey, String biomeId, String climateTypeKey) {
        return new ActionOptionWrapper(
            GuiUtil.createTranslatableBiomeStringFromId(biomeId),
            "",
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    // Queue change
                    ((NbtCompound)this.settings.getElement(biomePointKey)).putString(climateTypeKey, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                    
                    // Update map for display
                    this.biomeMap.put(biomePointKey, BiomeClimatePoint.fromCompound(((NbtCompound)this.settings.getElement(biomePointKey))));
                },
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(new Identifier(biomeId))
            ))  
        );
    }
}