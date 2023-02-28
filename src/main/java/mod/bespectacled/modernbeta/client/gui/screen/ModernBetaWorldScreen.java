package mod.bespectacled.modernbeta.client.gui.screen;

import org.apache.logging.log4j.util.TriConsumer;
import com.mojang.blaze3d.systems.RenderSystem;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModernBetaWorldScreen extends Screen {
    private final Screen parent;
    private final TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone;

    private ModernBetaSettingsPreset preset;
    private int left;
    private int right;
    private int top;
    private int bottom;
    
    public ModernBetaWorldScreen(Screen parent, GeneratorOptionsHolder generatorOptionsHolder, TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone) {
        super(Text.translatable("createWorld.customize.modern_beta.title"));
        
        this.parent = parent;
        this.onDone = onDone;

        ModernBetaChunkGenerator chunkGenerator = (ModernBetaChunkGenerator)generatorOptionsHolder.selectedDimensions().getChunkGenerator();
        ModernBetaSettingsChunk settingsChunk = new ModernBetaSettingsChunk.Builder(chunkGenerator.getChunkSettings()).build();
        
        this.preset = ModernBetaRegistries.SETTINGS_PRESET.get(settingsChunk.chunkProvider);
    }
    
    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
    
    @Override
    protected void init() {
        this.left = 0;
        this.right = this.width;
        this.top = 32;
        this.bottom = this.height - 32;
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(this.preset.getNbtChunk(), this.preset.getNbtBiome(), this.preset.getNbtCaveBiome());
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());
        
        CyclingButtonWidget<ModernBetaSettingsPreset> presetWidget = CyclingButtonWidget.builder(ModernBetaSettingsPreset::getName)
            .values(ModernBetaRegistries.SETTINGS_PRESET.getEntries())
            .initially(this.preset)
            .build(0, 0, 150, 20, Text.translatable("createWorld.customize.modern_beta.preset"), (button, preset) -> {
                this.preset = preset;
            });
        
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter().alignTop();
        
        GridWidget.Adder gridWidgetAdder = gridWidget.createAdder(2);
        gridWidgetAdder.add(presetWidget);
        
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.top + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(matrices);
        this.renderBackgroundOverlay(matrices);
        this.renderBackgroundGradient(matrices);
        
        DrawableHelper.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
    }
    
    private void renderBackgroundOverlay(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.125f, 0.125f, 0.125f, 1.0f);
        DrawableHelper.drawTexture(matrices, this.left, this.top, this.right, this.bottom, this.right - this.left, this.bottom - this.top, 32, 32);
        
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderBackgroundGradient(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        DrawableHelper.drawTexture(matrices, this.left, 0, -100, 0.0f, 0.0f, this.width, this.top, 32, 32);
        DrawableHelper.drawTexture(matrices, this.left, this.bottom, -100, 0.0f, this.bottom, this.width, this.height - this.bottom, 32, 32);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        DrawableHelper.fillGradient(matrices, this.left, this.top, this.right, this.top + 4, -16777216, 0);
        DrawableHelper.fillGradient(matrices, this.left, this.bottom - 4, this.right, this.bottom, 0, -16777216);
    }
}
