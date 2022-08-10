package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.wrapper.CyclingOptionWrapper;
import com.bespectacled.modernbeta.util.GUITags;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtString;

public class NoiseWorldScreen extends SettingsScreen {
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
        
        BooleanCyclingOptionWrapper generateDeepslate = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_DEEPSLATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_DEEPSLATE)),
            value -> this.putSetting(NbtTags.GEN_DEEPSLATE, NbtByte.of(value))
        );
        
        CyclingOptionWrapper<String> noisePostProcessorOption = new CyclingOptionWrapper<String>(
            GUITags.NOISE_POST_PROCESSOR_DISPLAY_STRING,
            Registries.NOISE_POST_PROCESSORS.getKeySet().stream().toArray(String[]::new),
            () -> NbtUtil.toStringOrThrow(this.getSetting(NbtTags.NOISE_POST_PROCESSOR)),
            value -> this.putSetting(NbtTags.NOISE_POST_PROCESSOR, NbtString.of(value))
        );

        boolean onlyDefaultProcessor = Registries.NOISE_POST_PROCESSORS.getKeySet().size() == 1;
        
        this.addOption(generateDeepslate);
        this.addOption(noisePostProcessorOption, !onlyDefaultProcessor);
    }
}
