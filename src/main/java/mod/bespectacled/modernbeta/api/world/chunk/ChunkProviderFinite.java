package mod.bespectacled.modernbeta.api.world.chunk;

import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

import org.slf4j.event.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceRules;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaGenerationStep;
import mod.bespectacled.modernbeta.world.spawn.SpawnLocatorIndev;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevel;
import net.minecraft.world.gen.chunk.AquiferSampler.FluidLevelSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.noise.NoiseConfig;

public abstract class ChunkProviderFinite extends ChunkProvider implements ChunkProviderNoiseImitable {
    private static String levelPhase;
    
    protected final int worldMinY;
    protected final int worldHeight;
    protected final int worldTopY;
    protected final int seaLevel;
    
    protected final int bedrockFloor;
    protected final int bedrockCeiling;
    
    protected final boolean useDeepslate;
    
    protected final BlockState defaultBlock;
    protected final BlockState defaultFluid;
    
    protected final int levelWidth;
    protected final int levelLength;
    protected final int levelHeight;
    protected final float caveRadius;
    
    protected final int[] heightmap;
    
    @Deprecated
    private final Block[][][] blockArr;
    
    private boolean pregenerated;
    
    public ChunkProviderFinite(ModernBetaChunkGenerator chunkGenerator, long seed) {
        super(chunkGenerator, seed);

        ChunkGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
        GenerationShapeConfig shapeConfig = generatorSettings.generationShapeConfig();
        
        this.worldMinY = shapeConfig.minimumY();
        this.worldHeight = shapeConfig.height();
        this.worldTopY = this.worldHeight + this.worldMinY;
        this.seaLevel = generatorSettings.seaLevel();
        this.bedrockFloor = 0;
        this.bedrockCeiling = Integer.MIN_VALUE;
        this.useDeepslate = this.chunkSettings.useDeepslate;

        this.defaultBlock = generatorSettings.defaultBlock();
        this.defaultFluid = generatorSettings.defaultFluid();
        
        this.levelWidth = this.chunkSettings.indevLevelWidth;
        this.levelLength = this.chunkSettings.indevLevelLength;
        this.levelHeight = MathHelper.clamp(this.chunkSettings.indevLevelHeight, 0, this.worldTopY);
        this.caveRadius = this.chunkSettings.indevCaveRadius;
        
        this.heightmap = new int[this.levelWidth * this.levelLength];
        this.blockArr = new Block[this.levelWidth][this.levelHeight][this.levelLength];
        this.fillBlockArr(Blocks.AIR);
        
        this.pregenerated = false;
    }
    
    @Override
    public SpawnLocator getSpawnLocator() {
        return new SpawnLocatorIndev(this);
    }

    @Override
    public CompletableFuture<Chunk> provideChunk(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk, NoiseConfig noiseConfig) {
        ChunkPos pos = chunk.getPos();

        if (this.inWorldBounds(pos.getStartX(), pos.getStartZ())) {
            this.pregenerateTerrainOrWait();
            this.generateTerrain(chunk, structureAccessor);
        } else {
            this.generateBorder(chunk);
        }

        return CompletableFuture.<Chunk>supplyAsync(
            () -> chunk, Util.getMainWorkerExecutor()
        );
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, ModernBetaBiomeSource biomeSource, NoiseConfig noiseConfig) {
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int worldTopY = this.worldHeight + this.worldMinY;
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                int x = startX + localX;
                int z = startZ + localZ;
                RegistryEntry<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, 0, z));
                
                boolean isCold;
                if (biomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
                    climateSampler.useBiomeFeature()) {
                    isCold = climateSampler.sample(x, z).temp() < 0.5D;
                } else {
                    isCold = biome.value().isCold(pos);
                }
                
                for (int y = worldTopY - 1; y >= this.worldMinY; --y) {
                    pos.set(x, y, z);
                    
                    BlockState blockState = this.postProcessSurfaceState(chunk.getBlockState(pos), biome, pos, isCold);
                    
                    chunk.setBlockState(pos, blockState, false);

                    // Set snow on top of snowy blocks
                    if (blockState.contains(Properties.SNOWY) && blockState.get(Properties.SNOWY).booleanValue())
                        chunk.setBlockState(pos.up(), BlockStates.SNOW, false);
                        
                }
            }
        }
    }
    
    @Override
    public int getHeight(int x, int z, Type type) {
        int seaLevel = this.getSeaLevel();
        
        x += this.levelWidth / 2;
        z += this.levelLength / 2;
        
        if (x < 0 || x >= this.levelWidth || z < 0 || z >= this.levelLength) 
            return seaLevel;
        
        this.pregenerateTerrainOrWait();
        int height = this.getLevelHighestBlock(x, z, type);
         
        return height;
    }
    
    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
        boolean outOfBounds = !this.inWorldBounds(chunkX << 4, chunkZ << 4);
        
        if (step == ModernBetaGenerationStep.FEATURES) {
            return outOfBounds;
        } else if (step == ModernBetaGenerationStep.STRUCTURE_STARTS) {
            return outOfBounds;
        }  else if (step == ModernBetaGenerationStep.CARVERS) {
            return outOfBounds|| !this.chunkSettings.useCaves;
        } else if (step == ModernBetaGenerationStep.SURFACE) { 
            return false;
        } else if (step == ModernBetaGenerationStep.ENTITY_SPAWN) {
            return outOfBounds;
        }
        
        return false;
    }
    
    @Override
    public AquiferSampler getAquiferSampler(Chunk chunk, NoiseConfig noiseConfig) {
        FluidLevelSampler fluidLevelSampler = (x, y, z) -> new FluidLevel(
            this.getSeaLevel(), this.getLevelFluidBlock().getDefaultState()
        );
        
        return AquiferSampler.seaLevel(fluidLevelSampler);
    }
    
    public int getLevelWidth() {
        return this.levelWidth;
    }
    
    public int getLevelLength() {
        return this.levelLength;
    }
    
    public int getLevelHeight() {
        return this.levelHeight;
    }
    
    public float getCaveRadius() {
        return this.caveRadius;
    }
    
    public Block getLevelBlock(int x, int y, int z) {
        x = MathHelper.clamp(x, 0, this.levelWidth - 1);
        y = MathHelper.clamp(y, 0, this.levelHeight - 1);
        z = MathHelper.clamp(z, 0, this.levelLength - 1);
        
        return this.blockArr[x][y][z];
    }
    
    public void setLevelBlock(int x, int y, int z, Block block) {
        x = MathHelper.clamp(x, 0, this.levelWidth - 1);
        y = MathHelper.clamp(y, 0, this.levelHeight - 1);
        z = MathHelper.clamp(z, 0, this.levelLength - 1);
        
        this.blockArr[x][y][z] = block;
    }
    
    public int getLevelHighestBlock(int x, int z, Heightmap.Type type) {
        x = MathHelper.clamp(x, 0, this.levelWidth - 1);
        z = MathHelper.clamp(z, 0, this.levelLength - 1);
        
        Predicate<Block> checkBlock = switch(type) {
            case OCEAN_FLOOR_WG -> block -> block == Blocks.AIR || block == this.getLevelFluidBlock();
            case WORLD_SURFACE_WG -> block -> block == Blocks.AIR;
            default -> block -> block == Blocks.AIR;
        };
        
        int y;
        
        for (y = this.levelHeight; checkBlock.test(this.getLevelBlock(x, y - 1, z)) && y > 0; --y);
        
        return y;
    }
    
    public Block getLevelFluidBlock() {
        return this.defaultFluid.getBlock();
    }
    
    protected abstract void pregenerateTerrain();
    
    protected abstract void generateBorder(Chunk chunk);
    
    protected abstract BlockState postProcessTerrainState(
        Block block, 
        BlockSourceRules blockSources,
        TerrainState terrainState,
        BlockPos pos,
        int topY
    );
    
    protected abstract void generateBedrock(Chunk chunk, Block block, BlockPos pos);

    protected abstract BlockState postProcessSurfaceState(BlockState blockState, RegistryEntry<Biome> biome, BlockPos pos, boolean isCold);
    
    protected void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.levelWidth / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.levelLength / 16 / 2) * 16;
        
        Heightmap heightmapOcean = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSurface = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        StructureWeightSampler structureWeightSampler = StructureWeightSampler.createStructureWeightSampler(structureAccessor, chunk.getPos());
        BlockPos.Mutable pos = new BlockPos.Mutable();
        SimpleNoisePos noisePos = new SimpleNoisePos();
        
        BlockHolder blockHolder = new BlockHolder();
        BlockSource baseBlockSource = this.getBaseBlockSource(structureWeightSampler, noisePos, blockHolder, this.defaultBlock.getBlock(), this.getLevelFluidBlock());
        BlockSourceRules blockSources = new BlockSourceRules.Builder()
            .add(baseBlockSource)
            .add(this.getActualBlockSource(blockHolder))
            .build(this.defaultBlock);
        
        for (int localX = 0; localX < 16; ++localX) {
            for (int localZ = 0; localZ < 16; ++localZ) {
                
                int x = localX + (chunkX << 4);
                int z = localZ + (chunkZ << 4);
                int topY = this.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                
                TerrainState terrainState = new TerrainState();
                
                for (int y = this.levelHeight - 1; y >= 0; --y) {
                    pos.set(x, y, z);
                    
                    Block block = this.getLevelBlock(offsetX + localX, y, offsetZ + localZ);
                    blockHolder.setBlock(block);
                    
                    BlockState blockState = this.postProcessTerrainState(block, blockSources, terrainState, pos, topY);
                    
                    chunk.setBlockState(pos.set(localX, y, localZ), blockState, false);
                     
                    this.generateBedrock(chunk, block, pos);
                    
                    heightmapOcean.trackUpdate(localX, y, localZ, block.getDefaultState());
                    heightmapSurface.trackUpdate(localX, y, localZ, block.getDefaultState());
                }
            }
        }
    }

    protected boolean inWorldBounds(int x, int z) {
        int halfWidth = this.levelWidth / 2;
        int halfLength = this.levelLength / 2;
        
        if (x >= -halfWidth && x < halfWidth && z >= -halfLength && z < halfLength) {
            return true;
        }
        
        return false;
    }

    protected boolean inLevelBounds(int x, int y, int z) {
        if (x < 0 || x >= this.levelWidth || y < 0 || y >= this.levelHeight || z < 0 || z >= this.levelLength) {
            return false;
        }
            
        return true;
    }

    protected void setPhase(String phase) {
        levelPhase = phase + "..";
        
        ModernBeta.log(Level.INFO, levelPhase);
    }
    
    protected void fillOblateSpheroid(float centerX, float centerY, float centerZ, float radius, Block fillBlock) {
        for (int x = (int)(centerX - radius); x < (int)(centerX + radius); ++x) {
            for (int y = (int)(centerY - radius); y < (int)(centerY + radius); ++y) {
                for (int z = (int)(centerZ - radius); z < (int)(centerZ + radius); ++z) {
                
                    float dx = x - centerX;
                    float dy = y - centerY;
                    float dz = z - centerZ;
                    
                    if ((dx * dx + dy * dy * 2.0f + dz * dz) < radius * radius && inLevelBounds(x, y, z)) {
                        Block block = this.getLevelBlock(x, y, z);
                        
                        if (block == this.defaultBlock.getBlock()) {
                            this.setLevelBlock(x, y, z, fillBlock);
                        }
                    }
                }
            }
        }
    }
    
    protected void flood(int x, int y, int z, Block fillBlock) {
        ArrayDeque<Vec3d> positions = new ArrayDeque<Vec3d>();
        
        positions.add(new Vec3d(x, y, z));
        
        while (!positions.isEmpty()) {
            Vec3d curPos = positions.poll();
            x = (int)curPos.x;
            y = (int)curPos.y;
            z = (int)curPos.z;
            
            Block block = this.getLevelBlock(x, y, z);
    
            if (block == Blocks.AIR) {
                this.setLevelBlock(x, y, z, fillBlock);
                
                if (y - 1 >= 0)               this.tryFlood(x, y - 1, z, positions);
                if (x - 1 >= 0)               this.tryFlood(x - 1, y, z, positions);
                if (x + 1 < this.levelWidth)  this.tryFlood(x + 1, y, z, positions);
                if (z - 1 >= 0)               this.tryFlood(x, y, z - 1, positions);
                if (z + 1 < this.levelLength) this.tryFlood(x, y, z + 1, positions);
            }
        }
    }
    
    private void tryFlood(int x, int y, int z, ArrayDeque<Vec3d> positions) {
        Block block = this.getLevelBlock(x, y, z);
        
        if (block == Blocks.AIR) {
            positions.add(new Vec3d(x, y, z));
        }
    }

    private synchronized void pregenerateTerrainOrWait() {
        if (!this.pregenerated) {
            this.pregenerateTerrain();
            this.pregenerated = true;
        }
    }
    
    private void fillBlockArr(Block block) {
        for (int x = 0; x < this.levelWidth; ++x) {
            for (int z = 0; z < this.levelLength; ++z) {
                for (int y = 0; y < this.levelHeight; ++y) {
                    this.setLevelBlock(x, y, z, block);
                }
            }
        }
    }
    
    public static void resetPhase() {
        levelPhase = "";
    }
    
    public static String getPhase() {
        return levelPhase;
    }
    
    protected static class TerrainState {
        private int runDepth;
        private boolean terrainModified;
        
        public TerrainState() {
            this.runDepth = 0;
            this.terrainModified = false;
        }
        
        public int getRunDepth() {
            return this.runDepth;
        }
        
        public void incrementRunDepth() {
            this.runDepth++;
        }
        
        public boolean isTerrainModified() {
            return this.terrainModified;
        }
        
        public void terrainModified() {
            this.terrainModified = true;
        }
    }
}
