package com.bespectacled.modernbeta.api.gui.wrapper;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class BooleanCyclingOptionWrapper implements OptionWrapper {
    private String key;
    private Supplier<Boolean> getter;
    private Consumer<Boolean> setter;
    
    public BooleanCyclingOptionWrapper(String key, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        this.key = key;
        this.getter = getter;
        this.setter = setter;
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
        );
    }
}
