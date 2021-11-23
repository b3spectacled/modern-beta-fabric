package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.world.structure.OldStructures;

import net.minecraft.nbt.NbtByte;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.StructureFeature;

public class InfWorldScreen extends SettingsScreen {
    private static final String HYDROGEN_LOADED_STRING = "createWorld.customize.hydrogenLoaded";
    
    private static final String GENERATE_OCEAN_SHRINES_DISPLAY_STRING = "createWorld.customize.inf.generateOceanShrines";
    private static final String GENERATE_MONUMENTS_DISPLAY_STRING = "createWorld.customize.inf.generateMonuments";

    protected InfWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    public static InfWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new InfWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putChanges(worldSetting, settings.getNbt()),
            new Settings(worldScreen.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        String biomeType = NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.BIOME_TYPE));
        
        boolean isHydrogenLoaded = Compat.isLoaded("hydrogen");
        boolean isSingleBiome = biomeType.equals(BuiltInTypes.Biome.SINGLE.name);
        boolean isSingleBiomeAndHasOceanShrine = isSingleBiome ? 
            this.registryManager
            .<Biome>get(Registry.BIOME_KEY)
            .get(new Identifier(NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.SINGLE_BIOME))))
            .getGenerationSettings()
            .hasStructureFeature(OldStructures.OCEAN_SHRINE_STRUCTURE) : 
            false;
        boolean isSingleBiomeAndHasMonument = isSingleBiome ? 
            this.registryManager
            .<Biome>get(Registry.BIOME_KEY)
            .get(new Identifier(NbtUtil.toStringOrThrow(this.worldSettings.getSetting(WorldSetting.BIOME, NbtTags.SINGLE_BIOME))))
            .getGenerationSettings()
            .hasStructureFeature(StructureFeature.MONUMENT) : 
            false;
        
        BooleanOptionWrapper generateOceanShrines = new BooleanOptionWrapper(
            GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanOptionWrapper generateMonuments = new BooleanOptionWrapper(
            GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );
        
        TextOptionWrapper hydrogenText = new TextOptionWrapper(HYDROGEN_LOADED_STRING, Formatting.RED);
        
        if (isHydrogenLoaded && !isSingleBiome)
            this.addOption(hydrogenText);
        
        this.addOption(generateOceanShrines, (!isHydrogenLoaded && !isSingleBiome) || isSingleBiomeAndHasOceanShrine);
        this.addOption(generateMonuments, (!isHydrogenLoaded && !isSingleBiome) || isSingleBiomeAndHasMonument);
    }
}
