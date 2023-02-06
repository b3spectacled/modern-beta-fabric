package mod.bespectacled.modernbeta.world.carver.configured;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.carver.ModernBetaCarvers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;

public class ModernBetaConfiguredCarvers {
    public static final RegistryKey<ConfiguredCarver<?>> BETA_CAVE = of("beta_cave");
    public static final RegistryKey<ConfiguredCarver<?>> BETA_CAVE_DEEP = of("beta_cave_deep");
    
    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredCarver<?>> carverRegisterable = (Registerable<ConfiguredCarver<?>>)registerable;
        RegistryEntryLookup<Block> registryBlock = carverRegisterable.getRegistryLookup(RegistryKeys.BLOCK);
        
        CaveCarverConfig configCave = new CaveCarverConfig(
            0.14285715f, // Probability
            BiasedToBottomHeightProvider.create(YOffset.fixed(0), YOffset.fixed(127), 8), // Y Level
            ConstantFloatProvider.create(0.5f), // Y scale, for large cave case(?)
            YOffset.aboveBottom(10), // Lava Level
            CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
            ConstantFloatProvider.create(1.0f), // Tunnel horizontal scale
            ConstantFloatProvider.create(1.0f), // Tunnel vertical scale
            ConstantFloatProvider.create(-0.69999999999999996f) // Y Floor Level
        );
        
        CaveCarverConfig configCaveDeep = new CaveCarverConfig(
            0.14285715f, // Probability
            BiasedToBottomHeightProvider.create(YOffset.fixed(-64), YOffset.fixed(0), 8), // Y Level
            ConstantFloatProvider.create(0.5f), // Y scale, for large cave case(?)
            YOffset.aboveBottom(10), // Lava Level
            CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
            ConstantFloatProvider.create(1.0f), // Tunnel horizontal scale
            ConstantFloatProvider.create(1.0f), // Tunnel vertical scale
            ConstantFloatProvider.create(-0.69999999999999996f) // Y Floor Level
        );
    
        carverRegisterable.register(BETA_CAVE, ModernBetaCarvers.BETA_CAVE.configure(configCave));
        carverRegisterable.register(BETA_CAVE_DEEP, ModernBetaCarvers.BETA_CAVE.configure(configCaveDeep));
    }
    
    public static RegistryKey<ConfiguredCarver<?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, ModernBeta.createId(id));
    }
}
