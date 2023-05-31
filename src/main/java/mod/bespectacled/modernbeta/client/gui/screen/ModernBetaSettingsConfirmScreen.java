package mod.bespectacled.modernbeta.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModernBetaSettingsConfirmScreen extends ModernBetaScreen {
    private final Runnable runnable;
    private final Text messageText;
    private final Text confirmText;
    
    public ModernBetaSettingsConfirmScreen(Screen parent, Runnable runnable, Text messageText, Text confirmText) {
        super(Text.empty(), parent);
        
        this.runnable = runnable;
        this.messageText = messageText;
        this.confirmText = confirmText;
    }

    @Override
    protected void init() {
        super.init();
        
        ButtonWidget buttonDone = ButtonWidget.builder(this.confirmText, button -> {
            this.runnable.run();
            this.client.setScreen(this.parent);
        }).dimensions(0, 0, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        ButtonWidget buttonCancel = ButtonWidget.builder(ScreenTexts.CANCEL, button ->
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        GridWidget gridWidgetMain = this.createGridWidget();
        GridWidget gridWidgetButtons = this.createGridWidget();
        gridWidgetButtons.getMainPositioner().alignVerticalCenter().alignHorizontalCenter();
        
        GridWidget.Adder gridAdderMain = gridWidgetMain.createAdder(1);
        GridWidget.Adder gridAdderButtons = gridWidgetButtons.createAdder(2);
        
        gridAdderMain.add(new TextWidget(this.messageText, this.textRenderer));
        gridAdderMain.add(new TextWidget(Text.empty(), this.textRenderer));
        gridAdderMain.add(gridWidgetButtons);
        
        gridAdderButtons.add(buttonDone);
        gridAdderButtons.add(buttonCancel);
        
        gridWidgetMain.refreshPositions();
        SimplePositioningWidget.setPos(gridWidgetMain, 0, this.height / 2 - gridWidgetMain.getHeight(), this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.forEachChild(this::addDrawableChild);
    }
    
    @Override
    protected void renderBackgroundWithOverlay(DrawContext context) {
        this.renderBackgroundTexture(context);
    }
}
