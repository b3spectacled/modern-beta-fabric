package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtInt;

public class VanillaBiomeScreen extends OceanBiomeScreen {
    private VanillaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }
    
    private VanillaBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        this(parent, worldSetting, consumer, new Settings(parent.getWorldSettings().getNbt(WorldSetting.BIOME)));
    }

    public static VanillaBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new VanillaBiomeScreen(
            parent,
            worldSetting,
            settings -> parent.getWorldSettings().putCompound(WorldSetting.BIOME, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        DoubleOptionWrapper<Integer> vanillaBiomeSize = new DoubleOptionWrapper<>(
            "createWorld.customize.vanilla.vanillaBiomeSize",
            "",
            1D, 8D, 1F,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.VANILLA_BIOME_SIZE)),
            value -> this.putSetting(NbtTags.VANILLA_BIOME_SIZE, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> vanillaOceanBiomeSize = new DoubleOptionWrapper<>(
            "createWorld.customize.vanilla.vanillaOceanBiomeSize",
            "",
            1D, 8D, 1F,
            () -> NbtUtil.toIntOrThrow(this.getSetting(NbtTags.VANILLA_OCEAN_BIOME_SIZE)),
            value -> this.putSetting(NbtTags.VANILLA_OCEAN_BIOME_SIZE, NbtInt.of(value.intValue()))
        );
        
        this.addOption(vanillaBiomeSize);
        this.addOption(vanillaOceanBiomeSize);
    }
}
