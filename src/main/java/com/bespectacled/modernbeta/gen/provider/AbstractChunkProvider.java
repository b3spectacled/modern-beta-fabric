package com.bespectacled.modernbeta.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.decorator.OldDecorators;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

/*
 * Some vanilla settings, for reference:
 * 
 * sizeVertical = 2
 * sizeHorizontal = 1
 * height = 128 (or 256 in vanilla)
 * 
 * verticalNoiseResolution = sizeVertical * 4 (8)
 * horizontalNoiseResolution = sizeHorizontal * 4 (4)
 * 
 * noiseSizeX = 16 / horizontalNoiseResolution (4)
 * noiseSizeZ = 16 / horizontalNoiseResolution (4)
 * noiseSizeY = height / verticalNoiseResolution (16)
 * 
 */
public abstract class AbstractChunkProvider {
    protected static final Mutable POS = new Mutable();
    protected static final Random RAND = new Random();
    
    protected static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    protected static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);
    
    protected static final Object2ObjectLinkedOpenHashMap<BlockPos, Integer> HEIGHTMAP_CACHE = new Object2ObjectLinkedOpenHashMap<>(512);
    protected static final int[][] HEIGHTMAP_CHUNK = new int[16][16];
    
    protected final int minY;
    protected final int worldHeight;
    protected final int seaLevel;
    
    protected final int verticalNoiseResolution;
    protected final int horizontalNoiseResolution;
    
    protected final int noiseSizeX;
    protected final int noiseSizeZ;
    protected final int noiseSizeY;
    protected final int noiseMinY;

    protected final double xzScale;
    protected final double yScale;
    
    protected final double xzFactor;
    protected final double yFactor;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    public AbstractChunkProvider(long seed) {
        this(seed, 0, 128, 64, 2, 1, 1.0, 1.0, 80, 160, BlockStates.STONE, BlockStates.WATER);
    }
    
    public AbstractChunkProvider(long seed, int minY, int worldHeight, int seaLevel) {
        this(seed, minY, worldHeight, seaLevel, 2, 1, 1.0, 1.0, 80, 160, BlockStates.STONE, BlockStates.WATER);
    }
    
    public AbstractChunkProvider(
        long seed, 
        int minY, 
        int worldHeight, 
        int seaLevel, 
        int sizeVertical, 
        int sizeHorizontal, 
        double xzScale, 
        double yScale, 
        double xzFactor, 
        double yFactor,
        BlockState defaultBlock,
        BlockState defaultFluid
    ) {
        this.minY = minY;
        this.worldHeight = worldHeight;
        this.seaLevel = seaLevel;
        
        this.verticalNoiseResolution = sizeVertical * 4;
        this.horizontalNoiseResolution = sizeHorizontal * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = this.worldHeight / this.verticalNoiseResolution;
        this.noiseMinY = this.minY / this.verticalNoiseResolution;
        
        this.xzScale = xzScale;
        this.yScale = yScale;
        
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
        
        this.defaultBlock = defaultBlock;
        this.defaultFluid = defaultFluid;
        
        RAND.setSeed(seed);
        HEIGHTMAP_CACHE.clear();
    }
    
    public AbstractChunkProvider(long seed, ChunkGeneratorSettings generatorSettings) {
        this(
            seed,
            generatorSettings.getGenerationShapeConfig().getMinimumY(),
            generatorSettings.getGenerationShapeConfig().getHeight(),
            generatorSettings.getSeaLevel(),
            generatorSettings.getGenerationShapeConfig().getSizeVertical(),
            generatorSettings.getGenerationShapeConfig().getSizeHorizontal(),
            generatorSettings.getGenerationShapeConfig().getSampling().getXZScale(),
            generatorSettings.getGenerationShapeConfig().getSampling().getYScale(),
            generatorSettings.getGenerationShapeConfig().getSampling().getXZFactor(),
            generatorSettings.getGenerationShapeConfig().getSampling().getYFactor(),
            generatorSettings.getDefaultBlock(),
            generatorSettings.getDefaultFluid()
        );
    }
    
    public abstract void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource);
    public abstract void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    public abstract int getHeight(int x, int z, Heightmap.Type type);
    public abstract PerlinOctaveNoise getBeachNoiseOctaves();
    
    public BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        
        if (density > 0.0) {
            blockStateToSet = this.defaultBlock;
        } else if (y < this.seaLevel) {
            if (temp < 0.5D && y >= this.seaLevel - 1) {
                // Does not work well with underwater structures
                //blockStateToSet = BlockStates.ICE;
                blockStateToSet = this.defaultFluid;
            } else {
                blockStateToSet = this.defaultFluid;
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
        OldDecorators.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestOctaves);
        OldDecorators.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestOctaves);
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
            case NETHER:
                provider = new NetherChunkProvider(seed);
                break;
            default:
                throw new IllegalArgumentException("[Modern Beta] No chunk provider matching world type.  This shouldn't happen!");
        }
        
        return provider;
    }
    
}
