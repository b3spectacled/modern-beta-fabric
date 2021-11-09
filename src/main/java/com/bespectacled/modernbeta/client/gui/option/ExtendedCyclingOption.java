package com.bespectacled.modernbeta.client.gui.option;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class ExtendedCyclingOption<T> extends CyclingOption<T> {
    private final boolean active;
    
    public ExtendedCyclingOption(
        String key,
        Function<GameOptions, T> getter,
        CyclingOption.Setter<T> setter,
        Supplier<CyclingButtonWidget.Builder<T>> buttonBuilderFactory,
        boolean active
    ) {
        super(key, getter, setter, buttonBuilderFactory);
        
        this.active = active;
    }
    
    public static <T> ExtendedCyclingOption<T> create(
        String key,
        T[] values,
        Function<T, Text> valueToText,
        Function<GameOptions, T> getter,
        CyclingOption.Setter<T> setter,
        boolean active
    ) {
        return new ExtendedCyclingOption<T>(
            key, 
            getter, 
            setter, 
            () -> {
                return CyclingButtonWidget.builder(valueToText).values(values);
            },
            active
        );
     }
    
    public static ExtendedCyclingOption<Boolean> create(
        String key,
        Text on,
        Text off,
        Function<GameOptions, Boolean> getter,
        CyclingOption.Setter<Boolean> setter,
        boolean active
    ) {
        return new ExtendedCyclingOption<Boolean>(
            key,
            getter,
            setter,
            () -> CyclingButtonWidget.onOffBuilder(on, off),
            active
        );
     }

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        ClickableWidget widget = super.createButton(options, x, y, width);
        widget.active = this.active;
        
        return widget;
    }
}
