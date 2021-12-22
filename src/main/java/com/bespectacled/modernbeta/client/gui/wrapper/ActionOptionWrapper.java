package com.bespectacled.modernbeta.client.gui.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.api.client.gui.wrapper.OptionWrapper;
import com.bespectacled.modernbeta.client.gui.option.ActionOption;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Formatting;

public class ActionOptionWrapper implements OptionWrapper {
    private final String key;
    private final ButtonWidget.PressAction onPress;
    
    private String suffix;
    private List<Formatting> formattingList;
    private Supplier<List<OrderedText>> tooltips;
    private boolean truncate;
    
    public ActionOptionWrapper(String key, ButtonWidget.PressAction onPress) {
        this.key = key;
        this.onPress = onPress;
        
        this.suffix = "";
        this.formattingList = new ArrayList<Formatting>();
        this.tooltips = () -> ImmutableList.of();
        this.truncate = true;
    }
    
    public ActionOptionWrapper suffix(String suffix) {
        this.suffix = suffix;
        
        return this;
    }
    
    public ActionOptionWrapper formatting(Formatting formatting) {
        this.formattingList.add(formatting);
        
        return this;
    }
    
    public ActionOptionWrapper tooltips(Supplier<List<OrderedText>> tooltips) {
        this.tooltips = tooltips;
        
        return this;
    }
    
    public ActionOptionWrapper truncate(boolean truncate) {
        this.truncate = truncate;
        
        return this;
    }
    
    @Override
    public Option create() {
        return this.create(true);
    }
    
    @Override
    public ActionOption create(boolean active) {
        return new ActionOption(
            this.key,
            this.suffix,
            this.onPress,
            this.formattingList,
            client -> () -> this.tooltips.get(),
            this.truncate,
            active
        );
    }
}
