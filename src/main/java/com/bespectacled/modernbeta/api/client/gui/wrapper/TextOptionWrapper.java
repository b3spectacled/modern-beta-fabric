package com.bespectacled.modernbeta.api.client.gui.wrapper;

import com.bespectacled.modernbeta.client.gui.option.TextOption;

import net.minecraft.client.option.Option;
import net.minecraft.util.Formatting;

public class TextOptionWrapper implements OptionWrapper {
    private final String key;
    private final Formatting formatting;
    
    public TextOptionWrapper(String key, Formatting formatting) {
        this.key = key;
        this.formatting = formatting;
    }
    
    public TextOptionWrapper(String key) {
        this(key, Formatting.RESET);
    }

    @Override
    public Option create() {
        return new TextOption(this.key, this.formatting);
    }

}
