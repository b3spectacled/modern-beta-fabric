package mod.bespectacled.modernbeta.client.gui.screen;

import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mod.bespectacled.modernbeta.settings.ModernBetaSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ModernBetaEditScreen extends ModernBetaScreen {
    private static final String SETTINGS = "createWorld.customize.modern_beta.settings";
    
    private final Consumer<String> onDone;
    private final Gson gson;
    private String settingsString;
    private EditBoxWidget settingsBox;
    
    public ModernBetaEditScreen(String title, Screen parent, ModernBetaSettings settings, Consumer<String> onDone) {
        super(Text.translatable(title), parent);

        this.onDone = onDone;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        
        this.settingsString = this.gson.toJson(settings);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(this.settingsString);
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        int editBoxWidth = (int)(this.width * 0.8);
        int editBoxHeight = (int)(this.height * 0.5);
        
        this.settingsBox = new EditBoxWidget(this.textRenderer, 0, 0, editBoxWidth, editBoxHeight, Text.of(""), Text.translatable(SETTINGS));
        this.settingsBox.setChangeListener(string -> this.settingsString = string);
        this.settingsBox.setText(this.settingsString);
        
        GridWidget gridWidget = this.createGridWidget();
        
        GridWidget.Adder gridWidgetAdder = gridWidget.createAdder(1);
        gridWidgetAdder.add(this.settingsBox);
        
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }
}

