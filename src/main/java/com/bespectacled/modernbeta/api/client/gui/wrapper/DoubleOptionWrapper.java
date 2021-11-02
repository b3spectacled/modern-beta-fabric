package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

public class DoubleOptionWrapper<T extends Number> implements OptionWrapper {
    private final String key;
    private final String suffix;
    private final double min;
    private final double max;
    private final float step;
    
    private final Supplier<T> getter;
    private final Consumer<Double> setter;
    
    private final List<OrderedText> tooltips;
    
    public DoubleOptionWrapper(
        String key,
        String suffix,
        double min,
        double max,
        float step,
        Supplier<T> getter,
        Consumer<Double> setter,
        List<OrderedText> tooltipsGetter
    ) {
        this.key = key;
        this.suffix = suffix;
        this.min = min;
        this.max = max;
        this.step = step;
        this.getter = getter;
        this.setter = setter;
        this.tooltips = tooltipsGetter;
    }
    
    public DoubleOptionWrapper(
        String key,
        String suffix,
        double min,
        double max,
        float step,
        Supplier<T> getter,
        Consumer<Double> setter
    ) {
        this(key, suffix, min, max, step, getter, setter, ImmutableList.of());
    }
    
    public DoubleOptionWrapper(
        String key,
        double min,
        double max,
        float step,
        Supplier<T> getter,
        Consumer<Double> setter
    ) {
        this(key, "", min, max, step, getter, setter, ImmutableList.of());
    }

    @Override
    public DoubleOption create() {
        return new DoubleOption(
            this.key, 
            this.min, this.max, this.step,
            gameOptions -> this.getter.get().doubleValue(), // Getter
            (gameOptions, value) -> this.setter.accept(value),
            (gameOptions, doubleOptions) -> {
                MutableText value = this.getter.get() instanceof Float ?
                    new LiteralText(String.format("%.2f", this.getter.get())) :
                    new LiteralText(this.getter.get().toString());
                
                return new TranslatableText(
                    "options.generic_value",
                    new Object[] { 
                        new TranslatableText(this.key),
                        value.append(" ").append(this.suffix)
                });
            },
            client -> tooltips
        );
    }
}

