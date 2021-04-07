package com.bespectacled.modernbeta.gui.biome;

import com.bespectacled.modernbeta.api.AbstractWorldScreenProvider;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleBiomeScreenProvider {
    public static CustomizeBuffetLevelScreen create(AbstractWorldScreenProvider screenProvider) {
        return new CustomizeBuffetLevelScreen(
            screenProvider, 
            screenProvider.getRegistryManager(),
            (biome) -> {
                screenProvider.setSingleBiome(screenProvider.getRegistryManager().<Biome>get(Registry.BIOME_KEY).getId(biome).toString());
                screenProvider.setBiomeProviderSettings("singleBiome", NbtString.of(screenProvider.getSingleBiome()));
            },
            screenProvider.getRegistryManager().<Biome>get(Registry.BIOME_KEY).get(new Identifier(screenProvider.getSingleBiome()))
        );
    }
}
