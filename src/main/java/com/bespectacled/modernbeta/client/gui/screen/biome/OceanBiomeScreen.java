package com.bespectacled.modernbeta.client.gui.screen.biome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.SettingsScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanOptionWrapper;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.client.gui.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.nbt.NbtByte;
import net.minecraft.text.TranslatableText;

public abstract class OceanBiomeScreen extends SettingsScreen {
    private static final String GENERATE_OCEANS_DISPLAY_STRING = "createWorld.customize.biome.generateOceans";

    private static final String GENERATE_OCEANS_TOOLTIP = "createWorld.customize.biome.generateOceans.tooltip";

    protected OceanBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings settings) {
        super(parent, worldSetting, consumer, settings);
    }

    @Override
    protected void init() {
        super.init();

        BooleanOptionWrapper generateOceans = new BooleanOptionWrapper(
            GENERATE_OCEANS_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getSetting(NbtTags.GEN_OCEANS)),
            value -> this.putSetting(NbtTags.GEN_OCEANS, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(GENERATE_OCEANS_TOOLTIP), 200)
        );

        this.addOption(generateOceans);
    }
}