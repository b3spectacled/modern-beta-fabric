package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class InfWorldScreen extends WorldScreen {
    private static final String HYDROGEN_LOADED_STRING = "createWorld.customize.hydrogenLoaded";
    
    private static final String GENERATE_OCEANS_DISPLAY_STRING = "createWorld.customize.inf.generateOceans";
    private static final String GENERATE_OCEAN_SHRINES_DISPLAY_STRING = "createWorld.customize.inf.generateOceanShrines";
    
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
        boolean isSingleBiomeAndHasOceanShrine = isSingleBiome ? 
            this.registryManager
            .<Biome>get(Registry.BIOME_KEY)
            .get(new Identifier(NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.SINGLE_BIOME))))
            .getGenerationSettings()
            .hasStructureFeature(OldStructures.OCEAN_SHRINE_STRUCTURE) : 
            false;
        
        BooleanOptionWrapper generateOceans = new BooleanOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_OCEANS)),
            value -> this.putChunkSetting(NbtTags.GEN_OCEANS, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );
        
        BooleanOptionWrapper generateOceanShrines = new BooleanOptionWrapper(
            GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getChunkSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putChunkSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        TextOptionWrapper hydrogenText = new TextOptionWrapper(HYDROGEN_LOADED_STRING, Formatting.RED);
        
        if (isHydrogenLoaded && !isSingleBiome) {
            this.addOption(hydrogenText);
        }
        
        if (!isHydrogenLoaded && !isSingleBiome) {
            this.addOption(generateOceans);
        }
        
        if (!isHydrogenLoaded && (!isSingleBiome || isSingleBiomeAndHasOceanShrine)) {
            this.addOption(generateOceanShrines);
        }
    }
}
