package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.config.ModernBetaConfigBiome.ClimateMapping;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtCompoundBuilder;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping.ClimateType;

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
    
    private Map<String, ClimateMapping> climateMap;
    
    private ClimateBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }
    
    public static ClimateBiomeScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new ClimateBiomeScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().replace(worldSetting, settings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Create Beta biome map from existing biome settings
        this.climateMap = this.createMap();
        
        this.climateMap.entrySet().forEach(
            e -> this.addBiomePointEntry(e.getKey(), e.getValue())
        );
    }
    
    private Map<String, ClimateMapping> createMap() {
        Map<String, ClimateMapping> climateMap = new LinkedHashMap<>();
        
        NbtCompound biomes = NbtUtil.toCompoundOrThrow(this.settings.get(NbtTags.BIOMES));
        for (String key : biomes.getKeys()) {
            NbtCompound biome = NbtUtil.readCompoundOrThrow(key, biomes);
            
            ClimateMapping mapping = new ClimateMapping(
                NbtUtil.readStringOrThrow(NbtTags.BIOME, biome),
                NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, biome),
                NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, biome)
            );
            
            climateMap.put(key, mapping);
        }
        
        return climateMap;
    }
    
    private void addBiomePointEntry(String key, ClimateMapping mapping) {
        TextOptionWrapper header = new TextOptionWrapper(GuiUtil.createTranslatableBiomeStringFromId(ModernBeta.createId(key)));
        header.formatting(Formatting.YELLOW).formatting(Formatting.BOLD);
        
        TextOptionWrapper landBiomeText = new TextOptionWrapper(LAND_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper oceanBiomeText = new TextOptionWrapper(OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        TextOptionWrapper deepOceanBiomeText = new TextOptionWrapper(DEEP_OCEAN_BIOME_DISPLAY_STRING).formatting(Formatting.GRAY);
        
        this.addOption(header);
        this.addDualOption(landBiomeText, this.createBiomeSelectionScreen(key, mapping, ClimateType.LAND));
        this.addDualOption(oceanBiomeText, this.createBiomeSelectionScreen(key, mapping, ClimateType.OCEAN));
        this.addDualOption(deepOceanBiomeText, this.createBiomeSelectionScreen(key, mapping, ClimateType.DEEP_OCEAN));
    }
    
    private ActionOptionWrapper createBiomeSelectionScreen(String key, ClimateMapping mapping, ClimateType type) {
        Identifier biomeId = new Identifier(mapping.biomeByClimateType(type));
        
        return new ActionOptionWrapper(
            GuiUtil.createTranslatableBiomeStringFromId(biomeId),
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    mapping.setBiomeByClimateType(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString(), type);
                    
                    this.updateMap();
                },
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(biomeId)
            ))
        );
    }
    
    private void updateMap() {
        NbtCompoundBuilder builder = new NbtCompoundBuilder();
        this.climateMap.entrySet().forEach(e -> builder.putCompound(e.getKey(), e.getValue().toCompound()));
        
        this.settings.put(NbtTags.BIOMES, builder.build());
    }
}