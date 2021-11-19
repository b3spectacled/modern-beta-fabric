package com.bespectacled.modernbeta.world.gen;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.compat.CompatBiomes;
import com.bespectacled.modernbeta.mixin.MixinSurfaceBuilderAccessor;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.random.ChunkRandom.RandomProvider;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

public class OldSurfaceBuilder extends SurfaceBuilder {
    // Set for specifying which biomes should use their vanilla surface builders.
    // Done on per-biome basis for best mod compatibility.
    private static final Set<Identifier> BIOMES_WITH_CUSTOM_SURFACES = new HashSet<Identifier>(
        Stream.concat(
            CompatBiomes.BIOMES_WITH_CUSTOM_SURFACES.stream().map(b -> new Identifier(b)), 
            ModernBeta.COMPAT_CONFIG.biomesWithCustomSurfaces.stream().map(b -> new Identifier(b))
        ).toList()
    );
    
    private final ChunkProvider chunkProvider;
    
    public OldSurfaceBuilder(
        NoiseColumnSampler columnSampler, 
        Registry<NoiseParameters> noiseRegistry, 
        BlockState blockState, 
        int seaLevel, 
        long seed, 
        RandomProvider randomProvider,
        ChunkProvider chunkProvider
    ) {
        super(columnSampler, noiseRegistry, blockState, seaLevel, seed, randomProvider);
        
        this.chunkProvider = chunkProvider;
    }
    
    /*
     * Like buildSurface(), but for a single column
     */
    public boolean buildSurfaceColumn(
        Registry<Biome> biomeRegistry,
        BiomeAccess biomeAccess,
        BlockColumnHolder column, 
        Chunk chunk,
        Biome biome,
        MaterialRules.MaterialRuleContext ruleContext,
        MaterialRules.BlockStateRule blockStateRule,
        int localX,
        int localZ,
        int surfaceTopY
    ) {
        RegistryKey<Biome> biomeKey = biomeRegistry.getKey(biome).orElseThrow(() -> new IllegalStateException("Unregistered biome: " + biome));
        
        if (!BIOMES_WITH_CUSTOM_SURFACES.contains(biomeKey.getValue())) {
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
        
        MixinSurfaceBuilderAccessor accessor = this.injectAccessor(this);
        
        // Generate eroded badlands hoodoos, as stone initially
        if (biomeKey == BiomeKeys.ERODED_BADLANDS) {
            accessor.invokeErodedBadlandsBuilder(column.getBlockColumn(), x, z, surfaceMinY, chunk);
            
            // Re-sample height after hoodoo generation
            height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;
        }
        
        ruleContext.initHorizontalContext(x, z);
        
        int stoneDepthAbove = 0;
        
        int waterHeight = Integer.MIN_VALUE;
        int solidHeight = Integer.MAX_VALUE;
        
        for (int y = height; y >= surfaceMinY; --y) {
            BlockState blockState = column.getBlockColumn().getState(y);
            pos.set(x, y, z);
            
            if (blockState.isAir()) {
                stoneDepthAbove = 0;
                waterHeight = Integer.MIN_VALUE;
                continue;
            }
            
            if (!blockState.getFluidState().isEmpty()) {
                if (waterHeight != Integer.MIN_VALUE)
                    continue;
                
                waterHeight = y + 1;
                continue;
            }
            
            if (solidHeight >= y) {
                solidHeight = DimensionType.field_35479;
                
                for (int solidY = y - 1; y >= surfaceMinY - 1; --solidY) {
                    blockState = column.getBlockColumn().getState(solidY);
                    
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
            
            blockState = blockStateRule.tryApply(x, y, z);
            
            if (blockState == null || blockState.isOf(Blocks.BEDROCK) || blockState.isOf(Blocks.DEEPSLATE))
                continue;
            
            column.getBlockColumn().setState(y, blockState);
        }
        
        if (biomeKey == BiomeKeys.FROZEN_OCEAN || biomeKey == BiomeKeys.DEEP_FROZEN_OCEAN) {
            accessor.invokeFrozenOceanBuilder(surfaceMinY, biome, column.getBlockColumn(), pos, x, z, height);
        }
        
        return true;
    }
    
    private boolean isSolid(BlockState blockState) {
        return !blockState.isAir() && blockState.getFluidState().isEmpty();
    }
    
    private MixinSurfaceBuilderAccessor injectAccessor(SurfaceBuilder surfaceBuilder) {
        return (MixinSurfaceBuilderAccessor)surfaceBuilder;
    }
    
    @Override
    protected int method_39553(ChunkNoiseSampler noiseSampler, int x, int z) {
        int topY = (this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider) ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        
        return topY - 8;
    }
}
