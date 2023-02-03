package mod.bespectacled.modernbeta.world.biome.injector;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

@FunctionalInterface
public interface BiomeInjectionResolver {
    public static final BiomeInjectionResolver DEFAULT = (biomeX, biomeY, biomeZ) -> null;
    
    public RegistryEntry<Biome> apply(int biomeX, int biomeY, int biomeZ);
}
