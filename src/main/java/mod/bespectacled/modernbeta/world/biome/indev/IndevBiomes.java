package mod.bespectacled.modernbeta.world.biome.indev;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class IndevBiomes {
    protected static final boolean ADD_LAKES = false;
    protected static final boolean ADD_SPRINGS = false;
    
    public static final RegistryKey<Biome> INDEV_NORMAL_KEY = ModernBetaBiomes.register(ModernBeta.createId("indev_normal"));
    public static final RegistryKey<Biome> INDEV_HELL_KEY = ModernBetaBiomes.register(ModernBeta.createId("indev_hell"));
    public static final RegistryKey<Biome> INDEV_PARADISE_KEY = ModernBetaBiomes.register(ModernBeta.createId("indev_paradise"));
    public static final RegistryKey<Biome> INDEV_WOODS_KEY = ModernBetaBiomes.register(ModernBeta.createId("indev_woods"));
    public static final RegistryKey<Biome> INDEV_SNOWY_KEY = ModernBetaBiomes.register(ModernBeta.createId("indev_snowy"));

    public static void registerBiomes() {
        ModernBetaBiomes.register(INDEV_NORMAL_KEY, IndevNormal.BIOME);
        ModernBetaBiomes.register(INDEV_HELL_KEY, IndevHell.BIOME);
        ModernBetaBiomes.register(INDEV_PARADISE_KEY, IndevParadise.BIOME);
        ModernBetaBiomes.register(INDEV_WOODS_KEY, IndevWoods.BIOME);
        ModernBetaBiomes.register(INDEV_SNOWY_KEY, IndevSnowy.BIOME);
    }
}
