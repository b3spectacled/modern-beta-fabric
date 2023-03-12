package mod.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import mod.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;

public abstract class OceanBiomeScreen extends SettingsScreen {
    private static final String GENERATE_OCEANS_DISPLAY_STRING = "createWorld.customize.biome.generateOceans";
    private static final String GENERATE_OCEANS_TOOLTIP = "createWorld.customize.biome.generateOceans.tooltip";
    
    protected OceanBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper generateOceans = new BooleanCyclingOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEANS)),
            value -> this.putSetting(NbtTags.GEN_OCEANS, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );
        
        this.addOption(generateOceans);
    }
}
