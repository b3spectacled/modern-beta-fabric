package com.bespectacled.modernbeta.api.gui.wrapper;

import com.bespectacled.modernbeta.gui.option.ActionOption;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;

public class ActionOptionWrapper implements OptionWrapper {
    private final String key;
    private final String suffix;
    private final ButtonWidget.PressAction onPress;
    
    public ActionOptionWrapper(String key, String suffix, ButtonWidget.PressAction onPress) {
        this.key = key;
        this.suffix = suffix;
        this.onPress = onPress;
    }
    
    @Override
    public Option create() {
        return new ActionOption(this.key, this.suffix, this.onPress);
    }
}
