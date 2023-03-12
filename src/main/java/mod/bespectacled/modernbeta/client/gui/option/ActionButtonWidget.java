package mod.bespectacled.modernbeta.client.gui.option;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.OrderableTooltip;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class ActionButtonWidget extends ButtonWidget implements OrderableTooltip {
    private final ActionButtonWidget.TooltipFactory tooltipFactory;

    public ActionButtonWidget(
        int x,
        int y,
        int width,
        int height,
        Text message,
        PressAction onPress,
        ActionButtonWidget.TooltipFactory tooltipFactory
    ) {
        super(x, y, width, height, message, onPress);
        
        this.tooltipFactory = tooltipFactory;
    }

    @Override
    public List<OrderedText> getOrderedTooltip() {
        return this.tooltipFactory.get();
    }

    @FunctionalInterface
    public static interface TooltipFactory extends Supplier<List<OrderedText>> {}
}
