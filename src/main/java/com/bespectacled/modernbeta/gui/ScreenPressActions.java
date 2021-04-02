package com.bespectacled.modernbeta.gui;

import java.util.function.Function;

import com.bespectacled.modernbeta.api.AbstractLevelScreenProvider;
import com.bespectacled.modernbeta.gui.biome.BetaCustomizeBiomesScreen;
import com.bespectacled.modernbeta.gui.biome.VanillaCustomizeBiomesScreen;
import com.bespectacled.modernbeta.mixin.client.MixinScreenAccessor;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class ScreenPressActions {
    public static final Function<AbstractLevelScreenProvider, ButtonWidget.PressAction> BETA = (screen) ->
        buttonWidget -> ((MixinScreenAccessor)screen).getClient().openScreen(
            new BetaCustomizeBiomesScreen(
                    screen,
                    screen.getRegistryManager(),
                    screen.getBiomeProviderSettings(),
                    betaBiomeSettings -> screen.setBiomeProviderSettings(betaBiomeSettings)
        ));
            
    public static final Function<AbstractLevelScreenProvider, ButtonWidget.PressAction> SINGLE = (screen) ->
        buttonWidget -> ((MixinScreenAccessor)screen).getClient().openScreen(
            new CustomizeBuffetLevelScreen(
                    screen, 
                    screen.getRegistryManager(),
                    (biome) -> {
                        screen.setSingleBiome(screen.getRegistryManager().<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                        screen.setBiomeProviderSettings("singleBiome", NbtString.of(screen.getSingleBiome()));
                    },
                    screen.getRegistryManager().<Biome>get(Registry.BIOME_KEY).get(new Identifier(screen.getSingleBiome()))
        ));
            
    public static final Function<AbstractLevelScreenProvider, ButtonWidget.PressAction> VANILLA = (screen) ->
        buttonWidget -> ((MixinScreenAccessor)screen).getClient().openScreen(
            new VanillaCustomizeBiomesScreen(
                    screen,
                    screen.getRegistryManager(),
                    screen.getBiomeProviderSettings(),
                    vanillaBiomeSettings -> screen.setBiomeProviderSettings(vanillaBiomeSettings)
        ));
}
