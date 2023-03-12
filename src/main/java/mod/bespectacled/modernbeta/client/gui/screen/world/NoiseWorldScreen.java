package mod.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import mod.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import mod.bespectacled.modernbeta.util.GUITags;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import net.minecraft.nbt.NbtByte;

public class NoiseWorldScreen extends SettingsScreen {
    private static final String GENERATE_NOISE_CAVES_DISPLAY_STRING = "createWorld.customize.noise.generateNoiseCaves";
    private static final String GENERATE_NOODLE_CAVES_DISPLAY_STRING = "createWorld.customize.noise.generateNoodleCaves";

    protected NoiseWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings setting) {
        super(parent, worldSetting, consumer, setting);
    }
    
    protected NoiseWorldScreen(WorldScreen worldScreen, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(worldScreen, worldSetting, consumer);
    }
    
    public static NoiseWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new NoiseWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().replace(worldSetting, settings)
        );
    }

    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper generateNoiseCaves = new BooleanCyclingOptionWrapper(
            GENERATE_NOISE_CAVES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_NOISE_CAVES)),
            value -> this.putSetting(NbtTags.GEN_NOISE_CAVES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateNoodleCaves = new BooleanCyclingOptionWrapper(
            GENERATE_NOODLE_CAVES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_NOODLE_CAVES)),
            value -> this.putSetting(NbtTags.GEN_NOODLE_CAVES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateDeepslate = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_DEEPSLATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_DEEPSLATE)),
            value -> this.putSetting(NbtTags.GEN_DEEPSLATE, NbtByte.of(value))
        );
        
        this.addOption(generateNoiseCaves);
        this.addOption(generateNoodleCaves);
        this.addOption(generateDeepslate);
    }
}
