package com.bespectacled.modernbeta.gui.option;

import com.bespectacled.modernbeta.mixin.client.*;

import java.util.function.Predicate;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;

/*
 * Option Wrapper for Screen Open Button
 */
public class ScreenButtonOption extends Option {
    private final String key;
    private ButtonWidget.PressAction onPress;
    private Predicate<Object> activePredicate;
    
    private AbstractButtonWidget button;
    
    public ScreenButtonOption(String key, Predicate<Object> activePredicate, ButtonWidget.PressAction onPress) {
        super(key);
        
        this.key = key;
        this.activePredicate = activePredicate;
        this.onPress = onPress;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        this.button = new ButtonWidget(
            x, y, width, 20,
            new TranslatableText(this.key),
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
    }
    
    public void updateActivePredicate(Predicate<Object> activePredicate) {
        this.activePredicate = activePredicate;
    }
}
