package com.bespectacled.modernbeta.gui;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;

public class DummyButtonOption extends Option {
    private final String key;
    private AbstractButtonWidget button;

    public DummyButtonOption(String key) {
        super(key);
        
        this.key = key;
    }

    @Override
    public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width) {
        this.button = new ButtonWidget(
            x, y, width, 20,
            new TranslatableText(this.key),
            (buttonWidget) -> {}
        );
        this.button.active = false;
            
        return this.button;
    }

}
