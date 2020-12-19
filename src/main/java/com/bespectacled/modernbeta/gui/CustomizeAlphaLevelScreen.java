package com.bespectacled.modernbeta.gui;

import java.util.Arrays;
import java.util.Iterator;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class CustomizeAlphaLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private OldGeneratorSettings generatorSettings;
    
    private BiomeType biomeType;
    private Iterator<BiomeType> typeIterator;
    
    private boolean generateOceans = ModernBeta.BETA_CONFIG.generateOceans;
    
    private ButtonListWidget buttonList;

    public CustomizeAlphaLevelScreen(CreateWorldScreen parent, OldGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.alpha.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        this.typeIterator = Arrays.asList(BiomeType.values()).iterator();
        this.biomeType = GUIUtil.iterateToBiomeType(BiomeType.CLASSIC, this.typeIterator);
        
        generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
        
        if (generatorSettings.providerSettings.contains("generateOceans"))
            generateOceans = generatorSettings.providerSettings.getBoolean("generateOceans");
    }
    
    @Override
    protected void init() {
        this.addButton(new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            (buttonWidget) -> {
                this.client.openScreen(this.parent);
                return;
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
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create(
                "createWorld.customize.type.biomeType", 
                BiomeType.values(), 
                (value) -> new TranslatableText("createWorld.customize.type." + value.getName()), 
                (gameOptions) -> { return this.biomeType; }, 
                (gameOptions, option, value) -> {
                    this.biomeType = value;
                    generatorSettings.providerSettings.putString("biomeType", this.biomeType.getName());
                    
                    return;
                })
        );
        
        this.buttonList.addSingleOptionEntry(
            CyclingOption.create("createWorld.customize.beta.generateOceans", 
            (gameOptions) -> { return generateOceans; }, 
            (gameOptions, option, value) -> { // Setter
                generateOceans = value;
                generatorSettings.providerSettings.putBoolean("generateOceans", value);
        }));
        
        this.children.add(this.buttonList);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
    }
    
}
