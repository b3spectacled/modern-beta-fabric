package com.bespectacled.modernbeta.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.BiomeScreen;
import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.DynamicRegistryManager;

public class VanillaBiomeScreen extends BiomeScreen {
    private VanillaBiomeScreen(
        WorldScreen parent, 
        DynamicRegistryManager registryManager, 
        NbtCompound parentProviderSettings,
        Consumer<NbtCompound> consumer
    ) {
        super(parent, registryManager, parentProviderSettings, consumer);
    }

    public static VanillaBiomeScreen create(WorldScreen screenProvider) {
        return new VanillaBiomeScreen(
            screenProvider, 
            screenProvider.getRegistryManager(), 
            screenProvider.getWorldSettings().getSettings(WorldSetting.BIOME),
            biomeProviderSettings -> screenProvider.getWorldSettings().copySettingsFrom(WorldSetting.BIOME, biomeProviderSettings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        DoubleOption vanillaBiomeSize = 
            new DoubleOption(
                "createWorld.customize.vanilla.vanillaBiomeSizeSlider", 
                1D, 8D, 1F,
                (gameOptions) -> (double)NBTUtil.readInt("vanillaBiomeSize", this.biomeProviderSettings, ModernBeta.BETA_CONFIG.biome_config.vanillaBiomeSize),
                (gameOptions, value) -> this.biomeProviderSettings.putInt("vanillaBiomeSize", value.intValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.vanilla.biomeSize"), 
                            Text.of(String.valueOf(NBTUtil.readInt("vanillaBiomeSize", this.biomeProviderSettings, ModernBeta.BETA_CONFIG.biome_config.vanillaBiomeSize))) 
                    });
                }
            );
        
        DoubleOption vanillaOceanBiomeSize =
            new DoubleOption(
                "createWorld.customize.indev.vanillaOceanBiomeSizeSlider", 
                1D, 8D, 1F,
                (gameOptions) -> (double)NBTUtil.readInt("vanillaOceanBiomeSize", this.biomeProviderSettings, ModernBeta.BETA_CONFIG.biome_config.vanillaOceanBiomeSize), // Getter
                (gameOptions, value) -> this.biomeProviderSettings.putInt("vanillaOceanBiomeSize", value.intValue()),
                (gameOptions, doubleOptions) -> {
                    return new TranslatableText(
                        "options.generic_value", 
                        new Object[] { 
                            new TranslatableText("createWorld.customize.vanilla.oceanBiomeSize"), 
                            Text.of(String.valueOf(NBTUtil.readInt("vanillaOceanBiomeSize", this.biomeProviderSettings, ModernBeta.BETA_CONFIG.biome_config.vanillaOceanBiomeSize))) 
                    });
                }
            );

        this.buttonList.addSingleOptionEntry(vanillaBiomeSize);
        this.buttonList.addSingleOptionEntry(vanillaOceanBiomeSize);
    }
}
