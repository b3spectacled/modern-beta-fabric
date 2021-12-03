package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;
import net.minecraft.util.Formatting;

public class InfWorldScreen extends SettingsScreen {
    private static final String HYDROGEN_LOADED_STRING = "createWorld.customize.hydrogenLoaded";
    
    private static final String GENERATE_OCEAN_SHRINES_DISPLAY_STRING = "createWorld.customize.inf.generateOceanShrines";
    private static final String GENERATE_MONUMENTS_DISPLAY_STRING = "createWorld.customize.inf.generateMonuments";
    private static final String GENERATE_DEEPSLATE_DISPLAY_STRING = "createWorld.customize.inf.generateDeepslate";

    protected InfWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    public static InfWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new InfWorldScreen(
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
        
        boolean isHydrogenLoaded = Compat.isLoaded("hydrogen");
        boolean isSingleBiome = biomeType.equals(BuiltInTypes.Biome.SINGLE.name);
        boolean showDeepslateOption = Registries.WORLD.get(worldType).showGenerateDeepslate();
        
        /*
        boolean isSingleBiomeAndHasOceanShrine = isSingleBiome ? 
            this.registryManager
            .<Biome>get(Registry.BIOME_KEY)
            .get(new Identifier(NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.SINGLE_BIOME))))
            .getGenerationSettings()
            .hasStructureFeature(OldStructures.OCEAN_SHRINE_STRUCTURE) : 
            false;
        */

        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateMonuments = new BooleanCyclingOptionWrapper(
            GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateDeepslate = new BooleanCyclingOptionWrapper(
            GENERATE_DEEPSLATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_DEEPSLATE)),
            value -> this.putSetting(NbtTags.GEN_DEEPSLATE, NbtByte.of(value))
        );
        
        TextOptionWrapper hydrogenText = new TextOptionWrapper(HYDROGEN_LOADED_STRING, Formatting.RED);
        
        if (isHydrogenLoaded && !isSingleBiome)
            this.addOption(hydrogenText);
        
        this.addOption(generateOceanShrines, !isHydrogenLoaded && !isSingleBiome);
        this.addOption(generateMonuments, !isHydrogenLoaded && !isSingleBiome);
        this.addOption(generateDeepslate, showDeepslateOption);
    }
}
