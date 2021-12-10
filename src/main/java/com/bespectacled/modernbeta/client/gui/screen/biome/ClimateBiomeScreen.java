package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.world.biome.provider.climate.BetaClimateMap;
import com.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping;

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
    
    private final Map<String, ClimateMapping> climateMap;
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
        
        // Create Beta biome map from existing biome settings
        this.climateMap = new BetaClimateMap(this.settings.getNbt()).getMap();
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
        
        this.climateMap.entrySet().forEach(
            e -> this.addBiomePointEntry(e.getKey(), e.getValue())
        );
    }
    
    private void addBiomePointEntry(String climateMappingKey, ClimateMapping climateMapping) {
        TextOptionWrapper header = new TextOptionWrapper(GuiUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(climateMappingKey)));
        header.formatting(Formatting.YELLOW).formatting(Formatting.BOLD);
        
        TextOptionWrapper landBiomeText = new TextOptionWrapper(LAND_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper oceanBiomeText = new TextOptionWrapper(OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper deepOceanBiomeText = new TextOptionWrapper(DEEP_OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        
        this.addOption(header);
        this.addDualOption(
            landBiomeText,
            this.createBiomeSelectionScreen(climateMappingKey, climateMapping.biome(), NbtTags.BIOME)
        );
        this.addDualOption(
            oceanBiomeText,
            this.createBiomeSelectionScreen(climateMappingKey, climateMapping.oceanBiome(), NbtTags.OCEAN_BIOME)
        );
        this.addDualOption(
            deepOceanBiomeText,
            this.createBiomeSelectionScreen(climateMappingKey, climateMapping.deepOceanBiome(), NbtTags.DEEP_OCEAN_BIOME)
        );
    }
    
    private ActionOptionWrapper createBiomeSelectionScreen(String climateMappingKey, Identifier biomeId, String climateTypeKey) {
        return new ActionOptionWrapper(
            GuiUtil.createTranslatableBiomeStringFromId(biomeId),
            "",
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    NbtCompound climateMapping = (NbtCompound) this.settings.getElement(climateMappingKey);
                    
                    // Queue change
                    climateMapping.putString(climateTypeKey, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                    
                    // Update map for display
                    this.climateMap.put(climateMappingKey, ClimateMapping.fromCompound(climateMapping));
                },
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(biomeId)
            ))  
        );
    }
}