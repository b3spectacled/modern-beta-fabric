package com.bespectacled.modernbeta.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.gui.wrapper.DoubleOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.gui.Settings;
import com.bespectacled.modernbeta.util.NBTUtil;
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
            () -> NBTUtil.toInt(this.biomeSettings.getSetting("vanillaBiomeSize"), ModernBeta.BIOME_CONFIG.vanillaBiomeSize),
            value -> this.biomeSettings.putChange("vanillaBiomeSize", NbtInt.of(value.intValue()))
        );
        
        DoubleOptionWrapper<Integer> vanillaOceanBiomeSize = new DoubleOptionWrapper<>(
            "createWorld.customize.vanilla.vanillaOceanBiomeSize",
            "",
            1D, 8D, 1F,
            () -> NBTUtil.toInt(this.biomeSettings.getSetting("vanillaOceanBiomeSize"), ModernBeta.BIOME_CONFIG.vanillaOceanBiomeSize),
            value -> this.biomeSettings.putChange("vanillaOceanBiomeSize", NbtInt.of(value.intValue()))
        );
        
        this.addOption(vanillaBiomeSize);
        this.addOption(vanillaOceanBiomeSize);
    }
}
