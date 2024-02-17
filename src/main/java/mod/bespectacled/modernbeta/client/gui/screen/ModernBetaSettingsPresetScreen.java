package mod.bespectacled.modernbeta.client.gui.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Environment(value=EnvType.CLIENT)
public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_DESC = "createWorld.customize.modern_beta.preset.desc";
    private static final String TEXT_PRESET_TYPE_DEFAULT = "createWorld.customize.modern_beta.preset.type.default";
    private static final String TEXT_PRESET_TYPE_CUSTOM = "createWorld.customize.modern_beta.preset.type.custom";
    
    //private static final Identifier TEXTURE_PRESET_DEFAULT = createTextureId("default");
    private static final Identifier TEXTURE_PRESET_CUSTOM = createTextureId("custom");
    
    private final List<String> presetsDefault;
    private final List<String> presetsCustom;

    private ModernBetaSettingsPreset preset;
    private PresetsListWidget listWidget;
    private ButtonWidget selectPresetButton;
    
    public ModernBetaSettingsPresetScreen(
        ModernBetaWorldScreen parent,
        List<String> presetsDefault,
        List<String> presetsCustom,
        ModernBetaSettingsPreset preset
    ) {
        super(Text.translatable(TEXT_TITLE), parent);
        
        this.presetsDefault = presetsDefault;
        this.presetsCustom = presetsCustom;
        
        this.preset = preset;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.listWidget = new PresetsListWidget(this.presetsDefault, this.presetsCustom);
        this.addSelectableChild(this.listWidget);
        
        this.selectPresetButton = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("createWorld.customize.presets.select"),
            onPress -> {
                ((ModernBetaWorldScreen)this.parent).setPreset(this.preset);
                this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());
        
        this.addDrawableChild(ButtonWidget.builder(
            ScreenTexts.CANCEL,
            button -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());
        
        this.updateSelectButton(this.listWidget.getSelectedOrNull() != null);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        this.listWidget.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    protected void renderBackgroundWithOverlay(DrawContext context) {}

    private void updateSelectButton(boolean hasSelected) {
        this.selectPresetButton.active = hasSelected;
    }
    
    private static Identifier createTextureId(String id) {
        return ModernBeta.createId("textures/gui/preset_" + id + ".png");
    }

    private class PresetsListWidget extends AlwaysSelectedEntryListWidget<PresetsListWidget.PresetEntry> {
        private static final int ITEM_HEIGHT = 60;
        private static final int ICON_SIZE = 56;
        
        public PresetsListWidget(List<String> presetsDefault, List<String> presetsCustom) {
            super(
                ModernBetaSettingsPresetScreen.this.client,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height,
                32,
                ModernBetaSettingsPresetScreen.this.height - 32,
                ITEM_HEIGHT
            );
            
            presetsDefault.forEach(key -> {
                this.addEntry(new PresetEntry(
                    key,
                    ModernBetaRegistries.SETTINGS_PRESET.get(key),
                    false
                ));
            });
            
            presetsCustom.forEach(key -> {
                this.addEntry(new PresetEntry(
                    key,
                    ModernBetaRegistries.SETTINGS_PRESET_ALT.get(key),
                    true
                ));
            });
        }
        
        @Override
        public void setSelected(PresetEntry entry) {
            super.setSelected(entry);
            
            ModernBetaSettingsPresetScreen.this.updateSelectButton(entry != null);
        }
        
        @Override
        protected int getScrollbarPositionX() {
            return super.getScrollbarPositionX() + 30;
        }
        
        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 85;
        }
        
        private class PresetEntry extends AlwaysSelectedEntryListWidget.Entry<PresetEntry> {
            private static final Identifier TEXTURE_JOIN = new Identifier("world_list/join");
            private static final Identifier TEXTURE_JOIN_HIGHLIGHTED = new Identifier("world_list/join_highlighted");
            
            private static final int TEXT_SPACING = 11;
            private static final int TEXT_LENGTH = 240;
            
            private final Identifier presetTexture;
            private final MutableText presetType;
            private final MutableText presetName;
            private final MutableText presetDesc;
            private final ModernBetaSettingsPreset preset;
            private final boolean isCustom;
            
            private long time;
            
            public PresetEntry(String presetName, ModernBetaSettingsPreset preset, boolean isCustom) {
                this.presetTexture = isCustom ? TEXTURE_PRESET_CUSTOM : createTextureId(presetName);
                this.presetType = isCustom ? Text.translatable(TEXT_PRESET_TYPE_CUSTOM) : Text.translatable(TEXT_PRESET_TYPE_DEFAULT);
                this.presetName = Text.translatable(TEXT_PRESET_NAME + "." + presetName);
                this.presetDesc = Text.translatable(TEXT_PRESET_DESC + "." + presetName);
                this.preset = preset;
                this.isCustom = isCustom;
            }

            @Override
            public Text getNarration() {
                return Text.empty();
            }

            @Override
            public void render(DrawContext context,int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = ModernBetaSettingsPresetScreen.this.textRenderer;
                
                Formatting presetTypeTextFormatting = this.isCustom ? Formatting.AQUA : Formatting.YELLOW;
                MutableText presetTypeText = this.presetType.formatted(presetTypeTextFormatting, Formatting.ITALIC);
                MutableText presetNameText = this.presetName.formatted(Formatting.WHITE);
                
                List<OrderedText> presetDescTexts = this.splitText(textRenderer, this.presetDesc);

                int textStartX = x + ICON_SIZE + 3;
                int textStartY = 1;
                
                context.drawText(textRenderer, presetNameText, textStartX, y + textStartY, Colors.WHITE, false);
                
                int descSpacing = TEXT_SPACING + textStartY + 1;
                for (OrderedText line : presetDescTexts) {
                    context.drawText(textRenderer, line, textStartX, y + descSpacing, Colors.GRAY, false);
                    descSpacing += TEXT_SPACING;
                }

                this.draw(context, x, y, this.presetTexture);

                if (ModernBetaSettingsPresetScreen.this.client.options.getTouchscreen().getValue().booleanValue() || hovered) {
                    boolean isMouseHovering = (mouseX - x) < ICON_SIZE;
                    Identifier texture = isMouseHovering ? TEXTURE_JOIN_HIGHLIGHTED : TEXTURE_JOIN;
                    
                    context.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
                    context.drawGuiTexture(
                        texture,
                        x,
                        y,
                        ICON_SIZE,
                        ICON_SIZE
                    );
                }
            }
            
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button != 0) {
                    return false;
                }
                
                this.setPreset();
                
                if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE) {
                    this.selectPreset();
                }
                
                if (Util.getMeasuringTimeMs() - this.time < 250L) {
                    this.selectPreset();
                }
                
                this.time = Util.getMeasuringTimeMs();
                
                return true;
            }
            
            private void setPreset() {
                PresetsListWidget.this.setSelected(this);
                ModernBetaSettingsPresetScreen.this.preset = this.preset.copy();
            }
            
            private void selectPreset() {
                ModernBetaSettingsPresetScreen presetScreen = ModernBetaSettingsPresetScreen.this;
                
                presetScreen.client.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );
                
                ((ModernBetaWorldScreen)presetScreen.parent).setPreset(this.preset);
                presetScreen.client.setScreen(presetScreen.parent);
            }
            
            private void draw(DrawContext context, int x, int y, Identifier textureId) {
                RenderSystem.enableBlend();
                context.drawTexture(textureId, x, y, 0.0f, 0.0f, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
                RenderSystem.disableBlend();
            }
            
            private List<OrderedText> splitText(TextRenderer textRenderer, Text text) {
                return textRenderer.wrapLines(text, TEXT_LENGTH);
            }
        }
    }
}
