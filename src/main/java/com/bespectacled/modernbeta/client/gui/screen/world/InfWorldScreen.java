package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.client.gui.WorldSettings;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class InfWorldScreen extends WorldScreen {
    private static final String HYDROGEN_LOADED_STRING = "createWorld.customize.hydrogenLoaded";
    
    private static final String GENERATE_OCEANS_DISPLAY_STRING = "createWorld.customize.inf.generateOceans";
    private static final String GENERATE_OCEAN_SHRINES_DISPLAY_STRING = "createWorld.customize.inf.generateOceanShrines";
    private static final String GENERATE_MONUMENTS_DISPLAY_STRING = "createWorld.customize.inf.generateMonuments";
    private static final String GENERATE_DEEPSLATE_DISPLAY_STRING = "createWorld.customize.inf.generateDeepslate";
    
    private static final String GENERATE_OCEANS_TOOLTIP = "createWorld.customize.inf.generateOceans.tooltip";
    
    public InfWorldScreen(
        CreateWorldScreen parent,
        WorldSettings worldSettings,
        Consumer<WorldSettings> consumer
    ) {
        super(parent, worldSettings, consumer);
    } 

    @Override
    protected void init() {
        super.init();
        
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        
        boolean isHydrogenLoaded = Compat.isLoaded("hydrogen");
        boolean isSingleBiome = biomeType.equals(BuiltInTypes.Biome.SINGLE.name);
        boolean showDeepslateOption = this.worldProvider.showGenerateDeepslate();
        
        BooleanCyclingOptionWrapper generateOceans = new BooleanCyclingOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_OCEANS)),
            value -> this.putChunkSetting(NbtTags.GEN_OCEANS, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );
        
        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putChunkSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateMonuments = new BooleanCyclingOptionWrapper(
            GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putChunkSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateDeepslate = new BooleanCyclingOptionWrapper(
            GENERATE_DEEPSLATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_DEEPSLATE)),
            value -> this.putChunkSetting(NbtTags.GEN_DEEPSLATE, NbtByte.of(value))
        );
        
        TextOptionWrapper hydrogenText = new TextOptionWrapper(HYDROGEN_LOADED_STRING, Formatting.RED);
        
        if (isHydrogenLoaded && !isSingleBiome) {
            this.addOption(hydrogenText);
        }
        
        this.addOption(generateOceans, !isHydrogenLoaded && !isSingleBiome);
        this.addOption(generateOceanShrines, !isHydrogenLoaded && !isSingleBiome);
        this.addOption(generateMonuments, !isHydrogenLoaded && !isSingleBiome);
        this.addOption(generateDeepslate, showDeepslateOption);
    }
}
