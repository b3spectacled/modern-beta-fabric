package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevOldGeneratorSettings;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.gen.GeneratorOptions;

public class CustomizeInfdevOldLevelScreen extends Screen {
    private CreateWorldScreen parent;
    private InfdevOldGeneratorSettings generatorSettings;
    
    private boolean infdevOldWinterMode = ModernBeta.BETA_CONFIG.infdevOldWinterMode;
    private boolean infdevOldPlus = ModernBeta.BETA_CONFIG.infdevOldPlus;
    private boolean generateVanillaBiomesInfdevOld = ModernBeta.BETA_CONFIG.generateVanillaBiomesInfdevOld;
    private boolean generateInfdevPyramid = ModernBeta.BETA_CONFIG.generateInfdevPyramid;
    private boolean generateInfdevWall = ModernBeta.BETA_CONFIG.generateInfdevWall;
    
    private ButtonListWidget buttonList;

    public CustomizeInfdevOldLevelScreen(CreateWorldScreen parent, InfdevOldGeneratorSettings generatorSettings) {
        super(new TranslatableText("createWorld.customize.infdev.title"));
        
        this.parent = parent;
        this.generatorSettings = generatorSettings;
        
        if (generatorSettings.settings.contains("infdevOldWinterMode"))
            infdevOldWinterMode = generatorSettings.settings.getBoolean("infdevOldWinterMode");
        if (generatorSettings.settings.contains("infdevOldPlus"))
            infdevOldPlus = generatorSettings.settings.getBoolean("infdevOldPlus");
        if (generatorSettings.settings.contains("generateVanillaBiomesInfdevOld"))
            generateVanillaBiomesInfdevOld = generatorSettings.settings.getBoolean("generateVanillaBiomesInfdevOld");
        if (generatorSettings.settings.contains("generateInfdevPyramid")) 
            this.generateInfdevPyramid = generatorSettings.settings.getBoolean("generateInfdevPyramid");
        if (generatorSettings.settings.contains("generateInfdevWall")) 
            this.generateInfdevWall = generatorSettings.settings.getBoolean("generateInfdevWall");
      
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
            new BooleanOption(
                "createWorld.customize.infdev.infdevOldWinterMode", 
                (gameOptions) -> { return infdevOldWinterMode; }, // Getter
                (gameOptions, value) -> { // Setter
                    infdevOldWinterMode = value;
                    generatorSettings.settings.putBoolean("infdevOldWinterMode", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.infdevOldPlus", 
                (gameOptions) -> { return infdevOldPlus; }, 
                (gameOptions, value) -> {
                    infdevOldPlus = value;
                    generatorSettings.settings.putBoolean("infdevOldPlus", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateVanillaBiomesInfdevOld", 
                (gameOptions) -> { return generateVanillaBiomesInfdevOld; }, 
                (gameOptions, value) -> {
                    generateVanillaBiomesInfdevOld = value;
                    generatorSettings.settings.putBoolean("generateVanillaBiomesInfdevOld", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevPyramid", 
                (gameOptions) -> { return generateInfdevPyramid; }, 
                (gameOptions, value) -> {
                    generateInfdevPyramid = value;
                    generatorSettings.settings.putBoolean("generateInfdevPyramid", value);
                }
        ));
        
        this.buttonList.addSingleOptionEntry(
            new BooleanOption(
                "createWorld.customize.infdev.generateInfdevWall", 
                (gameOptions) -> { return generateInfdevWall; }, 
                (gameOptions, value) -> {
                    generateInfdevWall = value;
                    generatorSettings.settings.putBoolean("generateInfdevWall", value);
                }
        ));
        
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
