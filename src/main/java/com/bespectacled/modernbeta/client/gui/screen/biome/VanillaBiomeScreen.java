package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;

public class VanillaBiomeScreen extends OceanBiomeScreen {
    private static final String LARGE_BIOMES_DISPLAY_STRING = "createWorld.customize.biome.largeBiomes";
    
    private VanillaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }
    
    public static VanillaBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new VanillaBiomeScreen(
            parent,
            worldSetting,
            settings -> parent.getWorldSettings().putCompound(worldSetting, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
     
        BooleanCyclingOptionWrapper largeBiomes = new BooleanCyclingOptionWrapper(
            LARGE_BIOMES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.VANILLA_LARGE_BIOMES)),
            value -> this.putSetting(NbtTags.VANILLA_LARGE_BIOMES, NbtByte.of(value))
        );
        
        this.addOption(largeBiomes);
    }
}
