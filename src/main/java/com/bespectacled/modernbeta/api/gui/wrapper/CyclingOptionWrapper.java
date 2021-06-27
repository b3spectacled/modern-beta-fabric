package com.bespectacled.modernbeta.api.gui.wrapper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class CyclingOptionWrapper<T> implements OptionWrapper {
    private final String key;
    private final T[] collection;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Function<T, Formatting> formatting;
    private final List<OrderedText> tooltips;
    
    public CyclingOptionWrapper(
        String key, 
        T[] collection,
        Supplier<T> getter, 
        Consumer<T> setter, 
        Function<T, Formatting> formatting,
        List<OrderedText> tooltips
    ) {
        this.key = key;
        this.collection = collection;
        this.getter = getter;
        this.setter = setter;
        this.formatting = formatting;
        this.tooltips = tooltips;
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter) {
        this(key, collection, getter, setter, value -> Formatting.RESET, ImmutableList.of());
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter, Function<T, Formatting> formatting) {
        this(key, collection, getter, setter, formatting, ImmutableList.of());
    }
    
    @Override
    public CyclingOption<T> create() {
        return CyclingOption.create(
            this.key,
            this.collection,
            value -> new TranslatableText(this.key + "." + value.toString().toLowerCase()).formatted(this.formatting.apply(value)), 
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> this.setter.accept(value)
        ).tooltip(client -> value -> this.tooltips);
    }
}
