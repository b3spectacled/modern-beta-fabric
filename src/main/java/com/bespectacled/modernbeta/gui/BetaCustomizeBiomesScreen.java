package com.bespectacled.modernbeta.gui;

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
    
    private Identifier desertId;
    private Identifier forestId;
    private Identifier iceDesertId;
    private Identifier plainsId;
    private Identifier rainforestId;
    private Identifier savannaId;
    private Identifier shrublandId;
    private Identifier seasonalForestId;
    private Identifier swamplandId;
    private Identifier taigaId;
    private Identifier tundraId;
    
    private Identifier oceanId;
    private Identifier coldOceanId;
    private Identifier frozenOceanId;
    private Identifier lukewarmOceanId;
    private Identifier warmOceanId;
    
    protected BetaCustomizeBiomesScreen(
        AbstractCustomizeLevelScreen parent, 
        DynamicRegistryManager registryManager, 
        CompoundTag biomeProviderSettings,
        Consumer<CompoundTag> consumer
    ) {
        super(new TranslatableText("Beta Biomes Configuration"));
        
        this.parent = parent;
        this.registryManager = registryManager;
        this.biomeProviderSettings = biomeProviderSettings;
        this.consumer = consumer;
        this.betaBiomeSettings = new CompoundTag();
        
        this.desertId = this.loadBiomeId("desert", BetaBiomes.DESERT_ID, this.biomeProviderSettings);
        this.forestId = this.loadBiomeId("forest", BetaBiomes.FOREST_ID, this.biomeProviderSettings);
        this.iceDesertId = this.loadBiomeId("ice_desert", BetaBiomes.TUNDRA_ID, this.biomeProviderSettings);
        this.plainsId = this.loadBiomeId("plains", BetaBiomes.PLAINS_ID, this.biomeProviderSettings);
        this.rainforestId = this.loadBiomeId("rainforest", BetaBiomes.RAINFOREST_ID, this.biomeProviderSettings);
        this.savannaId = this.loadBiomeId("savanna", BetaBiomes.SAVANNA_ID, this.biomeProviderSettings);
        this.shrublandId = this.loadBiomeId("shrubland", BetaBiomes.SHRUBLAND_ID, this.biomeProviderSettings);
        this.seasonalForestId = this.loadBiomeId("seasonal_forest", BetaBiomes.SEASONAL_FOREST_ID, this.biomeProviderSettings);
        this.swamplandId = this.loadBiomeId("swampland", BetaBiomes.SWAMPLAND_ID, this.biomeProviderSettings);
        this.taigaId = this.loadBiomeId("taiga", BetaBiomes.TAIGA_ID, this.biomeProviderSettings);
        this.tundraId = this.loadBiomeId("tundra", BetaBiomes.TUNDRA_ID, this.biomeProviderSettings);
        
        this.oceanId = this.loadBiomeId("ocean", BetaBiomes.OCEAN_ID, this.biomeProviderSettings);
        this.coldOceanId = this.loadBiomeId("cold_ocean", BetaBiomes.COLD_OCEAN_ID, this.biomeProviderSettings);
        this.frozenOceanId = this.loadBiomeId("frozen_ocean", BetaBiomes.FROZEN_OCEAN_ID, this.biomeProviderSettings);
        this.lukewarmOceanId = this.loadBiomeId("lukewarm_ocean",  BetaBiomes.LUKEWARM_OCEAN_ID, this.biomeProviderSettings);
        this.warmOceanId = this.loadBiomeId("warm_ocean",  BetaBiomes.WARM_OCEAN_ID, this.biomeProviderSettings);
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
        
        this.addBiomeButtonEntry("desert", this.createTranslatableBiomeString(BetaBiomes.DESERT_ID), this.desertId, biomeId -> this.desertId = biomeId);
        this.addBiomeButtonEntry("forest", this.createTranslatableBiomeString(BetaBiomes.FOREST_ID), this.forestId, biomeId -> this.forestId = biomeId);
        this.addBiomeButtonEntry("ice_desert", this.createTranslatableBiomeString(BetaBiomes.ICE_DESERT_ID), this.iceDesertId, biomeId -> this.iceDesertId = biomeId);
        this.addBiomeButtonEntry("plains", this.createTranslatableBiomeString(BetaBiomes.PLAINS_ID), this.plainsId, biomeId -> this.plainsId = biomeId);
        this.addBiomeButtonEntry("rainforest", this.createTranslatableBiomeString(BetaBiomes.RAINFOREST_ID), this.rainforestId, biomeId -> this.rainforestId = biomeId);
        this.addBiomeButtonEntry("savanna", this.createTranslatableBiomeString(BetaBiomes.SAVANNA_ID), this.savannaId, biomeId -> this.savannaId = biomeId);
        this.addBiomeButtonEntry("shrubland", this.createTranslatableBiomeString(BetaBiomes.SHRUBLAND_ID), this.shrublandId, biomeId -> this.shrublandId = biomeId);
        this.addBiomeButtonEntry("seasonal_forest", this.createTranslatableBiomeString(BetaBiomes.SEASONAL_FOREST_ID), this.seasonalForestId, biomeId -> this.seasonalForestId = biomeId);
        this.addBiomeButtonEntry("swampland", this.createTranslatableBiomeString(BetaBiomes.SWAMPLAND_ID), this.swamplandId, biomeId -> this.swamplandId = biomeId);
        this.addBiomeButtonEntry("taiga", this.createTranslatableBiomeString(BetaBiomes.TAIGA_ID), this.taigaId, biomeId -> this.taigaId = biomeId);
        this.addBiomeButtonEntry("tundra", this.createTranslatableBiomeString(BetaBiomes.TUNDRA_ID), this.tundraId, biomeId -> this.tundraId = biomeId);
        
        this.addBiomeButtonEntry("ocean", this.createTranslatableBiomeString(BetaBiomes.OCEAN_ID), this.oceanId, biomeId -> this.oceanId = biomeId);
        this.addBiomeButtonEntry("cold_ocean", this.createTranslatableBiomeString(BetaBiomes.COLD_OCEAN_ID), this.coldOceanId, biomeId -> this.coldOceanId = biomeId);
        this.addBiomeButtonEntry("frozen_ocean", this.createTranslatableBiomeString(BetaBiomes.FROZEN_OCEAN_ID), this.frozenOceanId, biomeId -> this.frozenOceanId = biomeId);
        this.addBiomeButtonEntry("lukewarm_ocean", this.createTranslatableBiomeString(BetaBiomes.LUKEWARM_OCEAN_ID), this.lukewarmOceanId, biomeId -> this.lukewarmOceanId = biomeId);
        this.addBiomeButtonEntry("warm_ocean", this.createTranslatableBiomeString(BetaBiomes.WARM_OCEAN_ID), this.warmOceanId, biomeId -> this.warmOceanId = biomeId);
        
        this.children.add(this.buttonList);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
    private Identifier loadBiomeId(String key, Identifier defaultId, CompoundTag source) {
        return (source.contains(key)) ? new Identifier(source.getString(key)) : defaultId;
    }
    
    private void addBiomeButtonEntry(String key, String biomeText, Identifier biomeId, Consumer<Identifier> updateIdConsumer) {
        this.buttonList.addOptionEntry(
            new TextOption(biomeText),
            new ScreenButtonOption(
                this.createTranslatableBiomeString(biomeId),
                (b) -> true,
                buttonWidget -> this.client.openScreen(new CustomizeBuffetLevelScreen(
                  this, 
                  this.registryManager,
                  (biome) -> {
                      this.betaBiomeSettings.putString(key, this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                      updateIdConsumer.accept(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome));
                  }, 
                  this.registryManager.<Biome>get(Registry.BIOME_KEY).get(biomeId)  
                ))
            )
        );
    }
    
    private String createTranslatableBiomeString(Identifier biomeId) {
        return "biome." + biomeId.getNamespace() + "." + biomeId.getPath();
    }
}
