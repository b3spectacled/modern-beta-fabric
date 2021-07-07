package com.bespectacled.modernbeta.client.gui.option;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class FormattedBooleanOption extends BooleanOption {
    public FormattedBooleanOption(String key, Predicate<GameOptions> getter, BiConsumer<GameOptions, Boolean> setter) {
        super(key, null, getter, setter);
    }
    
    @Override
    public Text getDisplayString(GameOptions options) {
        MutableText prefix = new TranslatableText(this.getDisplayPrefix().getString());
        MutableText toggleText = new TranslatableText(this.get(options) ? "options.on" : "options.off");
        
        toggleText = toggleText.formatted(this.get(options) ? Formatting.GREEN : Formatting.RED);
        
        return prefix.append(": ").append(toggleText);
    }
}
