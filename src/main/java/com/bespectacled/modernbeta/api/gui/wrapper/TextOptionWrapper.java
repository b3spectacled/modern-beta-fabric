package com.bespectacled.modernbeta.api.gui.wrapper;

import com.bespectacled.modernbeta.gui.option.TextOption;

import net.minecraft.client.option.Option;

public class TextOptionWrapper implements OptionWrapper {
    private final String key;
    
    public TextOptionWrapper(String key) {
        this.key = key;
    }

    @Override
    public Option create() {
        return new TextOption(this.key);
    }

}
