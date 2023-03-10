package mod.bespectacled.modernbeta.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class ModernBetaScreen extends Screen {
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_LENGTH = 150;
    public static final int BUTTON_LONG_LENGTH = 225;
    
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(matrices);
        this.renderBackgroundOverlay(matrices);
        this.renderBackgroundGradient(matrices);
        
        DrawableHelper.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
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
    
    protected void renderBackgroundOverlay(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.125f, 0.125f, 0.125f, 1.0f);
        DrawableHelper.drawTexture(matrices, this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayBottom, this.overlayRight - this.overlayLeft, this.overlayBottom - this.overlayTop, 32, 32);
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected void renderBackgroundGradient(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        DrawableHelper.drawTexture(matrices, this.overlayLeft, 0, -100, 0.0f, 0.0f, this.width, this.overlayTop, 32, 32);
        DrawableHelper.drawTexture(matrices, this.overlayLeft, this.overlayBottom, -100, 0.0f, this.overlayBottom, this.width, this.height - this.overlayBottom, 32, 32);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        DrawableHelper.fillGradient(matrices, this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayTop + 4, -16777216, 0);
        DrawableHelper.fillGradient(matrices, this.overlayLeft, this.overlayBottom - 4, this.overlayRight, this.overlayBottom, 0, -16777216);
    }
}
