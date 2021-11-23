package com.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;

public class Infdev227WorldScreen extends InfWorldScreen {
    private static final String INFDEV_PYRAMID_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevPyramid";
    private static final String INFDEV_WALL_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevWall";

    protected Infdev227WorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    public static Infdev227WorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new Infdev227WorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().putChanges(worldSetting, settings.getNbt()),
            new Settings(worldScreen.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanOptionWrapper generateInfdevPyramid = new BooleanOptionWrapper(
            INFDEV_PYRAMID_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_INFDEV_PYRAMID)),
            value -> this.putSetting(NbtTags.GEN_INFDEV_PYRAMID, NbtByte.of(value))
        );
        
        BooleanOptionWrapper generateInfdevWall = new BooleanOptionWrapper(
            INFDEV_WALL_DISPLAY_STRING, 
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_INFDEV_WALL)),
            value -> this.putSetting(NbtTags.GEN_INFDEV_WALL, NbtByte.of(value))
        );

        this.addOption(generateInfdevPyramid);
        this.addOption(generateInfdevWall);
    }
}
