package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.registry.Registries;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.client.gui.wrapper.TextOptionWrapper;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.settings.Settings;
import com.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;

import net.minecraft.nbt.NbtByte;
import net.minecraft.util.Formatting;

public class InfWorldScreen extends SettingsScreen {
    private static final String HYDROGEN_LOADED_STRING = "createWorld.customize.hydrogenLoaded";
    
    private static final String GENERATE_OCEAN_SHRINES_DISPLAY_STRING = "createWorld.customize.inf.generateOceanShrines";
    private static final String GENERATE_MONUMENTS_DISPLAY_STRING = "createWorld.customize.inf.generateMonuments";
    private static final String GENERATE_DEEPSLATE_DISPLAY_STRING = "createWorld.customize.inf.generateDeepslate";
    
    protected InfWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings setting) {
        super(parent, worldSetting, consumer, setting);
    }

    protected InfWorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }

    public static InfWorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new InfWorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().replace(worldSetting, settings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        String worldType = NbtUtil.toStringOrThrow(this.getSetting(NbtTags.WORLD_TYPE));
        
        boolean isHydrogenLoaded = Compat.isLoaded("hydrogen");
        boolean showDeepslateOption = Registries.WORLD.get(worldType).showGenerateDeepslate();

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
        
        TextOptionWrapper hydrogenText = new TextOptionWrapper(HYDROGEN_LOADED_STRING).formatting(Formatting.RED);
        
        if (isHydrogenLoaded)
            this.addOption(hydrogenText);
        
        this.addOption(generateOceanShrines, !isHydrogenLoaded);
        this.addOption(generateMonuments, !isHydrogenLoaded);
        this.addOption(generateDeepslate, showDeepslateOption);
    }
}
