package mod.bespectacled.modernbeta.client.gui.screen.biome;

import mod.bespectacled.modernbeta.client.gui.screen.WorldScreen;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;

public class SingleBiomeScreen {
    public static CustomizeBuffetLevelScreen create(WorldScreen worldScreen, WorldSetting worldSetting) {
        return new CustomizeBuffetLevelScreen(
            worldScreen, 
            worldScreen.getRegistryManager(),
            biomeKey -> worldScreen.getWorldSettings().put(
                worldSetting, 
                NbtTags.SINGLE_BIOME, 
                NbtString.of(biomeKey.getKey().orElseGet(() -> BiomeKeys.PLAINS).getValue().toString())
            ),
            worldScreen
                .getRegistryManager()
                .get(Registry.BIOME_KEY)
                .getOrCreateEntry(
                    RegistryKey.of(
                        Registry.BIOME_KEY,
                        new Identifier(
                            NbtUtil.toStringOrThrow(worldScreen.getWorldSettings().get(worldSetting, NbtTags.SINGLE_BIOME))
                        )
                    )
                )
        );
    }
    
    
}
