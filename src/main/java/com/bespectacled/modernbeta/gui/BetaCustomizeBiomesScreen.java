package com.bespectacled.modernbeta.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.gui.option.TextOption;
import com.bespectacled.modernbeta.gui.option.ScreenButtonOption;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BetaCustomizeBiomesScreen extends Screen {
    private final AbstractCustomizeLevelScreen parent;
    private final DynamicRegistryManager registryManager;
    private final CompoundTag biomeProviderSettings;
    private final Consumer<CompoundTag> consumer;
    private final CompoundTag betaBiomeSettings;
    
    private ButtonListWidget buttonList;

    private final Map<String, Identifier> biomeMap;
    
    protected BetaCustomizeBiomesScreen(
        AbstractCustomizeLevelScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings,
        Consumer<CompoundTag> consumer
    ) {
        super(new TranslatableText("createWorld.customize.beta.title"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.biomeProviderSettings = biomeProviderSettings;
        this.consumer = consumer;
        
        this.betaBiomeSettings = new CompoundTag();
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
        
        this.addBiomeButtonEntry("desert", this.createTranslatableBiomeString(BetaBiomes.DESERT_ID));
        this.addBiomeButtonEntry("forest", this.createTranslatableBiomeString(BetaBiomes.FOREST_ID));
        this.addBiomeButtonEntry("ice_desert", this.createTranslatableBiomeString(BetaBiomes.ICE_DESERT_ID));
        this.addBiomeButtonEntry("plains", this.createTranslatableBiomeString(BetaBiomes.PLAINS_ID));
        this.addBiomeButtonEntry("rainforest", this.createTranslatableBiomeString(BetaBiomes.RAINFOREST_ID));
        this.addBiomeButtonEntry("savanna", this.createTranslatableBiomeString(BetaBiomes.SAVANNA_ID));
        this.addBiomeButtonEntry("shrubland", this.createTranslatableBiomeString(BetaBiomes.SHRUBLAND_ID));
        this.addBiomeButtonEntry("seasonal_forest", this.createTranslatableBiomeString(BetaBiomes.SEASONAL_FOREST_ID));
        this.addBiomeButtonEntry("swampland", this.createTranslatableBiomeString(BetaBiomes.SWAMPLAND_ID));
        this.addBiomeButtonEntry("taiga", this.createTranslatableBiomeString(BetaBiomes.TAIGA_ID));
        this.addBiomeButtonEntry("tundra", this.createTranslatableBiomeString(BetaBiomes.TUNDRA_ID));
        
        this.addBiomeButtonEntry("ocean", this.createTranslatableBiomeString(BetaBiomes.OCEAN_ID));
        this.addBiomeButtonEntry("cold_ocean", this.createTranslatableBiomeString(BetaBiomes.COLD_OCEAN_ID));
        this.addBiomeButtonEntry("frozen_ocean", this.createTranslatableBiomeString(BetaBiomes.FROZEN_OCEAN_ID));
        this.addBiomeButtonEntry("lukewarm_ocean", this.createTranslatableBiomeString(BetaBiomes.LUKEWARM_OCEAN_ID));
        this.addBiomeButtonEntry("warm_ocean", this.createTranslatableBiomeString(BetaBiomes.WARM_OCEAN_ID));
        
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
        CompoundTag source = this.biomeProviderSettings;
        Identifier biomeId = (source.contains(key)) ? new Identifier(source.getString(key)) : defaultId;
        this.biomeMap.put(key, biomeId);
        
        return biomeId;
    }
    
    private void addBiomeButtonEntry(String key, String biomeText) {
        this.buttonList.addOptionEntry(
            new TextOption(biomeText),
            new ScreenButtonOption(
                this.createTranslatableBiomeString(this.biomeMap.get(key)),
                (b) -> true,
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
    
    private String createTranslatableBiomeString(Identifier biomeId) {
        return "biome." + biomeId.getNamespace() + "." + biomeId.getPath();
    }
}
