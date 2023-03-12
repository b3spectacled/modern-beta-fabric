package mod.bespectacled.modernbeta.client.gui.option;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class ExtendedDoubleOption extends DoubleOption {
    private final boolean active;
    
    public ExtendedDoubleOption(
        String key,
        double min,
        double max,
        float step,
        Function<GameOptions, Double> getter,
        BiConsumer<GameOptions, Double> setter,
        BiFunction<GameOptions, DoubleOption, Text> displayStringGetter,
        Function<MinecraftClient, List<OrderedText>> tooltipsGetter,
        boolean active
    ) {
        super(key, min, max, step, getter, setter, displayStringGetter, tooltipsGetter);
        
        this.active = active;
    }
    
    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        ClickableWidget widget = super.createButton(options, x, y, width);
        widget.active = this.active;
        
        return widget;
    }

}
