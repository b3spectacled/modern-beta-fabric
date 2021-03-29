package com.bespectacled.modernbeta.gui;

import com.bespectacled.modernbeta.mixin.client.*;

import java.util.function.Predicate;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

/*
 * Option Wrapper for Screen Open Button
 */
public class ScreenButtonOption extends Option {
    private final String key;
    private final String suffix;
    private ButtonWidget.PressAction onPress;
    private Predicate<Object> activePredicate;
    
    private AbstractButtonWidget button;
    
    public ScreenButtonOption(String key, String suffix, Predicate<Object> activePredicate, ButtonWidget.PressAction onPress) {
        super(key);
        
        this.key = key;
        this.suffix = suffix;
        this.activePredicate = activePredicate;
        this.onPress = onPress;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        MutableText buttonText = new TranslatableText(this.key);
        MutableText suffixText = new TranslatableText(this.suffix);
        
        if (this.suffix != null) {
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
        
        return this.button;
    }
    
    public void setButtonActive(Object obj) {
        if (this.button == null) return;
        
        if (this.activePredicate.test(obj)) {
            this.button.active = true;
        } else {
            this.button.active = false;
        }
    }
    
    public void updateOnPressAction(ButtonWidget.PressAction onPress) {
        ((MixinButtonWidgetAccessor)this.button).setOnPress(onPress);
        this.onPress = onPress;
    }
    
    public void updateActivePredicate(Predicate<Object> activePredicate) {
        this.activePredicate = activePredicate;
    }
}
