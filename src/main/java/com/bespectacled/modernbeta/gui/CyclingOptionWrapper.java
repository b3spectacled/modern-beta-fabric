package com.bespectacled.modernbeta.gui;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

public class CyclingOptionWrapper<T> {
    private CyclingOption option;
    
    private final String key;
    private final List<T> items;
    
    private Iterator<T> iter;
    private T current;
    
    private final Consumer<T> consumer;
    
    public CyclingOptionWrapper(String key, List<T> items, T start, Consumer<T> consumer) {
        this.key = key;
        this.items = items;
        
        this.iter = items.iterator();
        this.current = this.initIterator(start);
        
        this.consumer = consumer;
    }
    
    public CyclingOption create() {
        this.option = new CyclingOption(
            this.key,
            (gameOptions, value) -> {
                if (this.iter.hasNext()) {
                    this.current = this.iter.next();
                } else {
                    this.iter = this.items.iterator();
                    this.current = this.iter.next();
                }
                
                this.consumer.accept(this.current);
            },
            (gameOptions, cyclingOptions) -> {
                MutableText buttonLabel = new TranslatableText(this.key);
                buttonLabel.append(": ");
                
                MutableText currentText = new TranslatableText(this.key + "." + current.toString().toLowerCase());
                
                return buttonLabel.append(currentText);
            }
        );
        
        return this.option;
    }
    
    private T initIterator(T toGet) {
        T current = toGet;
        
        while (this.iter.hasNext() && (current = this.iter.next()) != toGet);
        
        return current;
    }
} 
