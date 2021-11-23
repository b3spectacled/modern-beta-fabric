package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.client.gui.option.ActionOption;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.text.OrderedText;

public class ActionOptionWrapper implements OptionWrapper {
    private final String key;
    private final String suffix;
    private final ButtonWidget.PressAction onPress;
    private final Supplier<List<OrderedText>> tooltips;
    
    public ActionOptionWrapper(String key, String suffix, ButtonWidget.PressAction onPress) {
        this(key, suffix, onPress, () -> ImmutableList.of());
    }
    
    public ActionOptionWrapper(
        String key,
        String suffix,
        ButtonWidget.PressAction onPress,
        Supplier<List<OrderedText>> tooltips
    ) {
        this.key = key;
        this.suffix = suffix;
        this.onPress = onPress;
        this.tooltips = tooltips;
    }
    
    @Override
    public Option create() {
        return this.create(true);
    }
    
    @Override
    public ActionOption create(boolean active) {
        return new ActionOption(this.key, this.suffix, this.onPress, active).tooltip(client -> () -> this.tooltips.get());
    }
}
