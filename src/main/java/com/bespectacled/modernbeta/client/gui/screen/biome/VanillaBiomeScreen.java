package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;

public class VanillaBiomeScreen extends SettingsScreen {
    private static final String LARGE_BIOMES_DISPLAY_STRING = "createWorld.customize.biome.largeBiomes";
    
    private VanillaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }
    
    private VanillaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(worldSetting)));
    }
    
    public static VanillaBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new VanillaBiomeScreen(
            parent,
            worldSetting,
            settings -> parent.getWorldSettings().putChanges(worldSetting, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
     
        BooleanCyclingOptionWrapper largeBiomes = new BooleanCyclingOptionWrapper(
            LARGE_BIOMES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.LARGE_BIOMES)),
            value -> this.putSetting(NbtTags.LARGE_BIOMES, NbtByte.of(value))
        );
        
        this.addOption(largeBiomes);
    }
}
