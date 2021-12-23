package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;

public class InfClimateWorldScreen extends InfWorldScreen {
    private static final String SAMPLE_CLIMATE_DISPLAY_STRING = "createWorld.customize.inf.sampleClimate";
    private static final String SAMPLE_CLIMATE_TOOLTIP = "createWorld.customize.inf.sampleClimate.tooltip";

    protected InfClimateWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings setting) {
        super(parent, worldSetting, consumer, setting);
    }
    
    protected InfClimateWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }

    public static InfClimateWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new InfClimateWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putCompound(worldSetting, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper sampleClimate = new BooleanCyclingOptionWrapper(
            SAMPLE_CLIMATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.SAMPLE_CLIMATE)),
            value -> this.putSetting(NbtTags.SAMPLE_CLIMATE, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(SAMPLE_CLIMATE_TOOLTIP), 200)
        );
        
        this.addOption(sampleClimate);
    }
}
