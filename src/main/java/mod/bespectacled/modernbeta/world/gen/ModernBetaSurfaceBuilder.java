package mod.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import mod.bespectacled.modernbeta.compat.CompatBiomes;
import mod.bespectacled.modernbeta.mixin.MixinSurfaceBuilderAccessor;
import mod.bespectacled.modernbeta.util.BlockColumnHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.random.ChunkRandom.RandomProvider;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class ModernBetaSurfaceBuilder extends SurfaceBuilder {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    private static final Set<RegistryKey<Biome>> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<RegistryKey<Biome>>(
        Stream.concat(
            CompatBiomes.BIOMES_WITH_CUSTOM_SURFACES.stream(), 
            ModernBeta.COMPAT_CONFIG.biomesWithCustomSurfaces.stream().map(b -> RegistryKey.of(Registry.BIOME_KEY, new Identifier(b)))
        ).toList()
    );
    
    //private final ChunkProvider chunkProvider;
    private final BlockState defaultState;
    
    public ModernBetaSurfaceBuilder(
        Registry<NoiseParameters> noiseRegistry, 
        BlockState defaultState, 
        int seaLevel, 
        long seed, 
        RandomProvider randomProvider,
        ChunkProvider chunkProvider
    ) {
        super(noiseRegistry, defaultState, seaLevel, seed, RandomProvider.XOROSHIRO);
        
        //this.chunkProvider = chunkProvider;
        this.defaultState = defaultState;
    }
    
    /*
     * Like buildSurface(), but for a single column
     */
    public boolean buildSurfaceColumn(
        Registry<Biome> biomeRegistry,
        BiomeAccess biomeAccess,
        BlockColumnHolder column, 
        Chunk chunk,
        RegistryEntry<Biome> biome,
        MaterialRules.MaterialRuleContext ruleContext,
        MaterialRules.BlockStateRule blockStateRule,
        int localX,
        int localZ,
        int surfaceTopY
    ) {
        MixinSurfaceBuilderAccessor accessor = this.injectAccessor(this);
        
        if (biome.getKey().isPresent() && !BIOMES_WITH_CUSTOM_SURFACES.contains(biome.getKey().get())) {
            return false;
        }
        
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        
        int x = startX + localX;
        int z = startZ + localZ;
        
        int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;
        int surfaceMinY = surfaceTopY - 8;
        
        column.setPos(x, z);
        
        // Generate eroded badlands hoodoos, as stone initially
        if (biome.matchesKey(BiomeKeys.ERODED_BADLANDS)) {
            accessor.invokePlaceBadlandsPillar(column.getBlockColumn(), x, z, surfaceMinY, chunk);
        }
        
        int newHeight = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;
        ruleContext.initHorizontalContext(x, z);
        
        int stoneDepthAbove = 0;
        
        int waterHeight = Integer.MIN_VALUE;
        int solidHeight = Integer.MAX_VALUE;
        int minimumY = chunk.getBottomY();
        
        for (int y = newHeight; y >= minimumY; --y) {
            BlockState columnState = column.getState(y);
            BlockState blockState;
            
            pos.set(x, y, z);
            
            if (columnState.isAir()) {
                stoneDepthAbove = 0;
                waterHeight = Integer.MIN_VALUE;
                continue;
            }
            
            if (!columnState.getFluidState().isEmpty()) {
                if (waterHeight != Integer.MIN_VALUE)
                    continue;
                
                waterHeight = y + 1;
                continue;
            }
            
            if (solidHeight >= y) {
                solidHeight = DimensionType.field_35479;
                
                for (int solidY = y - 1; y >= minimumY - 1; --solidY) {
                    blockState = column.getState(solidY);
                    
                    if (this.isSolid(blockState))
                        continue;
                    
                    solidHeight = solidY + 1;
                    break;
                }
            }
            
            int stoneDepthBelow = y - solidHeight + 1;
            ruleContext.initVerticalContext(
                ++stoneDepthAbove,
                stoneDepthBelow, 
                waterHeight, 
                x, y, z
            );
            
            if (columnState != this.defaultState)
                continue;
            
            blockState = blockStateRule.tryApply(x, y, z);
            if (blockState == null || this.ignoreBlockStateRule(blockState))
                continue;
            
            column.setState(y, blockState);
        }
        
        if (biome.matchesKey(BiomeKeys.FROZEN_OCEAN) || biome.matchesKey(BiomeKeys.DEEP_FROZEN_OCEAN)) {
            accessor.invokePlaceIceberg(surfaceMinY, biome.value(), column.getBlockColumn(), pos, x, z, height);
        }
        
        return true;
    }
    
    private boolean isSolid(BlockState blockState) {
        return !blockState.isAir() && blockState.getFluidState().isEmpty();
    }
    
    private MixinSurfaceBuilderAccessor injectAccessor(SurfaceBuilder surfaceBuilder) {
        return (MixinSurfaceBuilderAccessor)surfaceBuilder;
    }

    private boolean ignoreBlockStateRule(BlockState blockState) {
        if (blockState.isOf(Blocks.BEDROCK) || blockState.isOf(Blocks.DEEPSLATE))
            return true;
            
        return false;
    }
}
