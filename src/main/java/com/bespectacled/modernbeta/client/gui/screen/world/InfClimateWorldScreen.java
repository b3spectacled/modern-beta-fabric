package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.api.world.biome.ClimateBiomeProvider;
import com.bespectacled.modernbeta.client.gui.WorldSettings;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.BuiltinRegistries;

public class InfClimateWorldScreen extends InfWorldScreen {
    private static final String SAMPLE_CLIMATE_DISPLAY_STRING = "createWorld.customize.inf.sampleClimate";
    private static final String SAMPLE_CLIMATE_TOOLTIP = "createWorld.customize.inf.sampleClimate.tooltip";
    
    public InfClimateWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
        
        // Replace sampleClimate option depending on if climate sampler matches biome type
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        boolean isSameBiomeType = this.worldProvider.getBiomeProvider().equals(biomeType);
        
        this.putChunkSetting(NbtTags.SAMPLE_CLIMATE, NbtByte.of(isSameBiomeType));
    } 

    @Override
    protected void init() {
        super.init();
        
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        
        boolean isSameBiomeType = this.worldProvider.getBiomeProvider().equals(biomeType);
        boolean climateSampleable = Registries.BIOME
            .get(biomeType)
            .apply(0L, new NbtCompound(), BuiltinRegistries.BIOME) instanceof ClimateBiomeProvider;
        
        BooleanCyclingOptionWrapper sampleClimate = new BooleanCyclingOptionWrapper(
            SAMPLE_CLIMATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.SAMPLE_CLIMATE)),
            value -> this.putChunkSetting(NbtTags.SAMPLE_CLIMATE, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(SAMPLE_CLIMATE_TOOLTIP), 200)
        );
        
        // Check if biome type is ClimateBiomeProvider
        // and if default climate scale has been changed
        this.addOption(sampleClimate, climateSampleable && !isSameBiomeType);
    }
}
