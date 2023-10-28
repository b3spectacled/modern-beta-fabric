package mod.bespectacled.modernbeta.api.world.biome;

import mod.bespectacled.modernbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public interface BiomeResolverInfo {
     /**
     * Gets a biome and if it is hilly at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     *
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     *
     * @return A biome at given biome coordinates.
     */
    BiomeInfo getBiomeInfo(int biomeX, int biomeY, int biomeZ);
}
