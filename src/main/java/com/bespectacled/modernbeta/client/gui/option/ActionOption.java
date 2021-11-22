package com.bespectacled.modernbeta.client.gui.option;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

/*
 * Option Wrapper for arbitrary actions (i.e. opening new screens)
 */
public class ActionOption extends Option {
    private final String key;
    private final String suffix;
    private final ButtonWidget.PressAction onPress;
    private final boolean active;
    private Function<MinecraftClient, ActionButtonWidget.TooltipFactory> tooltips;
    
    private ActionButtonWidget button;
    
    public ActionOption(String key, String suffix, ButtonWidget.PressAction onPress) {
        this(key, suffix, onPress, true);
    }
    
    public ActionOption(String key, String suffix, ButtonWidget.PressAction onPress, boolean active) {
        this(key, suffix, onPress, client -> () -> ImmutableList.of(), active);
    }
    
    public ActionOption(
        String key,
        String suffix,
        ButtonWidget.PressAction onPress,
        Function<MinecraftClient, ActionButtonWidget.TooltipFactory> tooltips,
        boolean active
    ) {
        super(key);
        
        this.key = key;
        this.suffix = suffix;
        this.onPress = onPress;
        this.tooltips = tooltips;
        this.active = active;
    }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
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
        
        this.button = new ActionButtonWidget(
            x, y, width, 20,
            buttonText,
            this.onPress,
            this.tooltips.apply(MinecraftClient.getInstance())
        );
        
        if (this.onPress == null || !this.active)
            this.button.active = false;
        
        return this.button;
    }
    
    public ActionOption tooltip(Function<MinecraftClient, ActionButtonWidget.TooltipFactory> tooltips) {
        this.tooltips = tooltips;
        
        return this;
    }
    
    
}
