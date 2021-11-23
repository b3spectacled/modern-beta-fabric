package com.bespectacled.modernbeta.client.gui.option;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class ExtendedBooleanOption extends BooleanOption {
    private final boolean active;
    
    public ExtendedBooleanOption(String key, Predicate<GameOptions> getter, BiConsumer<GameOptions, Boolean> setter, boolean active) {
        super(key, null, getter, setter);
        
        this.active = active;
    }
    
    @Override
    public Text getDisplayString(GameOptions options) {
        MutableText prefix = new TranslatableText(this.getDisplayPrefix().getString());
        MutableText toggleText = new TranslatableText(this.get(options) ? "options.on" : "options.off");
        
        toggleText = toggleText.formatted(this.get(options) ? 
            this.active ? Formatting.GREEN : Formatting.GRAY : 
            this.active ? Formatting.RED : Formatting.GRAY
        );
        
        return prefix.append(": ").append(toggleText);
    }
    
    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        ClickableWidget widget = super.createButton(options, x, y, width);
        widget.active = this.active;
        
        return widget;
    }
}
