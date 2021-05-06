package com.bespectacled.modernbeta.gui.screen.biome;

import com.bespectacled.modernbeta.api.gui.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleBiomeScreen {
    public static CustomizeBuffetLevelScreen create(WorldScreen screenProvider) {
        return new CustomizeBuffetLevelScreen(
            screenProvider, 
            screenProvider.getRegistryManager(),
            biome -> screenProvider.getWorldSettings().putSetting(
                WorldSetting.BIOME,
                "singleBiome", 
                NbtString.of(screenProvider.getRegistryManager().<Biome>get(Registry.BIOME_KEY).getId(biome).toString())
            ),
            screenProvider
                .getRegistryManager()
                .<Biome>get(Registry.BIOME_KEY)
                .get(new Identifier(NBTUtil.readStringOrThrow("singleBiome", screenProvider.getWorldSettings().getSettings(WorldSetting.BIOME))))
        );
    }
}
