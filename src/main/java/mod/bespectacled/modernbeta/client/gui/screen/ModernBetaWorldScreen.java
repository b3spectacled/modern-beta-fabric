package mod.bespectacled.modernbeta.client.gui.screen;

import java.util.function.Function;

import org.apache.logging.log4j.util.TriConsumer;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModernBetaWorldScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title"; 
    private static final String TEXT_TITLE_CHUNK = "createWorld.customize.modern_beta.title.chunk"; 
    private static final String TEXT_TITLE_BIOME = "createWorld.customize.modern_beta.title.biome"; 
    private static final String TEXT_TITLE_CAVE_BIOME = "createWorld.customize.modern_beta.title.cave_biome"; 
    
    private static final String TEXT_PRESET = "createWorld.customize.modern_beta.preset";
    private static final String TEXT_CUSTOM = "createWorld.customize.modern_beta.preset.custom";
    private static final String TEXT_CHUNK = "createWorld.customize.modern_beta.chunk";
    private static final String TEXT_BIOME = "createWorld.customize.modern_beta.biome";
    private static final String TEXT_CAVE_BIOME = "createWorld.customize.modern_beta.cave_biome";
    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_RESET = "createWorld.customize.modern_beta.reset";
    
    private final TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone;
    private ModernBetaSettingsPreset preset;
    private CyclingButtonWidget<String> presetWidget;
    
    public ModernBetaWorldScreen(Screen parent, GeneratorOptionsHolder generatorOptionsHolder, TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone) {
        super(Text.translatable(TEXT_TITLE), parent);
        
        this.onDone = onDone;
        
        ModernBetaChunkGenerator chunkGenerator = (ModernBetaChunkGenerator)generatorOptionsHolder.selectedDimensions().getChunkGenerator();
        ModernBetaBiomeSource biomeSource = (ModernBetaBiomeSource)chunkGenerator.getBiomeSource();
        
        this.preset = new ModernBetaSettingsPreset(chunkGenerator.getChunkSettings(), biomeSource.getBiomeSettings(), biomeSource.getCaveBiomeSettings());
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(this.preset.getNbtChunk(), this.preset.getNbtBiome(), this.preset.getNbtCaveBiome());
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        Function<String, Text> presetText = key -> Text.translatable(TEXT_PRESET + "." + key);
        this.presetWidget = CyclingButtonWidget
            .builder(presetText)
            .values(ModernBetaRegistries.SETTINGS_PRESET.getKeySet())
            .initially(new ModernBetaSettingsChunk.Builder(this.preset.getNbtChunk()).chunkProvider)
            .build(0, 0, 150, 20, Text.translatable(TEXT_PRESET), (button, key) -> {
                this.preset = ModernBetaRegistries.SETTINGS_PRESET.get(key);
        });
        
        this.onPresetChange();
        
        ButtonWidget buttonChunk = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                ModernBetaSettingsChunk.fromCompound(this.preset.getNbtChunk()), string -> this.preset = this.preset.set(string, "", "")
            ))
        ).build();
        
        ButtonWidget buttonBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_BIOME,
                this,
                ModernBetaSettingsBiome.fromCompound(this.preset.getNbtBiome()), string -> this.preset = this.preset.set("", string, "")
            ))
        ).build();
        
        ButtonWidget buttonCaveBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CAVE_BIOME,
                this,
                ModernBetaSettingsCaveBiome.fromCompound(this.preset.getNbtCaveBiome()), string -> this.preset = this.preset.set("", "", string)
            ))
        ).build();
        
        ButtonWidget buttonReset = ButtonWidget.builder(
            Text.translatable(TEXT_RESET),
            button -> this.client.setScreen(new ModernBetaSettingsResetScreen(this, this::resetPreset))
        ).build();
        
        GridWidget gridWidgetMain = this.createGridWidget();
        GridWidget gridWidgetSettings = this.createGridWidget();
        
        GridWidget.Adder gridAdderMain = gridWidgetMain.createAdder(1);
        GridWidget.Adder gridAdderSettings = gridWidgetSettings.createAdder(2);
        gridAdderSettings.getMainPositioner().alignVerticalCenter();
        
        gridAdderMain.add(this.presetWidget);
        gridAdderMain.add(gridWidgetSettings);
        gridAdderMain.add(buttonReset);
        
        this.addGridTextButtonPair(gridAdderSettings, TEXT_CHUNK, buttonChunk);
        this.addGridTextButtonPair(gridAdderSettings, TEXT_BIOME, buttonBiome);
        this.addGridTextButtonPair(gridAdderSettings, TEXT_CAVE_BIOME, buttonCaveBiome);
        
        gridWidgetMain.refreshPositions();
        SimplePositioningWidget.setPos(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.forEachChild(this::addDrawableChild);
    }
    
    private void onPresetChange() {
        if (!ModernBetaRegistries.SETTINGS_PRESET.contains(this.preset)) {
            this.presetWidget.setMessage(Text.translatable(TEXT_PRESET).append(": ").append(Text.translatable(TEXT_CUSTOM)));
            this.presetWidget.active = false;
        } else {
            this.presetWidget.setValue(ModernBetaRegistries.SETTINGS_PRESET.getKey(this.preset));
            this.presetWidget.active = true;
        }
    }
    
    private void resetPreset() {
        this.preset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Chunk.BETA.id);
        this.onPresetChange();
    }
}
