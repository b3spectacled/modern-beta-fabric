package com.bespectacled.modernbeta.client.gui.option;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class ExtendedCyclingOption extends CyclingOption {
    private final boolean active;
    
    public ExtendedCyclingOption(
        String key,
        BiConsumer<GameOptions, Integer> setter,
        BiFunction<GameOptions, CyclingOption, Text> messageProvider,
        boolean active
    ) {
        super(key, setter, messageProvider);
        
        this.active = active;
    }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        ClickableWidget widget = super.createButton(options, x, y, width);
        widget.active = this.active;
        
        return widget;
    }
}
