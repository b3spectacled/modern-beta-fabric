package com.bespectacled.modernbeta.client.gui.option;

import java.util.List;
import java.util.function.Function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

/*
 * Option Wrapper for arbitrary actions (i.e. opening new screens)
 */
public class ActionOption extends Option {
    private final String key;
    private final String suffix;
    private final ButtonWidget.PressAction onPress;
    private final List<Formatting> formattingList;
    private final Function<MinecraftClient, ActionButtonWidget.TooltipFactory> tooltips;
    private final boolean truncate;
    private final boolean active;
    
    private ActionButtonWidget button;
    
    public ActionOption(
        String key,
        String suffix,
        ButtonWidget.PressAction onPress,
        List<Formatting> formattingList,
        Function<MinecraftClient, ActionButtonWidget.TooltipFactory> tooltips,
        boolean truncate,
        boolean active
    ) {
        super(key);
        
        this.key = key;
        this.suffix = suffix;
        this.onPress = onPress;
        this.formattingList = formattingList;
        this.tooltips = tooltips;
        this.truncate = truncate;
        this.active = active;
    }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        TranslatableText buttonText = new TranslatableText(this.key);
        TranslatableText suffixText = new TranslatableText(this.suffix);
        
        if (!this.suffix.isEmpty() || this.suffix == null) {
            buttonText.append(": ");
            
            // Truncate suffix string if too long
            if (truncate && suffixText.getString().length() > 16) {
                suffixText = new TranslatableText(suffixText.asTruncatedString(16));
                suffixText.append("...");
            }
            
            buttonText.append(suffixText);
        }
        
        this.formattingList.forEach(f -> buttonText.formatted(f));
        
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
}
