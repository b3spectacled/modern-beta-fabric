package com.bespectacled.modernbeta.gui.screen.world;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.registry.BuiltInTypes;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;

public class InfWorldScreen extends WorldScreen {
    private static final String GENERATE_OCEANS_DISPLAY_STRING = "createWorld.customize.inf.generateOceans";
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
        
        BooleanCyclingOptionWrapper generateOceans = new BooleanCyclingOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NBTUtil.toBoolean(this.worldSettings.getSetting(WorldSetting.CHUNK, "generateOceans"), ModernBeta.GEN_CONFIG.generateOceans),
            value -> this.worldSettings.putChange(WorldSetting.CHUNK, "generateOceans", NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );
        
        if (!biomeType.equals(BuiltInTypes.Biome.SINGLE.name)) {
            this.addOption(generateOceans);
        }
    }
}
