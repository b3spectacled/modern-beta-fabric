package com.bespectacled.modernbeta.api.client.gui.wrapper;

import net.minecraft.client.option.Option;

public interface OptionWrapper {
    Option create();
    
    default Option create(boolean active) {
        return this.create();
    }
}
