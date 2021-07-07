package com.bespectacled.modernbeta.client.gui.screen.biome;

import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleBiomeScreen {
    public static CustomizeBuffetLevelScreen create(WorldScreen worldScreen) {
        return new CustomizeBuffetLevelScreen(
            worldScreen, 
            worldScreen.getRegistryManager(),
            biome -> worldScreen.getWorldSettings().putChange(
                WorldSetting.BIOME, 
                WorldSettings.TAG_SINGLE_BIOME, 
                NbtString.of(worldScreen.getRegistryManager().<Biome>get(Registry.BIOME_KEY).getId(biome).toString())
            ),
            worldScreen
                .getRegistryManager()
                .<Biome>get(Registry.BIOME_KEY)
                .get(new Identifier(NBTUtil.toStringOrThrow(worldScreen.getWorldSettings().getSetting(WorldSetting.BIOME, WorldSettings.TAG_SINGLE_BIOME))))
        );
    }
}
