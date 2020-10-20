package com.bespectacled.modernbeta.gen;

import java.util.Arrays;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.IndevGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.IndevUtil.Theme;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class IndevChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<IndevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    IndevGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(IndevChunkGenerator::new)));

    private final IndevGeneratorSettings settings;
    
    private OldNoiseGeneratorCombined heightNoise1;
    private OldNoiseGeneratorCombined heightNoise2;
    
    private OldNoiseGeneratorOctaves heightNoise3;
    private OldNoiseGeneratorOctaves islandNoise;
    
    private OldNoiseGeneratorOctaves noise5;
    private OldNoiseGeneratorOctaves noise6;
    
    private OldNoiseGeneratorCombined erodeNoise1;
    private OldNoiseGeneratorCombined erodeNoise2;
    
    private OldNoiseGeneratorOctaves sandNoiseOctaves;
    private OldNoiseGeneratorOctaves gravelNoiseOctaves;

    // Note:
    // I considered using 1D array for block storage,
    // but apparently 1D array is significantly slower,
    // tested on 1024*1024*256 world
    private Block blockArr[][][];
    private int heightmap[]; 
    
    private int width;
    private int length;
    private int height;
    private int layers;
    private int waterLevel;
    
    private boolean pregenerated = false;
    private Random rand;

    IndevBiomeSource biomeSource;
    private Theme theme;
    private Type type;
    private final Block fluidBlock;

    public static long seed;
    
    public IndevChunkGenerator(BiomeSource biomes, long seed, IndevGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.seed = seed;
        this.rand = new Random(seed);
        this.biomeSource = (IndevBiomeSource) biomes;

        this.theme = Theme.NORMAL;
        this.type = Type.ISLAND;

        this.width = 256;
        this.length = 256;
        this.height = 128;
        
        if (this.settings.settings.contains("levelType")) this.type = Type.values()[settings.settings.getInt("levelType")];
        if (this.settings.settings.contains("levelTheme")) this.theme = Theme.values()[settings.settings.getInt("levelTheme")];
        
        if (this.settings.settings.contains("levelWidth")) this.width = settings.settings.getInt("levelWidth");
        if (this.settings.settings.contains("levelLength")) this.length = settings.settings.getInt("levelLength");
        if (this.settings.settings.contains("levelHeight")) this.height = settings.settings.getInt("levelHeight");
        
        this.fluidBlock = (this.theme == Theme.HELL) ? Blocks.LAVA : Blocks.WATER;
        this.waterLevel = this.height / 2;
        this.layers = (this.type == Type.FLOATING) ? (this.height - 64) / 48 + 1 : 1;
        
        this.pregenerated = false;
        
        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setSeed(seed);
        ModernBeta.setBlockColorsSeed(0L, true);
        ModernBeta.SEED = seed;
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "indev"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Indev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return IndevChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();
        int spawnX = worldAccess.getLevelProperties().getSpawnX();
        int spawnZ = worldAccess.getLevelProperties().getSpawnZ();
        int spawnY = getHeight(spawnX, spawnZ, null);
        
        if (inIndevRegion(pos.x, pos.z)) {
            if (!pregenerated) {
                blockArr = pregenerateTerrain(blockArr);
               
                pregenerated = true;   
            }
            
            setTerrain(chunk, blockArr);
     
        } else if (this.type != Type.FLOATING) {
            if (this.type == Type.ISLAND)
                generateWaterBorder(chunk);
            else {
                generateWorldBorder(chunk);
            }
        }
    }
    
    private boolean inIndevRegion(int chunkX, int chunkZ) {
        int chunkWidth = this.width / 16;
        int chunkLength = this.length / 16;
        
        int chunkHalfWidth = chunkWidth / 2;
        int chunkHalfLength = chunkLength / 2;
        
        if (chunkX >= -chunkHalfWidth && chunkX < chunkHalfWidth && chunkZ >= -chunkHalfLength && chunkZ < chunkHalfLength)
            return true;
        
        return false;
    }
    
    private void setTerrain(Chunk chunk, Block[][][] blockArr) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.width / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.length / 16 / 2) * 16;
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    Block blockToSet = blockArr[offsetX + x][y][offsetZ + z];
                    
                    if (!blockToSet.equals(Blocks.AIR)) {
                        chunk.setBlockState(mutable.set(x, y, z), blockToSet.getDefaultState(), false);
                    }
                     
                    if (y <= 1 && this.type != Type.FLOATING) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                    }
                    
                    heightmapOCEAN.trackUpdate(x, y, z, blockToSet.getDefaultState());
                    heightmapSURFACE.trackUpdate(x, y, z, blockToSet.getDefaultState());
                        
                }
            }
        }
    }
    
    private Block[][][] pregenerateTerrain(Block[][][] blockArr) {
        blockArr = new Block[this.width][this.height][this.length];
        fillBlockArr(blockArr);
        
        for (int l = 0; l < this.layers; ++l) { 
            // Floating island layer generation depends on water level being lowered on each iteration
            this.waterLevel = (this.type == Type.FLOATING) ? this.height - 32 - l * 48 : this.waterLevel; 
            
            // Noise Generators (Here instead of constructor to randomize floating layer generation)    
            heightNoise1 = new OldNoiseGeneratorCombined(new OldNoiseGeneratorOctaves(this.rand, 8, true), new OldNoiseGeneratorOctaves(this.rand, 8, true));
            heightNoise2 = new OldNoiseGeneratorCombined(new OldNoiseGeneratorOctaves(this.rand, 8, true), new OldNoiseGeneratorOctaves(this.rand, 8, true));
            
            heightNoise3 = new OldNoiseGeneratorOctaves(this.rand, 6, true);
            islandNoise = new OldNoiseGeneratorOctaves(this.rand, 2, true);
            
            noise5 = new OldNoiseGeneratorOctaves(this.rand, 8, true);
            noise6 = new OldNoiseGeneratorOctaves(this.rand, 8, true);
            
            erodeNoise1 = new OldNoiseGeneratorCombined(new OldNoiseGeneratorOctaves(this.rand, 8, true), new OldNoiseGeneratorOctaves(this.rand, 8, true));
            erodeNoise2 = new OldNoiseGeneratorCombined(new OldNoiseGeneratorOctaves(this.rand, 8, true), new OldNoiseGeneratorOctaves(this.rand, 8, true));
            
            sandNoiseOctaves = new OldNoiseGeneratorOctaves(this.rand, 8, true);
            gravelNoiseOctaves = new OldNoiseGeneratorOctaves(this.rand, 8, true);
            
            heightmap = generateHeightmap(heightmap);
            erodeTerrain(heightmap);
             
            ModernBeta.LOGGER.log(Level.INFO, "[Indev] Soiling..");
            for (int x = 0; x < this.width; ++x) {
                 
                double var1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
                 
                 //relZ = 0;
                 for (int z = 0; z < this.length; ++z) {
                     
                     double var2 = Math.max(var1, Math.abs(z / (this.length - 1.0) - 0.5) * 2.0);
                     var2 = var2 * var2 * var2;
                     
                     int dirtTransition = heightmap[x + z * this.width] + this.waterLevel;
                     int dirtThickness = (int)(noise5.generateIndevNoiseOctaves(x, z) / 24.0) - 4;
                 
                     int stoneTransition = dirtTransition + dirtThickness;
                     heightmap[x + z * this.width] = Math.max(dirtTransition, stoneTransition);
                     
                     if (heightmap[x + z * this.width] > this.height - 2) {
                         heightmap[x + z * this.width] = this.height - 2;
                     }
                     
                     if (heightmap[x + z * this.width] <= 0) {
                         heightmap[x + z * this.width] = 1;
                     }
                     
                     double var4 = noise6.generateIndevNoiseOctaves(x * 2.3, z * 2.3) / 24.0;
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
                         
                         if (this.type == Type.FLOATING && y < var5)
                             blockToSet = Blocks.AIR;

                         Block someBlock = blockArr[x][y][z];
                         
                         if (someBlock.equals(Blocks.AIR)) {
                             blockArr[x][y][z] = blockToSet;
                         }
                     }
                 }
            }
            
            buildIndevSurface(blockArr, heightmap);
            floodTerrain(blockArr);            
            carveTerrain(blockArr);
            plantIndevSurface(blockArr);
        }
        
        return blockArr;
    }
    
    
    private int[] generateHeightmap(int heightmap[]) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Raising..");
        
        if (heightmap == null) {
            heightmap = new int[this.width * this.length]; // For entire indev world
        }
        
        for (int x = 0; x < this.width; ++x) {
            double islandVar1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
            
            for (int z = 0; z < this.length; ++z) {
                double islandVar2 = Math.abs((z / (this.length - 1.0) - 0.5) * 2.0);
                
                double heightLow = heightNoise1.generateCombinedIndevNoiseOctaves(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = heightNoise2.generateCombinedIndevNoiseOctaves(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightCheck = heightNoise3.generateIndevNoiseOctaves(x, z) / 8.0;
                
                if (heightCheck > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                if (this.type == Type.ISLAND) {
                    double islandVar3 = Math.sqrt(islandVar1 * islandVar1 + islandVar2 * islandVar2) * 1.2000000476837158;
                    islandVar3 = Math.min(islandVar3, islandNoise.generateIndevNoiseOctaves(x * 0.05f, z * 0.05f) / 4.0 + 1.0);
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
       
        return heightmap;
        
    }
    
    private void erodeTerrain(int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Eroding..");
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                double var1 = erodeNoise1.generateCombinedIndevNoiseOctaves(x << 1, z << 1) / 8.0;
                int var2 = erodeNoise2.generateCombinedIndevNoiseOctaves(x << 1, z << 1) > 0.0 ? 1 : 0;
            
                if (var1 > 2.0) {
                    int var3 = heightmap[x + z * this.width];
                    var3 = ((var3 - var2) / 2 << 1) + var2;
                    
                    heightmap[x + z * this.width] = var3;
                }
            }
        }
    }
    
    private void buildIndevSurface(Block[][][] blockArr, int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Growing..");
        
        int seaLevel = this.waterLevel - 1;
        
        if (this.theme == Theme.PARADISE) seaLevel += 2;
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                boolean genSand = sandNoiseOctaves.generateIndevNoiseOctaves(x, z) > 8.0;
                boolean genGravel = gravelNoiseOctaves.generateIndevNoiseOctaves(x, z) > 12.0;
                
                if (this.type == Type.ISLAND) {
                    genSand = sandNoiseOctaves.generateIndevNoiseOctaves(x, z) > -8.0;
                }
                
                if (this.theme == Theme.PARADISE) {
                    genSand = sandNoiseOctaves.generateIndevNoiseOctaves(x, z) > -32.0;
                }
                
                if (this.theme == Theme.HELL || this.theme == Theme.WOODS) {
                    genSand = sandNoiseOctaves.generateIndevNoiseOctaves(x, z) > -8.0;
                }
                
                int heightResult = heightmap[x + z *  this.width];
                Block block = blockArr[x][heightResult][z];
                Block blockAbove = blockArr[x][heightResult + 1][z];
                
                if ((blockAbove.equals(this.fluidBlock) || blockAbove.equals(Blocks.AIR)) && heightResult <= seaLevel && genGravel) {
                    blockArr[x][heightResult][z] = Blocks.GRAVEL;
                }
                
     
                if (blockAbove.equals(Blocks.AIR)) {
                    Block surfaceBlock = null;
                    
                    if (heightResult <= seaLevel && genSand) {
                        surfaceBlock = (this.theme == Theme.HELL) ? Blocks.GRASS_BLOCK : Blocks.SAND; 
                    }
                    
                    if (!block.equals(Blocks.AIR) && surfaceBlock != null) {
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
            float caveX = this.rand.nextFloat() * this.width;
            float caveY = this.rand.nextFloat() * this.height;
            float caveZ = this.rand.nextFloat() * this.length;

            int caveLen = (int)((this.rand.nextFloat() + this.rand.nextFloat()) * 200F);
            
            float theta = this.rand.nextFloat() * 3.1415927f * 2.0f;
            float deltaTheta = 0.0f;
            float phi = this.rand.nextFloat() * 3.1415927f * 2.0f;
            float deltaPhi = 0.0f;
            
            float caveRadius = this.rand.nextFloat() * this.rand.nextFloat() * 1f;
            
            for (int len = 0; len < caveLen; ++len) {
                caveX += MathHelper.sin(theta) * MathHelper.cos(phi);
                caveZ += MathHelper.cos(theta) * MathHelper.cos(phi);
                caveY += MathHelper.sin(phi);
                
                theta = theta + deltaTheta * 0.2f;
                deltaTheta = (deltaTheta * 0.9f) + (this.rand.nextFloat() - this.rand.nextFloat());
                phi = phi / 2 + deltaPhi / 4;
                deltaPhi = (deltaPhi * 0.75f) + (this.rand.nextFloat() - this.rand.nextFloat());
                
                if (this.rand.nextFloat() >= 0.25f) {
                    float centerX = caveX + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerY = caveY + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    float centerZ = caveZ + (this.rand.nextFloat() * 4.0f - 2.0f) * 0.2f;
                    
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
    
    private void floodTerrain(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Watering..");
        
        if (this.type == Type.FLOATING) {
            return;
        }
        
        if (this.type == Type.ISLAND) {
            floodIsland(blockArr);
        }
        
        int waterSourceCount = this.width * this.length / 800;
        
        for (int i = 0; i < waterSourceCount; ++i) {
            int randX = random.nextInt(this.width);
            int randZ = random.nextInt(this.length);
            int y = random.nextBoolean() ? waterLevel - 1 : waterLevel - 2;
            
            flood(blockArr, randX, y, randZ);
        }
    }
    
    private void flood(Block[][][] blockArr, int x, int y, int z) {
        if (x < 0 || z < 0 || x > this.width - 1 || z > this.length - 1) return;
        
        Block someBlock = blockArr[x][y][z];
        
        if (someBlock == Blocks.AIR) {
            blockArr[x][y][z] = this.fluidBlock;
            
            if (y - 1 >= 0          && blockArr[x][y - 1][z] == Blocks.AIR) flood(blockArr, x, y - 1, z);
            if (x - 1 >= 0          && blockArr[x - 1][y][z] == Blocks.AIR) flood(blockArr, x - 1, y, z);
            if (x + 1 < this.width  && blockArr[x + 1][y][z] == Blocks.AIR) flood(blockArr, x + 1, y, z);
            if (z - 1 >= 0          && blockArr[x][y][z - 1] == Blocks.AIR) flood(blockArr, x, y, z - 1);
            if (z + 1 < this.length && blockArr[x][y][z + 1] == Blocks.AIR) flood(blockArr, x, y, z + 1);
        }
    }
    
    private void floodIsland(Block[][][] blockArr) {
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    Block someBlock = blockArr[x][y][z];
                    
                    if (someBlock.equals(Blocks.AIR) && y < waterLevel) {
                        blockArr[x][y][z] = this.fluidBlock;
                    }
                }
            }
        }
    }
    
    private void plantIndevSurface(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Planting..");
        
        if (this.theme == Theme.HELL) return;
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height - 2; ++y) {
                    Block block = blockArr[x][y][z];
                    Block blockAbove = blockArr[x][y + 1][z];
                    
                    if (block.equals(Blocks.DIRT) && blockAbove.equals(Blocks.AIR)) {
                        blockArr[x][y][z] = Blocks.GRASS_BLOCK;
                    }
                }
            }
        }
    }
    
    private void generateWorldBorder(Chunk chunk) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Block topBlock = Blocks.GRASS_BLOCK;
        
        if (this.theme == Theme.HELL) topBlock = Blocks.DIRT;
         
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                    } else if (y == this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), topBlock .getDefaultState(), false);
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
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                    } else if (y == this.waterLevel - 10) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.DIRT.getDefaultState(), false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), this.fluidBlock.getDefaultState(), false);
                    }
                }
            }
        }
    }
    
    private void countBlocks(Block[][][] blockArr) {
        int countStone = 0;
        int countDirt = 0;
        int countAir = 0;
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for(int y = 0; y < this.height; ++y) {
                    Block someBlock = blockArr[x][y][z];
                    
                    if (someBlock.equals(Blocks.STONE)) countStone++;
                    if (someBlock.equals(Blocks.DIRT)) countDirt++;
                    if (someBlock.equals(Blocks.AIR)) countAir++;
                }
            }
        }
        
        ModernBeta.LOGGER.log(Level.DEBUG, "Block count, stone/dirt/air: " + countStone + ", " + countDirt + ", " + countAir);
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
    
    // Not ideal, unused
    /*
    private void generateIndevHouse(WorldAccess world, int spawnX, int spawnY, int spawnZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
            for (int y = spawnY - 2; y <= spawnY + 2; ++y) {
                for (int z = spawnZ - 3; z <= spawnZ + 3; ++z) {
                    Block blockToSet = (y < spawnY - 1) ? Blocks.OBSIDIAN : Blocks.AIR;
                    if (x == spawnX - 3 || z == spawnZ - 3 || x == spawnX + 3 || z == spawnZ + 3 || y == spawnY - 2 || y == spawnY + 2) {
                        blockToSet = Blocks.STONE;
                        if (y >= spawnY - 1) {
                            blockToSet = Blocks.OAK_PLANKS;
                        }
                    }
                    if (z == spawnZ - 3 && x == spawnX && y >= spawnY - 1 && y <= spawnY) {
                        blockToSet = Blocks.AIR;
                    }
                    if (x >= this.width || x < 0 || z >= this.length || z < 0) continue;
                    //blockArr[x][y][z] = blockToSet;
                    world.setBlockState(mutable.set(x, y, z), blockToSet.getDefaultState(), 1);
                }
            }
        }
        
        //blockArr[spawnX - 3 + 1][spawnY][spawnZ] = Blocks.WALL_TORCH;
        //blockArr[spawnX + 3 - 1][spawnY][spawnZ] = Blocks.WALL_TORCH;
        
        world.setBlockState(mutable.set(spawnX - 3 + 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState(), 1);
        world.setBlockState(mutable.set(spawnX + 3 - 1, spawnY, spawnZ), Blocks.WALL_TORCH.getDefaultState(), 1);
    }
    */
    
    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
        // Do nothing, for now.
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int height = this.waterLevel + 1;
        
        if (x < 0 || x >= this.width || z < 0 || z >= this.length) return height;
        
        if (!pregenerated) {
            this.blockArr = pregenerateTerrain(this.blockArr);
            pregenerated = true;
        }
        
        for (int y = this.height - 1; y >= 0; --y) {
            Block someBlock = this.blockArr[x][y][z];
            
            if (!someBlock.equals(Blocks.AIR)) {
                break;
            }
            
            height = y;
        }
        
        if (height == 0) height = this.waterLevel;
         
        return height - 1;
    }

    @Override
    public int getMaxY() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return this.waterLevel;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new IndevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

}
