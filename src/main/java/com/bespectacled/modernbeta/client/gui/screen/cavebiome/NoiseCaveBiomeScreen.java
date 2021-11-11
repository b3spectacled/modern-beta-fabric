package com.bespectacled.modernbeta.client.gui.screen.cavebiome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtInt;

public class NoiseCaveBiomeScreen extends SettingsScreen {
    private static final String VERTICAL_SCALE_DISPLAY_STRING = "createWorld.customize.caveBiome.verticalNoiseScale";
    private static final String HORIZONTAL_SCALE_DISPLAY_STRING = "createWorld.customize.caveBiome.horizontalNoiseScale";
    
    private NoiseCaveBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings) {
        super(parent, worldSetting, consumer, biomeSettings);
    }
    
    public static NoiseCaveBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new NoiseCaveBiomeScreen(
            parent, 
            worldSetting, 
            settings -> parent.getWorldSettings().putChanges(worldSetting, settings.getNbt()),
            new Settings(parent.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        DoubleOptionWrapper<Integer> verticalScaleOption = new DoubleOptionWrapper<>(
            VERTICAL_SCALE_DISPLAY_STRING,
            1D, 64D, 1f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.VERTICAL_NOISE_SCALE)),
            value -> this.putSetting(NbtTags.VERTICAL_NOISE_SCALE, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> horizontalScaleOption = new DoubleOptionWrapper<>(
            HORIZONTAL_SCALE_DISPLAY_STRING,
            1D, 64D, 1f,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.HORIZONTAL_NOISE_SCALE)),
            value -> this.putSetting(NbtTags.HORIZONTAL_NOISE_SCALE, NbtInt.of(value.intValue()))
        );
        
        this.addOption(verticalScaleOption);
        this.addOption(horizontalScaleOption);
    }
}
