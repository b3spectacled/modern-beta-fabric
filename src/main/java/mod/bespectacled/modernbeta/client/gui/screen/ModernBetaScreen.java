package mod.bespectacled.modernbeta.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public abstract class ModernBetaScreen extends Screen {
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_LENGTH = 150;
    public static final int BUTTON_LONG_LENGTH = 200;
    
    public static final int TEXTURE_HEIGHT = 50;
    public static final int TEXTURE_LENGTH = 150;
    
    protected final Screen parent;
    protected int overlayLeft;
    protected int overlayRight;
    protected int overlayTop;
    protected int overlayBottom;
    
    public ModernBetaScreen(Text title, Screen parent) {
        super(title);
        
        this.parent = parent;
    }
    
    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundWithOverlay(context);
        
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    protected void init() {
        this.overlayLeft = 0;
        this.overlayRight = this.width;
        this.overlayTop = 32;
        this.overlayBottom = this.height - 32;
    }
    
    protected GridWidget createGridWidget() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter().alignTop();
        
        return gridWidget;
    }
    
    protected void addGridTextButtonPair(GridWidget.Adder adder, String text, ButtonWidget buttonWidget) {
        adder.add(new TextWidget(Text.translatable(text), this.textRenderer));
        adder.add(buttonWidget);
    }
    
    protected void renderBackgroundOverlay(DrawContext context) {
        context.setShaderColor(0.125f, 0.125f, 0.125f, 1.0f);
        context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayBottom, this.overlayRight - this.overlayLeft, this.overlayBottom - this.overlayTop, 32, 32);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected void renderBackgroundGradient(DrawContext context) {
        context.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, this.overlayLeft, 0, -100, 0.0f, 0.0f, this.width, this.overlayTop, 32, 32);
        context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, this.overlayLeft, this.overlayBottom, -100, 0.0f, this.overlayBottom, this.width, this.height - this.overlayBottom, 32, 32);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        context.fillGradient(this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayTop + 4, -16777216, 0);
        context.fillGradient(this.overlayLeft, this.overlayBottom - 4, this.overlayRight, this.overlayBottom, 0, -16777216);
    }
    
    protected void renderBackgroundWithOverlay(DrawContext context) {
        this.renderBackgroundTexture(context);
        this.renderBackgroundOverlay(context);
        this.renderBackgroundGradient(context);
    }
}
