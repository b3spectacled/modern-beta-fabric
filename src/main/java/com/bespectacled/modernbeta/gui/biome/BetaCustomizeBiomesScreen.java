package com.bespectacled.modernbeta.gui.biome;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.AbstractLevelScreenProvider;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.gui.ScreenButtonOption;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.GUIUtil;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaCustomizeBiomesScreen extends Screen {
    private final AbstractLevelScreenProvider parent;
    private final DynamicRegistryManager registryManager;
    private final NbtCompound biomeProviderSettings;
    private final Consumer<NbtCompound> consumer;
    private final NbtCompound betaBiomeSettings;
    
    private ButtonListWidget buttonList;

    private final Map<String, Identifier> biomeMap;
    
    public BetaCustomizeBiomesScreen(
        AbstractLevelScreenProvider parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound biomeProviderSettings,
        Consumer<NbtCompound> consumer
    ) {
        super(new TranslatableText("createWorld.customize.beta.title"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.biomeProviderSettings = biomeProviderSettings;
        this.consumer = consumer;
        
        this.betaBiomeSettings = new NbtCompound();
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
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.biomeProviderSettings.copyFrom(betaBiomeSettings);
                this.consumer.accept(this.biomeProviderSettings);
                this.client.openScreen(this.parent);
            }
        ));

        this.addButton(new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }
        ));
            
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        
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
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
    private Identifier loadBiomeId(String key, Identifier defaultId) {
        NbtCompound source = this.biomeProviderSettings;
        Identifier biomeId = (source.contains(key)) ? new Identifier(source.getString(key)) : defaultId;
        this.biomeMap.put(key, biomeId);
        
        return biomeId;
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
                      this.betaBiomeSettings.putString(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                      this.biomeMap.put(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                  }, 
                  this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeMap.get(key))  
                ))
            )
        );
    }
}
