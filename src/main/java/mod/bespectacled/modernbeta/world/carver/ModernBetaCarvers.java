package mod.bespectacled.modernbeta.world.carver;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CaveCarverConfig;

public class ModernBetaCarvers {
    public static final Carver<CaveCarverConfig> BETA_CAVE = register(
        "beta_cave", 
        new BetaCaveCarver(CaveCarverConfig.CAVE_CODEC)
    );
    
    private static Carver<CaveCarverConfig> register(String id, Carver<CaveCarverConfig> carver) {
        return Registry.register(Registries.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static void register() {}
}
