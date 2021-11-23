package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.client.gui.option.ExtendedCyclingOption;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class CyclingOptionWrapper<T> implements OptionWrapper {
    private final String key;
    private final T[] collection;
    @SuppressWarnings("unused")
    private final Supplier<T> getter;
    private final Consumer<T> setter;
    private final Function<T, Formatting> formatting;
    private final Function<T, List<OrderedText>> tooltips;
    
    private CyclingOption cyclingOption; 
    private Iterator<T> iter;
    private T current;
    
    public CyclingOptionWrapper(
        String key, 
        T[] collection,
        Supplier<T> getter, 
        Consumer<T> setter, 
        Function<T, Formatting> formatting,
        Function<T, List<OrderedText>> tooltips,
        T start
    ) {
        this.key = key;
        this.collection = collection;
        this.getter = getter;
        this.setter = setter;
        this.formatting = formatting;
        this.tooltips = tooltips;
        
        this.iter = Arrays.asList(collection).iterator();
        this.current = this.initIterator(start);
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter, T start) {
        this(key, collection, getter, setter, value -> Formatting.RESET, value -> ImmutableList.of(), start);
    }
    
    public CyclingOptionWrapper(String key, T[] collection, Supplier<T> getter, Consumer<T> setter, Function<T, Formatting> formatting, T start) {
        this(key, collection, getter, setter, formatting, value -> ImmutableList.of(), start);
    }
    
    @Override
    public Option create() {
        return this.create(true);
    }
    
    @Override
    public CyclingOption create(boolean active) {
        this.cyclingOption = new ExtendedCyclingOption(
            this.key,
            (gameOptions, value) -> {
                if (this.iter.hasNext()) {
                    this.current = this.iter.next();
                } else {
                    this.iter = Arrays.asList(this.collection).iterator();
                    this.current = this.iter.next();
                }
                
                this.setter.accept(this.current);
            },
            (gameOptions, cyclingOptions) -> {
                MutableText prefix = new TranslatableText(this.key);
                MutableText currentText = new TranslatableText(this.key + "." + this.current.toString().toLowerCase()).formatted(this.formatting.apply(this.current));
                
                return prefix.append(": ").append(currentText);
            },
            active
        );
        
        cyclingOption.setTooltip(this.tooltips.apply(this.current));
        
        return cyclingOption;
    }
    
    private T initIterator(T toGet) {
        T current = toGet;
        
        while (this.iter.hasNext() && (current = this.iter.next()) != toGet);
        
        return current;
    }
}
