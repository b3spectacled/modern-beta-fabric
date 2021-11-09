package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtInt;

public class VanillaBiomeScreen extends BiomeScreen {
    private VanillaBiomeScreen(WorldScreen parent, Consumer<Settings> consumer) {
        super(parent, consumer);
    }

    public static VanillaBiomeScreen create(WorldScreen worldScreen) {
        return new VanillaBiomeScreen(
            worldScreen,
            settings -> worldScreen.getWorldSettings().putChanges(WorldSetting.BIOME, settings.getNbt())
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        DoubleOptionWrapper<Integer> vanillaBiomeSize = new DoubleOptionWrapper<>(
            "createWorld.customize.vanilla.vanillaBiomeSize",
            "",
            1D, 8D, 1F,
            () -> NbtUtil.toIntOrThrow(this.biomeSettings.getSetting(NbtTags.VANILLA_BIOME_SIZE)),
            value -> this.biomeSettings.putChange(NbtTags.VANILLA_BIOME_SIZE, NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> vanillaOceanBiomeSize = new DoubleOptionWrapper<>(
            "createWorld.customize.vanilla.vanillaOceanBiomeSize",
            "",
            1D, 8D, 1F,
            () -> NbtUtil.toIntOrThrow(this.biomeSettings.getSetting(NbtTags.VANILLA_OCEAN_BIOME_SIZE)),
            value -> this.biomeSettings.putChange(NbtTags.VANILLA_OCEAN_BIOME_SIZE, NbtInt.of(value.intValue()))
        );
        
        this.addOption(vanillaBiomeSize);
        this.addOption(vanillaOceanBiomeSize);
    }
}
