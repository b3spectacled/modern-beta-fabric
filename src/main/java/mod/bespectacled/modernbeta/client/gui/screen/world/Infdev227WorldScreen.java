package mod.bespectacled.modernbeta.client.gui.screen.world;

import java.util.function.Consumer;

import mod.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.client.gui.wrapper.BooleanCyclingOptionWrapper;
import mod.bespectacled.modernbeta.util.GUITags;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import net.minecraft.nbt.NbtByte;

public class Infdev227WorldScreen extends SettingsScreen {
    private static final String INFDEV_PYRAMID_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevPyramid";
    private static final String INFDEV_WALL_DISPLAY_STRING = "createWorld.customize.infdev.generateInfdevWall";

    protected Infdev227WorldScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer) {
        super(parent, worldSetting, consumer);
    }

    public static Infdev227WorldScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new Infdev227WorldScreen(
            worldScreen,
            worldSetting,
            settings -> worldScreen.getWorldSettings().replace(worldSetting, settings)
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        BooleanCyclingOptionWrapper generateDeepslate = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_DEEPSLATE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_DEEPSLATE)),
            value -> this.putSetting(NbtTags.GEN_DEEPSLATE, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateOceanShrines = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_OCEAN_SHRINES_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEAN_SHRINES)),
            value -> this.putSetting(NbtTags.GEN_OCEAN_SHRINES, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateMonuments = new BooleanCyclingOptionWrapper(
            GUITags.GENERATE_MONUMENTS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_MONUMENTS)),
            value -> this.putSetting(NbtTags.GEN_MONUMENTS, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateInfdevPyramid = new BooleanCyclingOptionWrapper(
            INFDEV_PYRAMID_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_INFDEV_PYRAMID)),
            value -> this.putSetting(NbtTags.GEN_INFDEV_PYRAMID, NbtByte.of(value))
        );
        
        BooleanCyclingOptionWrapper generateInfdevWall = new BooleanCyclingOptionWrapper(
            INFDEV_WALL_DISPLAY_STRING, 
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_INFDEV_WALL)),
            value -> this.putSetting(NbtTags.GEN_INFDEV_WALL, NbtByte.of(value))
        );
        
        this.addOption(generateDeepslate);
        
        this.addOption(generateOceanShrines);
        this.addOption(generateMonuments);
        
        this.addOption(generateInfdevPyramid);
        this.addOption(generateInfdevWall);
    }
}
