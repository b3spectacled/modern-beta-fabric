package com.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(ButtonWidget.class)
public interface MixinButtonWidgetAccessor {
    @Accessor("onPress")
    public void setOnPress(ButtonWidget.PressAction onPress);
}
