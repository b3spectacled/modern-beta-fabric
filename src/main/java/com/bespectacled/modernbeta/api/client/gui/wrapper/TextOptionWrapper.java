package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.client.gui.option.TextOption;

import net.minecraft.client.option.Option;
import net.minecraft.util.Formatting;

public class TextOptionWrapper implements OptionWrapper {
    private final String key;
    private final List<Formatting> formattingList;
    
    public TextOptionWrapper(String key) {
        this.key = key;
        this.formattingList = new ArrayList<Formatting>();
    }
    
    public TextOptionWrapper(String key, Formatting initialFormatting) {
        this(key);
        this.formattingList.add(initialFormatting);
    }
    
    public TextOptionWrapper formatting(Formatting formatting) {
        this.formattingList.add(formatting);
        
        return this;
    }

    @Override
    public Option create(boolean active) {
        return new TextOption(this.key, this.formattingList);
    }

}