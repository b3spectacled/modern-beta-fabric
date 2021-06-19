package com.bespectacled.modernbeta.api.gui.wrapper;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class CyclingOptionWrapper<T> implements OptionWrapper {
    private final String key;
    private final T[] collection;
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Function<T, Formatting> formatting;
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter) {
        this.key = key;
        this.collection = collection;
        this.getter = getter;
        this.setter = setter;
        this.formatting = value -> Formatting.RESET;
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter, Function<T, Formatting> formatting) {
        this.key = key;
        this.collection = collection;
        this.getter = getter;
        this.setter = setter;
        this.formatting = formatting;
    }
    
    @Override
    public CyclingOption<T> create() {
        return CyclingOption.create(
            this.key,
            this.collection,
            value -> new TranslatableText(this.key + "." + value.toString().toLowerCase()).formatted(this.formatting.apply(value)), 
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> this.setter.accept(value)
        );
    }
}
