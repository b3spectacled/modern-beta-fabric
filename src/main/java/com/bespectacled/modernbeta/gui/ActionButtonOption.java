package com.bespectacled.modernbeta.gui;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

/*
 * Option Wrapper for arbitrary actions (i.e. opening new screens)
 */
public class ActionButtonOption extends Option {
    private final String key;
    private final String suffix;
    private final ButtonWidget.PressAction onPress;
    
    private AbstractButtonWidget button;
    
    public ActionButtonOption(String key, String suffix, ButtonWidget.PressAction onPress) {
        super(key);
        
        this.key = key;
        this.suffix = suffix;
        this.onPress = onPress;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        MutableText buttonText = new TranslatableText(this.key);
        MutableText suffixText = new TranslatableText(this.suffix);
        
        if (!this.suffix.isEmpty() || this.suffix == null) {
            buttonText.append(": ");
            
            // Truncate suffix string if too long
            if (suffixText.getString().length() > 16) {
                suffixText = new TranslatableText(suffixText.asTruncatedString(16));
                suffixText.append("...");
            }
            
            buttonText.append(suffixText);
        }
        
        this.button = new ButtonWidget(
            x, y, width, 20,
            buttonText,
            this.onPress
        );
        
        if (this.onPress == null)
            this.button.active = false;
        
        return this.button;
    }
}
