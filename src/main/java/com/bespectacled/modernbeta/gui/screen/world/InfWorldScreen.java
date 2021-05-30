package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.nbt.NbtByte;

public class InfWorldScreen extends WorldScreen {
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
        
        String biomeType = NBTUtil.readStringOrThrow(WorldSettings.TAG_BIOME, this.worldSettings.getSettings(WorldSetting.BIOME));
        
        CyclingOption<Boolean> generateOceans = 
            CyclingOption.create(
                "createWorld.customize.inf.generateOceans",
                (gameOptions) -> NBTUtil.readBoolean("generateOceans", this.worldSettings.getSettings(WorldSetting.CHUNK), ModernBeta.GEN_CONFIG.generateOceans), 
                (gameOptions, option, value) -> this.worldSettings.putSetting(WorldSetting.CHUNK, "generateOceans", NbtByte.of(value))
            );
        
        if (!biomeType.equals(BuiltInTypes.Biome.SINGLE.name)) {
            buttonList.addSingleOptionEntry(generateOceans);
        }

    }
}
