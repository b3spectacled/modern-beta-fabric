package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.BiConsumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.WorldScreenProvider;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.gui.TextOption;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.DynamicRegistryManager;

public class InfWorldScreenProvider extends WorldScreenProvider {
    public InfWorldScreenProvider(
        CreateWorldScreen parent, 
        DynamicRegistryManager registryManager,
        NbtCompound chunkProviderSettings,
        NbtCompound biomeProviderSettings,
        BiConsumer<NbtCompound, NbtCompound> consumer
    ) {
        super(parent, registryManager, chunkProviderSettings, biomeProviderSettings, consumer);
    }

    @Override
    protected void init() {
        super.init();
        
        String biomeType = NBTUtil.readStringOrThrow("biomeType", this.biomeProviderSettings);
        
        CyclingOption<Boolean> generateOceans = 
            CyclingOption.create("createWorld.customize.inf.generateOceans",
                (gameOptions) -> NBTUtil.readBoolean("generateOceans", this.chunkProviderSettings, ModernBeta.GEN_CONFIG.generateOceans), 
                (gameOptions, option, value) -> { // Setter
                    this.chunkProviderSettings.putBoolean("generateOceans", value);
            });
        
        if (!biomeType.equals(BuiltInTypes.Biome.SINGLE.name)) {
            buttonList.addSingleOptionEntry(generateOceans);
        }

        if (!(this instanceof Infdev227WorldScreenProvider) && !(this instanceof IslandWorldScreenProvider))
            this.buttonList.addSingleOptionEntry(new TextOption("Note: Settings are not final and may change."));

    }
}
