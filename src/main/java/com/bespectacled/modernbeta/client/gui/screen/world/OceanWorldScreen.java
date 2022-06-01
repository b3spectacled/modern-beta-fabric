package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.util.GUITags;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;

import net.minecraft.nbt.NbtByte;

public abstract class OceanWorldScreen extends NoiseWorldScreen {
    protected OceanWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings setting) {
        super(parent, worldSetting, consumer, setting);
    }
    
    protected OceanWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateMonuments = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );
        
        this.addOption(generateOceanShrines);
        this.addOption(generateMonuments);
    }
}
