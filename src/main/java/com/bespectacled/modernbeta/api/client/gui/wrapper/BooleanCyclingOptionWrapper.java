package com.bespectacled.modernbeta.api.client.gui.wrapper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.bespectacled.modernbeta.client.gui.option.ExtendedCyclingOption;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class BooleanCyclingOptionWrapper implements OptionWrapper {
    private final String key;
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final List<OrderedText> tooltips;
    
    public BooleanCyclingOptionWrapper(String key, Supplier<Boolean> getter, Consumer<Boolean> setter, List<OrderedText> tooltips) {
        this.key = key;
        this.getter = getter;
        this.setter = setter;
        this.tooltips = tooltips;
    }
    
    public BooleanCyclingOptionWrapper(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        this(key, getter, setter, ImmutableList.of());
    }
    
    @Override
    public CyclingOption<Boolean> create() {
        return CyclingOption.create(
            this.key,
            new TranslatableText(ScreenTexts.ON.getString()).formatted(Formatting.GREEN),
            new TranslatableText(ScreenTexts.OFF.getString()).formatted(Formatting.RED),
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> {
                this.setter.accept(value);
            }
        ).tooltip(client -> value -> this.tooltips);
    }
    
    @Override
    public CyclingOption<Boolean> create(boolean active) {
        CyclingOption<Boolean> cyclingOption = ExtendedCyclingOption.create(
            this.key,
            new TranslatableText(ScreenTexts.ON.getString()).formatted(active ? Formatting.GREEN : Formatting.GRAY),
            new TranslatableText(ScreenTexts.OFF.getString()).formatted(active ? Formatting.RED : Formatting.GRAY),
            gameOptions -> this.getter.get(),
            (gameOptions, option, value) -> {
                this.setter.accept(value);
            },
            active
        ).tooltip(client -> value -> this.tooltips);
        
        return cyclingOption;
    }
}
