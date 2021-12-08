package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.BuiltinRegistries;

public class InfClimateWorldScreen extends InfWorldScreen {
    private static final String SAMPLE_CLIMATE_DISPLAY_STRING = "createWorld.customize.inf.sampleClimate";
    private static final String SAMPLE_CLIMATE_TOOLTIP = "createWorld.customize.inf.sampleClimate.tooltip";

    protected InfClimateWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    public static InfClimateWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new InfClimateWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putCompound(worldSetting, settings.getNbt()),
            new Settings(worldScreen.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        String worldType = NbtUtil.toStringOrThrow(this.getSetting(NbtTags.WORLD_TYPE));
        String biomeType = NbtUtil.toStringOrThrow(this.worldSettings.getElement(WorldSetting.BIOME, NbtTags.BIOME_TYPE));
        
        boolean isSameBiomeType = Registries.WORLD.get(worldType).getBiomeProvider().equals(biomeType);
        boolean climateSampleable = Registries.BIOME
            .get(biomeType)
            .apply(0L, new NbtCompound(), BuiltinRegistries.BIOME) instanceof ClimateSampler;
        
        // Replace sampleClimate option depending on if climate sampler matches biome type
        if (isSameBiomeType)
            this.putSetting(NbtTags.SAMPLE_CLIMATE, NbtByte.of(isSameBiomeType));
        
        BooleanCyclingOptionWrapper sampleClimate = new BooleanCyclingOptionWrapper(
            SAMPLE_CLIMATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.SAMPLE_CLIMATE)),
            value -> this.putSetting(NbtTags.SAMPLE_CLIMATE, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(SAMPLE_CLIMATE_TOOLTIP), 200)
        );
        
        // Check if biome type is ClimateBiomeProvider
        // and if default climate scale has been changed
        this.addOption(sampleClimate, climateSampleable && !isSameBiomeType);
    }
}
