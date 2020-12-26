package com.bespectacled.modernbeta.gen.provider;

import java.util.Map;
import java.util.Random;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.BoundedHashMap;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public abstract class AbstractChunkProvider {
    protected static final Mutable POS = new Mutable();
    protected static final Random RAND = new Random();
    
    protected static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    protected static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);
    
    protected static final Map<BlockPos, Integer> GROUND_CACHE_Y = new BoundedHashMap<>(512);
    protected static final int[][] CHUNK_Y = new int[16][16];
    
    protected final int minY;
    protected final int worldHeight;
    protected final int seaLevel;
    
    public AbstractChunkProvider(long seed) {
        this.minY = -128;
        this.worldHeight = 128;
        this.seaLevel = 64;
        
        RAND.setSeed(seed);
        GROUND_CACHE_Y.clear();
    }
    
    public AbstractChunkProvider(long seed, int minY, int worldHeight, int seaLevel) {
        this.minY = minY;
        this.worldHeight = worldHeight;
        this.seaLevel = seaLevel;
        
        RAND.setSeed(seed);
        GROUND_CACHE_Y.clear();
    }
    
    public abstract void makeChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource);
    public abstract void makeSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    public abstract int getHeight(int x, int z, Heightmap.Type type);
    public abstract PerlinOctaveNoise getBeachNoiseOctaves();
    
    public BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = BlockStates.STONE;
        } else if (y < this.seaLevel) {
            if (temp < 0.5D && y >= this.seaLevel - 1) {
                // Get chunk errors so disabled for now.
                //blockStateToSet = Blocks.ICE.getDefaultState(); 
                blockStateToSet = BlockStates.WATER;
            } else {
                blockStateToSet = BlockStates.WATER;
            }

        }
        return blockStateToSet;
    }
    
    public int getWorldHeight() {
        return this.worldHeight;
    }
    
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    
    public static void setForestOctaves(PerlinOctaveNoise forestOctaves) {
        BetaDecorator.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestOctaves);
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestOctaves);
        BetaDecorator.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestOctaves);
    }
    
    public static AbstractChunkProvider getChunkProvider(long seed, WorldType worldType, OldGeneratorSettings settings) {
        AbstractChunkProvider provider;
        
        switch(worldType) {
            case BETA:
                provider = new BetaChunkProvider(seed);
                break;
            case SKYLANDS:
                provider = new SkylandsChunkProvider(seed);
                break;
            case ALPHA:
                provider = new AlphaChunkProvider(seed);
                break;
            case INFDEV:
                provider = new InfdevChunkProvider(seed);
                break;
            case INFDEV_OLD:
                provider = new InfdevOldChunkProvider(seed, settings.providerSettings);
                break;
            case INDEV:
                provider = new IndevChunkProvider(seed, settings.providerSettings);
                break;
            default:
                throw new IllegalArgumentException("[Modern Beta] No chunk provider matching world type.  This shouldn't happen!");
        }
        
        return provider;
    }
    
}
