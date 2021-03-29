package com.bespectacled.modernbeta.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

/*
 * Option Wrapper for Static Text Field
 */
public class TextOption extends Option {
    private final String key;
    private AbstractButtonWidget button;

    public TextOption(String key) {
        super(key);
        
        this.key = key;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        this.button = new ButtonWidget(
            x, y, width, 20,
            new TranslatableText(this.key),
            (buttonWidget) -> {}
        ) {
            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                MinecraftClient client = MinecraftClient.getInstance();
                TextRenderer textRenderer = client.textRenderer;
                
                int textColor = 0xFFFFFF;
                DrawableHelper.drawCenteredText(
                    matrices, 
                    textRenderer, 
                    this.getMessage(), 
                    this.x + this.width / 2, 
                    this.y + (this.height - 8) / 2, 
                    textColor | MathHelper.ceil(this.alpha * 255.0f) << 24
                );
            
            }
        };
        this.button.active = false;
            
        return this.button;
    }

}
