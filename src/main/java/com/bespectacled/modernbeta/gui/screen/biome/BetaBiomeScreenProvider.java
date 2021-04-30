package com.bespectacled.modernbeta.gui.screen.biome;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.gui.BiomeScreenProvider;
import com.bespectacled.modernbeta.api.gui.WorldScreenProvider;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.GUIUtil;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaBiomeScreenProvider extends BiomeScreenProvider {
    private final Map<String, Identifier> biomeMap;
    
    private BetaBiomeScreenProvider(
        WorldScreenProvider parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound parentProviderSettings,
        Consumer<NbtCompound> consumer
    ) {
        super(parent, registryManager, parentProviderSettings, consumer);
        
        this.biomeMap = new HashMap<String, Identifier>();
        
        this.loadBiomeId("desert", BetaBiomes.DESERT_ID);
        this.loadBiomeId("forest", BetaBiomes.FOREST_ID);
        this.loadBiomeId("ice_desert", BetaBiomes.TUNDRA_ID);
        this.loadBiomeId("plains", BetaBiomes.PLAINS_ID);
        this.loadBiomeId("rainforest", BetaBiomes.RAINFOREST_ID);
        this.loadBiomeId("savanna", BetaBiomes.SAVANNA_ID);
        this.loadBiomeId("shrubland", BetaBiomes.SHRUBLAND_ID);
        this.loadBiomeId("seasonal_forest", BetaBiomes.SEASONAL_FOREST_ID);
        this.loadBiomeId("swampland", BetaBiomes.SWAMPLAND_ID);
        this.loadBiomeId("taiga", BetaBiomes.TAIGA_ID);
        this.loadBiomeId("tundra", BetaBiomes.TUNDRA_ID);
        
        this.loadBiomeId("ocean", BetaBiomes.OCEAN_ID);
        this.loadBiomeId("cold_ocean", BetaBiomes.COLD_OCEAN_ID);
        this.loadBiomeId("frozen_ocean", BetaBiomes.FROZEN_OCEAN_ID);
        this.loadBiomeId("lukewarm_ocean",  BetaBiomes.LUKEWARM_OCEAN_ID);
        this.loadBiomeId("warm_ocean",  BetaBiomes.WARM_OCEAN_ID);
    }
    
    public static BetaBiomeScreenProvider create(WorldScreenProvider screenProvider) {
        return new BetaBiomeScreenProvider(
            screenProvider, 
            screenProvider.getRegistryManager(), 
            screenProvider.getBiomeProviderSettings(),
            biomeProviderSettings -> screenProvider.setBiomeProviderSettings(biomeProviderSettings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addBiomeButtonEntry("desert", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.DESERT_ID));
        this.addBiomeButtonEntry("forest", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.FOREST_ID));
        this.addBiomeButtonEntry("ice_desert", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.ICE_DESERT_ID));
        this.addBiomeButtonEntry("plains", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.PLAINS_ID));
        this.addBiomeButtonEntry("rainforest", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.RAINFOREST_ID));
        this.addBiomeButtonEntry("savanna", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.SAVANNA_ID));
        this.addBiomeButtonEntry("shrubland", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.SHRUBLAND_ID));
        this.addBiomeButtonEntry("seasonal_forest", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.SEASONAL_FOREST_ID));
        this.addBiomeButtonEntry("swampland", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.SWAMPLAND_ID));
        this.addBiomeButtonEntry("taiga", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.TAIGA_ID));
        this.addBiomeButtonEntry("tundra", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.TUNDRA_ID));
        
        this.addBiomeButtonEntry("ocean", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.OCEAN_ID));
        this.addBiomeButtonEntry("cold_ocean", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.COLD_OCEAN_ID));
        this.addBiomeButtonEntry("frozen_ocean", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.FROZEN_OCEAN_ID));
        this.addBiomeButtonEntry("lukewarm_ocean", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.LUKEWARM_OCEAN_ID));
        this.addBiomeButtonEntry("warm_ocean", GUIUtil.createTranslatableBiomeStringFromId(BetaBiomes.WARM_OCEAN_ID));
        
        this.children.add(this.buttonList);
    }
    
    private void loadBiomeId(String key, Identifier alternate) {
        this.biomeMap.put(key, new Identifier(NBTUtil.readString(key, this.biomeProviderSettings, alternate.toString())));
    }
    
    private void addBiomeButtonEntry(String key, String biomeText) {
        this.buttonList.addOptionEntry(
            new TextOption(biomeText),
            new ScreenButtonOption(
                GUIUtil.createTranslatableBiomeStringFromId(this.biomeMap.get(key)),
                "",
                buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                  this,
                  this.registryManager,
                  (biome) -> {
                      this.biomeProviderSettings.putString(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                      this.biomeMap.put(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                  }, 
                  this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeMap.get(key))  
                ))
            )
        );
    }
}
