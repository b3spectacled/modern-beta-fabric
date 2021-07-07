package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.client.gui.option.FormattedBooleanOption;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.option.BooleanOption;
import net.minecraft.text.OrderedText;

public class BooleanOptionWrapper implements OptionWrapper {
    private final String key;
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final List<OrderedText> tooltips;
    
    public BooleanOptionWrapper(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, List<OrderedText> tooltips) {
        this.key = key;
        this.getter = getter;
        this.setter = setter;
        this.tooltips = tooltips;
    }
    
    public BooleanOptionWrapper(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        this(key, getter, setter, ImmutableList.of());
    }
    
    @Override
    public BooleanOption create() {
        FormattedBooleanOption booleanOption = new FormattedBooleanOption(
            this.key,
            gameOptions -> this.getter.get(),
            (gameOptions, value) -> {
                this.setter.accept(value);
            }
        );
        
        booleanOption.setTooltip(tooltips);
        
        return booleanOption;
    }
}
