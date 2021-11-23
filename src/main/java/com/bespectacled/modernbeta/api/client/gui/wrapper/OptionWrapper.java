package com.bespectacled.modernbeta.api.client.gui.wrapper;

import net.minecraft.client.option.Option;

public interface OptionWrapper {
    default Option create() {
        return this.create(true);
    }
    
    Option create(boolean active);
}
