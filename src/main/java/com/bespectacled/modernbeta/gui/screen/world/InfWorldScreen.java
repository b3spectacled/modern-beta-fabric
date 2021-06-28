package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class InfWorldScreen extends WorldScreen {
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
        
        String biomeType = NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_BIOME));
        String singleBiome = NBTUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, WorldSettings.TAG_SINGLE_BIOME));
        
        boolean isSingleBiome = biomeType.equals(BuiltInTypes.Biome.SINGLE.name);
        boolean hasOceanShrine = this.registryManager
            .<Biome>get(Registry.BIOME_KEY)
            .get(new Identifier(singleBiome))
            .getGenerationSettings()
            .hasStructureFeature(OldStructures.OCEAN_SHRINE_STRUCTURE);
        
        BooleanCyclingOptionWrapper generateOceans = new BooleanCyclingOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateOceans"), ModernBeta.GEN_CONFIG.generateOceans),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "generateOceans", NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );
        
        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateOceanShrines"), ModernBeta.GEN_CONFIG.generateOceanShrines),
            value -> { 
                this.worldSettings.putChange(WorldSetting.CHUNK, "generateOceanShrines", NbtByte.of(value));
                
                this.client.openScreen(
                    this.worldProvider.createWorldScreen(
                        (CreateWorldScreen)this.parent,
                        this.worldSettings,
                        this.consumer
                ));
            }
        );
        
        if (!isSingleBiome) {
            this.addOption(generateOceans);
        }
        
        if (!isSingleBiome || hasOceanShrine) {
            this.addOption(generateOceanShrines);
        }
    }
}
