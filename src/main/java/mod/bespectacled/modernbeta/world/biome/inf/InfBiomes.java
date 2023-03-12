package mod.bespectacled.modernbeta.world.biome.inf;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.OldBiomes;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class InfBiomes {
    protected static final boolean ADD_LAKES_ALPHA = false;
    protected static final boolean ADD_SPRINGS_ALPHA = true;
    
    protected static final boolean ADD_ALTERNATE_STONES_ALPHA = false;
    protected static final boolean ADD_NEW_MINEABLES_ALPHA = false;

    protected static final boolean ADD_LAKES_INF_611 = false;
    protected static final boolean ADD_SPRINGS_INF_611 = false;
    
    protected static final boolean ADD_LAKES_INF_420 = false;
    protected static final boolean ADD_SPRINGS_INF_420 = false;
    
    protected static final boolean ADD_LAKES_INF_415 = false;
    protected static final boolean ADD_SPRINGS_INF_415 = false;
    
    protected static final boolean ADD_LAKES_INF_227 = false;
    protected static final boolean ADD_SPRINGS_INF_227 = false;
    
    public static final RegistryKey<Biome> ALPHA_KEY = OldBiomes.register(ModernBeta.createId("alpha"));
    public static final RegistryKey<Biome> ALPHA_WINTER_KEY = OldBiomes.register(ModernBeta.createId("alpha_winter"));
    
    public static final RegistryKey<Biome> INFDEV_611_KEY = OldBiomes.register(ModernBeta.createId("infdev_611"));
    public static final RegistryKey<Biome> INFDEV_611_WINTER_KEY = OldBiomes.register(ModernBeta.createId("infdev_611_winter"));

    public static final RegistryKey<Biome> INFDEV_420_KEY = OldBiomes.register(ModernBeta.createId("infdev_420"));
    public static final RegistryKey<Biome> INFDEV_420_WINTER_KEY = OldBiomes.register(ModernBeta.createId("infdev_420_winter"));
    
    public static final RegistryKey<Biome> INFDEV_415_KEY = OldBiomes.register(ModernBeta.createId("infdev_415"));
    public static final RegistryKey<Biome> INFDEV_415_WINTER_KEY = OldBiomes.register(ModernBeta.createId("infdev_415_winter"));
    
    public static final RegistryKey<Biome> INFDEV_227_KEY = OldBiomes.register(ModernBeta.createId("infdev_227"));
    public static final RegistryKey<Biome> INFDEV_227_WINTER_KEY = OldBiomes.register(ModernBeta.createId("infdev_227_winter"));
    
    public static void registerAlphaBiomes() {
        OldBiomes.register(ALPHA_KEY, Alpha.BIOME);
        OldBiomes.register(ALPHA_WINTER_KEY, AlphaWinter.BIOME);
    }
    
    public static void registerInfdev611Biomes() {
        OldBiomes.register(INFDEV_611_KEY, Infdev611.BIOME);
        OldBiomes.register(INFDEV_611_WINTER_KEY, Infdev611Winter.BIOME);
    }
    
    public static void registerInfdev420Biomes() {
        OldBiomes.register(INFDEV_420_KEY, Infdev420.BIOME);
        OldBiomes.register(INFDEV_420_WINTER_KEY, Infdev420Winter.BIOME);
    }
    
    public static void registerInfdev415Biomes() {
        OldBiomes.register(INFDEV_415_KEY, Infdev415.BIOME);
        OldBiomes.register(INFDEV_415_WINTER_KEY, Infdev415Winter.BIOME);
    }
    
    public static void registerInfdev227Biomes() {
        OldBiomes.register(INFDEV_227_KEY, Infdev227.BIOME);
        OldBiomes.register(INFDEV_227_WINTER_KEY, Infdev227Winter.BIOME);
    }
}
