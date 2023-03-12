package mod.bespectacled.modernbeta.world.carver;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

public class OldCarvers {
    public static final Carver<CaveCarverConfig> OLD_BETA_CAVE_CARVER = register(
        "old_beta_cave", 
        new OldCaveCarver(CaveCarverConfig.CAVE_CODEC)
    );
    
    public static final RegistryEntry<ConfiguredCarver<CaveCarverConfig>> CONF_OLD_BETA_CAVE_CARVER = register(
        "old_beta_cave", 
        new ConfiguredCarver<CaveCarverConfig>(
            OLD_BETA_CAVE_CARVER, 
            new CaveCarverConfig(
                0.14285715f, // Probability
                BiasedToBottomHeightProvider.create(YOffset.fixed(0), YOffset.fixed(127), 8), // Y Level
                ConstantFloatProvider.create(0.5f), // Y scale, for large cave case(?)
                YOffset.aboveBottom(10), // Lava Level
                CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), 
                ConstantFloatProvider.create(1.0f), // Tunnel horizontal scale
                ConstantFloatProvider.create(1.0f), // Tunnel vertical scale
                ConstantFloatProvider.create(-0.69999999999999996f) // Y Floor Level
            )
        )
    );
    
    public static final RegistryEntry<ConfiguredCarver<CaveCarverConfig>> CONF_OLD_BETA_CAVE_CARVER_DEEP = register(
        "old_beta_cave_deep", 
        new ConfiguredCarver<CaveCarverConfig>(
            OLD_BETA_CAVE_CARVER, 
            new CaveCarverConfig(
                0.14285715f, // Probability
                BiasedToBottomHeightProvider.create(YOffset.fixed(-64), YOffset.fixed(0), 8), // Y Level
                ConstantFloatProvider.create(0.5f), // Y scale, for large cave case(?)
                YOffset.aboveBottom(10), // Lava Level
                CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()), 
                ConstantFloatProvider.create(1.0f), // Tunnel horizontal scale
                ConstantFloatProvider.create(1.0f), // Tunnel vertical scale
                ConstantFloatProvider.create(-0.69999999999999996f) // Y Floor Level
            )
        )
    );
        
    
    private static Carver<CaveCarverConfig> register(String id, Carver<CaveCarverConfig> carver) {
        return Registry.register(Registry.CARVER, ModernBeta.createId(id), carver);
    }
    
    private static <WC extends CarverConfig> RegistryEntry<ConfiguredCarver<WC>> register(String id, ConfiguredCarver<WC> carver) {
        return BuiltinRegistries.method_40360(BuiltinRegistries.CONFIGURED_CARVER, id, carver);
    }
}
