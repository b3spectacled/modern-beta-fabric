package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.client.gui.option.TextOption;

import net.minecraft.client.option.Option;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class TextOptionWrapper implements OptionWrapper {
    private final MutableText text;
    private final List<Formatting> formattingList;
    
    public TextOptionWrapper(MutableText text) {
        this.text = text;
        this.formattingList = new ArrayList<Formatting>();
    }
    
    public TextOptionWrapper(String key) {
        this.text = new TranslatableText(key);
        this.formattingList = new ArrayList<Formatting>();
    }
    
    public TextOptionWrapper formatting(Formatting formatting) {
        this.formattingList.add(formatting);
        
        return this;
    }

    @Override
    public Option create() {
        return this.create(true);
    }

    @Override
    public Option create(boolean active) {
        return new TextOption(this.text, this.formattingList);
    }
}
