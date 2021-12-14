package com.bespectacled.modernbeta.client.gui.option;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

/*
 * Option Wrapper for Static Text Field
 */
public class TextOption extends Option {
    private final MutableText text;
    private final List<Formatting> formattingList;
    
    private ClickableWidget button;
    
    public TextOption(MutableText text, List<Formatting> formattingList) {
        super(text.asString());
        
        this.text = text;
        this.formattingList = formattingList;
    }
    
    public TextOption(TranslatableText text) {
        this(text, List.of());
    }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        this.formattingList.forEach(f -> this.text.formatted(f));
        
        this.button = new ButtonWidget(
            x, y, width, 20,
            this.text,
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
