package com.bespectacled.modernbeta.api.gui.screen;

import java.util.List;

import com.bespectacled.modernbeta.api.gui.wrapper.OptionWrapper;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

public abstract class GUIScreen extends Screen {
    private ButtonListWidget buttonList; // Do not allow button list to be directly modifiable.
    
    protected final Screen parent;
    
    protected GUIScreen(String title, Screen parent) {
        super(new TranslatableText(title));
        
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.buttonList);
    }
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float tickDelta) {
        this.renderBackground(matrixStack);
        
        this.buttonList.render(matrixStack, mouseX, mouseY, tickDelta);
        DrawableHelper.drawCenteredText(matrixStack, this.textRenderer, this.title, this.width / 2, 16, 16777215);
        
        super.render(matrixStack, mouseX, mouseY, tickDelta);
        
        // Render tooltips
        List<OrderedText> tooltips = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        if (tooltips != null) {
            this.renderOrderedTooltip(matrixStack, tooltips, mouseX, mouseY);
        }
    }
    
    public void addOption(OptionWrapper option) {
        this.buttonList.addSingleOptionEntry(option.create());
    }
    
    public void addDualOption(OptionWrapper firstOption, OptionWrapper secondOption) {
        this.buttonList.addOptionEntry(firstOption.create(), secondOption.create());
    }
}
