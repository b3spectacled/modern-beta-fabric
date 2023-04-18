package mod.bespectacled.modernbeta.world.carver;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.carver.Carver;

public class ModernBetaCarvers {
    public static final Carver<BetaCaveCarverConfig> BETA_CAVE = register(
        "beta_cave", 
        new BetaCaveCarver(BetaCaveCarverConfig.CAVE_CODEC)
    );
    
    private static Carver<BetaCaveCarverConfig> register(String id, Carver<BetaCaveCarverConfig> carver) {
        return Registry.register(Registries.CARVER, ModernBeta.createId(id), carver);
    }
    
    public static void register() {}
}
