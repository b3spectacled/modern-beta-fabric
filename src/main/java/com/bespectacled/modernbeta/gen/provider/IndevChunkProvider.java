package com.bespectacled.modernbeta.gen.provider;

import java.util.ArrayDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.AbstractBiomeProvider;
import com.bespectacled.modernbeta.api.AbstractChunkProvider;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaClimateSampler;
import com.bespectacled.modernbeta.biome.indev.IndevUtil;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevType;
import com.bespectacled.modernbeta.biome.provider.BetaBiomeProvider;
import com.bespectacled.modernbeta.gen.BlockStructureWeightSampler;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoiseCombined;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class IndevChunkProvider extends AbstractChunkProvider {
    private PerlinOctaveNoiseCombined heightNoise1;
    private PerlinOctaveNoiseCombined heightNoise2;
    
    private PerlinOctaveNoise heightNoise3;
    private PerlinOctaveNoise islandNoise;
    
    private PerlinOctaveNoise noise5;
    private PerlinOctaveNoise noise6;
    
    private PerlinOctaveNoiseCombined erodeNoise1;
    private PerlinOctaveNoiseCombined erodeNoise2;
    
    private PerlinOctaveNoise sandNoiseOctaves;
    private PerlinOctaveNoise gravelNoiseOctaves;
 
    // Note:
    // I considered using 1D array for block storage,
    // but apparently 1D array is significantly slower,
    // tested on 1024*1024*256 world
    private Block blockArr[][][];
    
    private final IndevTheme levelTheme;
    private final IndevType levelType;
    private final BlockState fluidBlock;
    private final BlockState topsoilBlock;
    
    private final int width;
    private final int length;
    private final int height;
    private final float caveRadius;
    
    private int layers;
    private int waterLevel;
    //private int groundLevel;
    
    private final AtomicBoolean generated;
    private final CountDownLatch generatedLatch;
    
    public IndevChunkProvider(long seed, AbstractBiomeProvider biomeProvider, Supplier<ChunkGeneratorSettings> generatorSettings, NbtCompound providerSettings) {
        //super(seed, settings);
        super(seed, 0, 320, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, 0, 0, 0, 0, 0, 0, false, false, false, BlockStates.STONE, BlockStates.WATER, biomeProvider, generatorSettings, providerSettings);
        
        this.levelTheme = this.providerSettings.contains("levelTheme") ? IndevTheme.fromName(this.providerSettings.getString("levelTheme")) : IndevTheme.NORMAL;
        this.levelType = this.providerSettings.contains("levelType") ? IndevType.fromName(this.providerSettings.getString("levelType")) : IndevType.ISLAND;
        this.fluidBlock = this.inHell() ? BlockStates.LAVA : BlockStates.WATER;
        this.topsoilBlock = this.inHell() ? BlockStates.PODZOL : BlockStates.GRASS_BLOCK;
        
        this.width = this.providerSettings.contains("levelWidth") ? this.providerSettings.getInt("levelWidth") : 256;
        this.length = this.providerSettings.contains("levelLength") ? this.providerSettings.getInt("levelLength") : 256;
        this.height = this.providerSettings.contains("levelHeight") ? this.providerSettings.getInt("levelHeight") : 128;
        this.caveRadius = this.providerSettings.contains("caveRadius") ? this.providerSettings.getFloat("caveRadius") : 1.0f;
        
        this.waterLevel = this.height / 2;
        this.layers = (this.levelType == IndevType.FLOATING) ? (this.height - 64) / 48 + 1 : 1;
        
        this.blockArr = null;
        this.generated = new AtomicBoolean(false);
        this.generatedLatch = new CountDownLatch(1);
    }

    /**
     * 1.17: Added AtomicBoolean + CountDownLatch
     */
    @Override
    public Chunk provideChunk(StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();

        if (this.inWorldBounds(pos.getStartX(), pos.getStartZ())) {
            this.pregenerateTerrainOrWait();
            this.setTerrain(structureAccessor, chunk);
     
        } else if (this.levelType != IndevType.FLOATING) {
            if (this.levelType == IndevType.ISLAND)
                this.generateWaterBorder(chunk);
            else {
                this.generateWorldBorder(chunk);
            }
        }

        return chunk;
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos.Mutable mutableUp = new BlockPos.Mutable();
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int worldTopY = this.worldHeight + this.minY;
        
        for (int x = 0; x < 16; ++x) {
            int absX = startX + x;
            for (int z = 0; z < 16; ++z) {
                int absZ = startZ + z;
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(absX, 0, absZ));
                
                double temp;
                boolean isCold;
                if (biomeSource.getBiomeProvider() instanceof BetaBiomeProvider) {
                    temp = BetaClimateSampler.INSTANCE.sampleTemp(absX, absZ);
                    isCold = temp < 0.5D ? true : false;
                } else {
                    isCold = biome.isCold(mutable);
                }
                
                for (int y = worldTopY - 1; y >= this.minY; --y) {
                    
                    BlockState topBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                    BlockState fillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                    
                    BlockState state = chunk.getBlockState(mutable.set(x, y, z));

                    // Skip replacing surface blocks if this is a Hell level and biome surface is standard grass/dirt.
                    if (this.inHell() && topBlock.equals(BlockStates.GRASS_BLOCK) && fillerBlock.equals(BlockStates.DIRT))
                        continue;
                    
                    if (state.equals(this.topsoilBlock)) {
                        state = topBlock;
                    } else if (state.equals(BlockStates.DIRT)) {
                        state = fillerBlock;
                    } 
                    
                    // Set snow/ice
                    if (!this.inWorldBounds(absX, absZ)) {
                        if (y == this.seaLevel) {
                            if (isCold && state.equals(topBlock)) {
                                state = topBlock.with(SnowyBlock.SNOWY, true);
                                chunk.setBlockState(mutableUp.set(x, y + 1, z), BlockStates.SNOW, false);
                            }
                            
                        } else if (y == this.seaLevel - 1 && this.levelTheme != IndevTheme.HELL) {
                            if (isCold && state.equals(BlockStates.WATER)) {
                                state = BlockStates.ICE;
                            }
                        }
                    }

                    chunk.setBlockState(mutable.set(x, y, z), state, false);
                }
            }
        }
    }
    
    @Override
    public int getHeight(int x, int z, Type type) {
        int height = this.height - 1;
        
        x = x + this.width / 2;
        z = z + this.length / 2;
        
        if (x < 0 || x >= this.width || z < 0 || z >= this.length) return waterLevel;
        
        this.pregenerateTerrainOrWait();
        
        for (int y = this.height - 1; y >= 0; --y) {
            Block someBlock = this.blockArr[x][y][z];
            
            if (!someBlock.equals(Blocks.AIR)) {
                break;
            }
            
            height = y;
        }
        
        if (height <= this.waterLevel) height = this.waterLevel;
         
        return height;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
    
    @Override
    public boolean skipChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        boolean inIndevRegion = IndevUtil.inIndevRegion(chunkX << 4, chunkZ << 4, this.width, this.length);
        
        if (chunkStatus == ChunkStatus.FEATURES) {
            return !inIndevRegion;
        } else if (chunkStatus == ChunkStatus.STRUCTURE_STARTS) {
            return !inIndevRegion;
        }  else if (chunkStatus == ChunkStatus.CARVERS || chunkStatus == ChunkStatus.LIQUID_CARVERS) {
            return true;
        } else if (chunkStatus == ChunkStatus.SURFACE) { 
            return false;
        }
        
        return false;
    }
    
    public boolean inWorldBounds(int x, int z) {
        return IndevUtil.inIndevRegion(x, z, this.width, this.length);
    }
    
    public void generateIndevHouse(ServerWorld world, BlockPos spawnPos) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Building..");
        
        int spawnX = spawnPos.getX();
        int spawnY = spawnPos.getY() + 1;
        int spawnZ = spawnPos.getZ();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        Block floorBlock = this.inHell() ? Blocks.MOSSY_COBBLESTONE : Blocks.STONE;
        Block wallBlock = this.inHell() ? Blocks.MOSSY_COBBLESTONE : Blocks.OAK_PLANKS;
        
        for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
            for (int y = spawnY - 2; y <= spawnY + 2; ++y) {
                for (int z = spawnZ - 3; z <= spawnZ + 3; ++z) {
                    Block blockToSet = (y < spawnY - 1) ? Blocks.OBSIDIAN : Blocks.AIR;
                    
                    if (x == spawnX - 3 || z == spawnZ - 3 || x == spawnX + 3 || z == spawnZ + 3 || y == spawnY - 2 || y == spawnY + 2) {
                        blockToSet = floorBlock;
                        if (y >= spawnY - 1) {
                            blockToSet = wallBlock;
                        }
                    }
                    if (z == spawnZ + 3 && x == spawnX && y >= spawnY - 1 && y <= spawnY) {
                        blockToSet = Blocks.AIR;
                    }
                    
                    world.setBlockState(mutable.set(x, y, z), BlockStates.getBlockState(blockToSet));
                }
            }
        }
        
        world.setBlockState(mutable.set(spawnX - 3 + 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.CLOCKWISE_90));
        world.setBlockState(mutable.set(spawnX + 3 - 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState().rotate(BlockRotation.COUNTERCLOCKWISE_90));
    }
    
    public IndevType getType() {
        return this.levelType;
    }
    
    public IndevTheme getTheme() {
        return this.levelTheme;
    }
    
    private void setTerrain(StructureAccessor structureAccessor, Chunk chunk) {
        if (this.blockArr == null)
            throw new IllegalStateException("[Modern Beta] Indev chunk provider is trying to set terrain before level has been generated!");
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.width / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.length / 16 / 2) * 16;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        BlockStructureWeightSampler structureWeightSampler = new BlockStructureWeightSampler(structureAccessor, chunk);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                
                int absX = x + (chunkX << 4);
                int absZ = z + (chunkZ << 4);
                
                for (int y = 0; y < this.height; ++y) {
                    Block blockToSet = this.blockArr[offsetX + x][y][offsetZ + z];
                    
                    blockToSet = structureWeightSampler.getBlockWeight(absX, y, absZ, blockToSet);

                    if (blockToSet != Blocks.AIR) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.getBlockState(blockToSet), false);
                    }
                    
                    if (this.levelType == IndevType.FLOATING) continue;
                     
                    if (y <= 1 + this.bedrockFloor && blockToSet == Blocks.AIR) {
                        //chunk.setBlockState(mutable.set(x, y, z), BlockStates.LAVA, false);
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                    } else if (y <= 1 + this.bedrockFloor) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                    }
                    
                    heightmapOCEAN.trackUpdate(x, y, z, BlockStates.getBlockState(blockToSet));
                    heightmapSURFACE.trackUpdate(x, y, z, BlockStates.getBlockState(blockToSet));
                        
                }
            }
        }
    }
    
    private void pregenerateTerrainOrWait() {
        // Only one thread should enter pregeneration method,
        // others should funnel into awaiting for latch to count down.
        if (!this.generated.getAndSet(true)) {
            this.blockArr = pregenerateTerrain();
            this.generatedLatch.countDown();
        } else {
            try {
                this.generatedLatch.await();
            } catch (InterruptedException e) {
                ModernBeta.LOGGER.log(Level.ERROR, "[Modern Beta] Indev chunk provider failed to pregenerate terrain!");
                e.printStackTrace();
            }
        }
    }
    
    private Block[][][] pregenerateTerrain() {
        int[] heightmap = new int[this.width * this.length];
        Block[][][] blockArr = new Block[this.width][this.height][this.length];
        fillBlockArr(blockArr);
        
        for (int l = 0; l < this.layers; ++l) { 
            // Floating island layer generation depends on water level being lowered on each iteration
            this.waterLevel = (this.levelType == IndevType.FLOATING) ? this.height - 32 - l * 48 : this.waterLevel; 
            //this.groundLevel = this.waterLevel - 2;
            
            // Noise Generators (Here instead of constructor to randomize floating layer generation)    
            heightNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, false), new PerlinOctaveNoise(RAND, 8, false));
            heightNoise2 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, false), new PerlinOctaveNoise(RAND, 8, false));
            
            heightNoise3 = new PerlinOctaveNoise(RAND, 6, false);
            islandNoise = new PerlinOctaveNoise(RAND, 2, false);
            
            noise5 = new PerlinOctaveNoise(RAND, 8, false);
            noise6 = new PerlinOctaveNoise(RAND, 8, false);
            
            erodeNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, false), new PerlinOctaveNoise(RAND, 8, false));
            erodeNoise2 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, false), new PerlinOctaveNoise(RAND, 8, false));
            
            sandNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);
            gravelNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);
            
            generateHeightmap(heightmap);
            erodeTerrain(heightmap);
             
            ModernBeta.LOGGER.log(Level.INFO, "[Indev] Soiling..");
            for (int x = 0; x < this.width; ++x) {
                 
                double var1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
                 
                 //relZ = 0;
                 for (int z = 0; z < this.length; ++z) {
                     
                     double var2 = Math.max(var1, Math.abs(z / (this.length - 1.0) - 0.5) * 2.0);
                     var2 = var2 * var2 * var2;
                     
                     int dirtTransition = heightmap[x + z * this.width] + this.waterLevel;
                     int dirtThickness = (int)(noise5.sample(x, z) / 24.0) - 4;
                 
                     int stoneTransition = dirtTransition + dirtThickness;
                     heightmap[x + z * this.width] = Math.max(dirtTransition, stoneTransition);
                     
                     if (heightmap[x + z * this.width] > this.height - 2) {
                         heightmap[x + z * this.width] = this.height - 2;
                     }
                     
                     if (heightmap[x + z * this.width] <= 0) {
                         heightmap[x + z * this.width] = 1;
                     }
                     
                     double var4 = noise6.sample(x * 2.3, z * 2.3) / 24.0;
                     int var5 = (int)(Math.sqrt(Math.abs(var4)) * Math.signum(var4) * 20.0) + this.waterLevel;
                     var5 = (int)(var5 * (1.0 - var2) + var2 * this.height);
                     
                     if (var5 > this.waterLevel) {
                         var5 = this.height;
                     }
                     
                     for (int y = 0; y < this.height; ++y) {
                         Block blockToSet = Blocks.AIR;
                         
                         if (y <= dirtTransition)
                             blockToSet = Blocks.DIRT;
                         
                         if (y <= stoneTransition)
                             blockToSet = Blocks.STONE;
                         
                         if (this.levelType == IndevType.FLOATING && y < var5)
                             blockToSet = Blocks.AIR;

                         Block someBlock = blockArr[x][y][z];
                         
                         if (someBlock.equals(Blocks.AIR)) {
                             blockArr[x][y][z] = blockToSet;
                         }
                     }
                 }
            }
            
            buildSurface(blockArr, heightmap);
            carveTerrain(blockArr);
            floodFluid(blockArr);   
            floodLava(blockArr);
            plantSurface(blockArr);
        }
        
        return blockArr;
    }
    
    
    private void generateHeightmap(int heightmap[]) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Raising..");
        
        for (int x = 0; x < this.width; ++x) {
            double islandVar1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
            
            for (int z = 0; z < this.length; ++z) {
                double islandVar2 = Math.abs((z / (this.length - 1.0) - 0.5) * 2.0);
                
                double heightLow = heightNoise1.sample(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = heightNoise2.sample(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightCheck = heightNoise3.sample(x, z) / 8.0;
                
                if (heightCheck > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                if (this.levelType == IndevType.ISLAND) {
                    double islandVar3 = Math.sqrt(islandVar1 * islandVar1 + islandVar2 * islandVar2) * 1.2000000476837158;
                    islandVar3 = Math.min(islandVar3, islandNoise.sample(x * 0.05f, z * 0.05f) / 4.0 + 1.0);
                    islandVar3 = Math.max(islandVar3, Math.max(islandVar1, islandVar2));
                    
                    if (islandVar3 > 1.0) {
                        islandVar3 = 1.0;
                    } else if (islandVar3 < 0.0) {
                        islandVar3 = 0.0;
                    }
                    
                    islandVar3 *= islandVar3;
                    heightResult = heightResult * (1.0 - islandVar3) - islandVar3 * 10.0 + 5.0;
                    
                    if (heightResult < 0.0) {
                        heightResult -= heightResult * heightResult * 0.20000000298023224;
                    }
                            
                            
                } else if (heightResult < 0.0) {
                    heightResult *= 0.8;
                }
                
                heightmap[x + z * this.width] = (int)heightResult;
            }
        }
    }
    
    private void erodeTerrain(int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Eroding..");
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                double var1 = erodeNoise1.sample(x << 1, z << 1) / 8.0;
                int var2 = erodeNoise2.sample(x << 1, z << 1) > 0.0 ? 1 : 0;
            
                if (var1 > 2.0) {
                    int var3 = heightmap[x + z * this.width];
                    var3 = ((var3 - var2) / 2 << 1) + var2;
                    
                    heightmap[x + z * this.width] = var3;
                }
            }
        }
    }
    
    private void buildSurface(Block[][][] blockArr, int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Growing..");
        
        int seaLevel = this.waterLevel - 1;
        
        if (this.levelTheme == IndevTheme.PARADISE) seaLevel += 2;
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                boolean genSand = sandNoiseOctaves.sample(x, z) > 8.0;
                boolean genGravel = gravelNoiseOctaves.sample(x, z) > 12.0;
                
                if (this.levelType == IndevType.ISLAND) {
                    genSand = sandNoiseOctaves.sample(x, z) > -8.0;
                }
                
                if (this.levelTheme == IndevTheme.PARADISE) {
                    genSand = sandNoiseOctaves.sample(x, z) > -32.0;
                }
                
                if (this.levelTheme == IndevTheme.HELL || this.levelTheme == IndevTheme.WOODS) {
                    genSand = sandNoiseOctaves.sample(x, z) > -8.0;
                }
                
                int heightResult = heightmap[x + z *  this.width];
                Block block = blockArr[x][heightResult][z];
                Block blockAbove = blockArr[x][heightResult + 1][z];
                
                if ((blockAbove == this.fluidBlock.getBlock() || blockAbove == Blocks.AIR) && heightResult <= this.waterLevel - 1 && genGravel) {
                    blockArr[x][heightResult][z] = Blocks.GRAVEL;
                }
                
     
                if (blockAbove == Blocks.AIR) {
                    Block surfaceBlock = null;
                    
                    if (heightResult <= seaLevel && genSand) {
                        surfaceBlock = (this.levelTheme == IndevTheme.HELL) ? Blocks.GRASS_BLOCK : Blocks.SAND; 
                    }
                    
                    if (block != Blocks.AIR && surfaceBlock != null) {
                        blockArr[x][heightResult][z] = surfaceBlock;
                    }
                }
            }
        }
    }
    
    private void carveTerrain(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Carving..");
        
        int caveCount = this.width * this.length * this.height / 256 / 64 << 1;
        
        for (int i = 0; i < caveCount; ++i) {
            float caveX = RAND.nextFloat() * this.width;
            float caveY = RAND.nextFloat() * this.height;
            float caveZ = RAND.nextFloat() * this.length;

            int caveLen = (int)((RAND.nextFloat() + RAND.nextFloat()) * 200F);
            
            float theta = RAND.nextFloat() * 3.1415927f * 2.0f;
            float deltaTheta = 0.0f;
            float phi = RAND.nextFloat() * 3.1415927f * 2.0f;
            float deltaPhi = 0.0f;
            
            float caveRadius = RAND.nextFloat() * RAND.nextFloat() * this.caveRadius;
            
            for (int len = 0; len < caveLen; ++len) {
                caveX += MathHelper.sin(theta) * MathHelper.cos(phi);
                caveZ += MathHelper.cos(theta) * MathHelper.cos(phi);
                caveY += MathHelper.sin(phi);
                
                theta = theta + deltaTheta * 0.2f;
                deltaTheta = (deltaTheta * 0.9f) + (RAND.nextFloat() - RAND.nextFloat());
                phi = phi / 2 + deltaPhi / 4;
                deltaPhi = (deltaPhi * 0.75f) + (RAND.nextFloat() - RAND.nextFloat());
                
                if (RAND.nextFloat() >= 0.25f) {
                    float centerX = caveX + (RAND.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerY = caveY + (RAND.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerZ = caveZ + (RAND.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    
                    float radius = (this.height - centerY) / this.height;
                    radius = 1.2f + (radius * 3.5f + 1.0f) * caveRadius;
                    radius = radius * MathHelper.sin(len * 3.1415927f / caveLen);
                    
                    fillOblateSpheroid(blockArr, centerX, centerY, centerZ, radius, Blocks.AIR);
                }
            }
        }
    }
    
    private void fillOblateSpheroid(Block[][][] blockArr, float centerX, float centerY, float centerZ, float radius, Block fillBlock) {
        for (int x = (int)(centerX - radius); x < (int)(centerX + radius); ++x) {
            for (int y = (int)(centerY - radius); y < (int)(centerY + radius); ++y) {
                for (int z = (int)(centerZ - radius); z < (int)(centerZ + radius); ++z) {
                
                    float dx = x - centerX;
                    float dy = y - centerY;
                    float dz = z - centerZ;
                    
                    if ((dx * dx + dy * dy * 2.0f + dz * dz) < radius * radius && inLevelBounds(x, y, z)) {
                        Block someBlock = blockArr[x][y][z];
                        
                        if (someBlock == Blocks.STONE) {
                            blockArr[x][y][z] = fillBlock;
                        }
                    }
                }
            }
        }
    }
    
    // Using Classic generation algorithm
    private void floodFluid(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Watering..");
        Block toFill = this.fluidBlock.getBlock();
        
        if (this.levelType == IndevType.FLOATING) {
            return;
        }
        
        for (int x = 0; x < this.width; ++x) {
            flood(blockArr, x, this.waterLevel - 1, 0, toFill);
            flood(blockArr, x, this.waterLevel - 1, this.length - 1, toFill);
        }
        
        for (int z = 0; z < this.length; ++z) {
            flood(blockArr, this.width - 1, this.waterLevel - 1, z, toFill);
            flood(blockArr, 0, this.waterLevel - 1, z, toFill);
        }
        
        int waterSourceCount = this.width * this.length / 800;
        
        for (int i = 0; i < waterSourceCount; ++i) {
            int randX = RAND.nextInt(this.width);
            int randZ = RAND.nextInt(this.length);
            int randY = RAND.nextBoolean() ? waterLevel - 1 : waterLevel - 2;
            
            flood(blockArr, randX, randY, randZ, toFill);
        }
       
    }
    
    // Using Classic generation algorithm
    private void floodLava(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Melting..");
        
        if (this.levelType == IndevType.FLOATING) {
            return;
        }

        int lavaSourceCount = this.width * this.length / 20000;
         
        for (int i = 0; i < lavaSourceCount; ++i) {
            int randX = RAND.nextInt(this.width);
            int randZ = RAND.nextInt(this.length);
            int randY = (int)((this.waterLevel - 3) * RAND.nextFloat() * RAND.nextFloat());
            
            flood(blockArr, randX, randY, randZ, Blocks.LAVA);
        }
        
    }
    
    private void flood(Block[][][] blockArr, int x, int y, int z, Block toFill) {
        ArrayDeque<Vec3d> positions = new ArrayDeque<Vec3d>();
        
        positions.add(new Vec3d(x, y, z));
        
        while (!positions.isEmpty()) {
            Vec3d curPos = positions.poll();
            x = (int)curPos.x;
            y = (int)curPos.y;
            z = (int)curPos.z;
            
            Block someBlock = blockArr[x][y][z];

            if (someBlock == Blocks.AIR) {
                blockArr[x][y][z] = toFill;
                
                if (y - 1 >= 0)          positions.add(new Vec3d(x, y - 1, z));
                if (x - 1 >= 0)          positions.add(new Vec3d(x - 1, y, z));
                if (x + 1 < this.width)  positions.add(new Vec3d(x + 1, y, z));
                if (z - 1 >= 0)          positions.add(new Vec3d(x, y, z - 1));
                if (z + 1 < this.length) positions.add(new Vec3d(x, y, z + 1));
            }
        }
        
    }
    
    private void plantSurface(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Planting..");
        
        Block blockToPlant = this.topsoilBlock.getBlock();
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height - 2; ++y) {
                    Block block = blockArr[x][y][z];
                    Block blockAbove = blockArr[x][y + 1][z];
                    
                    if (block.equals(Blocks.DIRT) && blockAbove.equals(Blocks.AIR)) {
                        blockArr[x][y][z] = blockToPlant;
                    }
                }
            }
        }
    }
    
    private void generateWorldBorder(Chunk chunk) {
        BlockState topBlock = this.topsoilBlock;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
         
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                    } else if (y == this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), topBlock, false);
                    }
                }
            }
        }
    }
    
    private void generateWaterBorder(Chunk chunk) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel - 10) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.BEDROCK, false);
                    } else if (y == this.waterLevel - 10) {
                        chunk.setBlockState(mutable.set(x, y, z), BlockStates.DIRT, false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), this.fluidBlock, false);
                    }
                }
            }
        }
    }
    
    private void fillBlockArr(Block[][][] blockArr) {
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    blockArr[x][y][z] = Blocks.AIR;
                }
            }
        }
    }
    
    private boolean inLevelBounds(int x, int y, int z) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height || z < 0 || z >= this.length) {
            return false;
        }
            
        return true;
    }
    
    private boolean inHell() {
        return this.levelTheme == IndevTheme.HELL;
    }
}
