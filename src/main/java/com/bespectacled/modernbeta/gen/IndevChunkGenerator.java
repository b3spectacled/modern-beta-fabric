package com.bespectacled.modernbeta.gen;

import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.IndevUtil;
import com.bespectacled.modernbeta.util.IndevUtil.Theme;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class IndevChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<IndevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(IndevChunkGenerator::new)));

    private final OldGeneratorSettings settings;
    private final StructuresConfig structuresConfig;
    private final IndevBiomeSource biomeSource;
    private final long seed;
    
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
    
    private PerlinOctaveNoise forestNoiseOctaves;

    // Note:
    // I considered using 1D array for block storage,
    // but apparently 1D array is significantly slower,
    // tested on 1024*1024*256 world
    private Block blockArr[][][];
    private int heightmap[]; 
    
    private Theme theme;
    private Type type;
    private BlockState fluidBlock;
    
    private int width;
    private int length;
    private int height;
    private int layers;
    private int waterLevel;
    private int groundLevel;
    private float caveRadius;
    
    private int spawnX;
    private int spawnZ;
    
    private boolean pregenerated = false;

    private static final Mutable POS = new Mutable();
    private static final Random RAND = new Random();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);

    public IndevChunkGenerator(BiomeSource biomes, long seed, OldGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.structuresConfig = settings.wrapped.getStructuresConfig();
        this.seed = seed;
        this.biomeSource = (IndevBiomeSource) biomes;
        
        RAND.setSeed(seed);
        
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);

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
        if (this.settings.settings.contains("caveRadius")) this.caveRadius = settings.settings.getFloat("caveRadius");
        
        this.fluidBlock = (this.theme == Theme.HELL) ? BlockStates.LAVA : BlockStates.WATER;
        this.waterLevel = this.height / 2;
        this.layers = (this.type == Type.FLOATING) ? (this.height - 64) / 48 + 1 : 1;
        
        this.spawnX = 0;
        this.spawnZ = 0;
        
        this.pregenerated = false;
        
        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "indev"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Indev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return IndevChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();
        
        this.spawnX = worldAccess.getLevelProperties().getSpawnX();
        this.spawnZ = worldAccess.getLevelProperties().getSpawnZ();

        if (IndevUtil.inIndevRegion(pos.getStartX(), pos.getStartZ(), this.width, this.length)) {

            if (!pregenerated) {
                blockArr = pregenerateTerrain(blockArr);
               
                pregenerated = true;   
            }
            
            setTerrain(structureAccessor, chunk, blockArr);
     
        } else if (this.type != Type.FLOATING) {
            if (this.type == Type.ISLAND)
                generateWaterBorder(chunk);
            else {
                generateWorldBorder(chunk);
            }
        }
    }
    
    private void setTerrain(StructureAccessor structureAccessor, Chunk chunk, Block[][][] blockArr) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = (chunkX + this.width / 16 / 2) * 16;
        int offsetZ = (chunkZ + this.length / 16 / 2) * 16;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                
                int absX = x + (chunkX << 4);
                int absZ = z + (chunkZ << 4);
                
                for (int y = 0; y < this.height; ++y) {
                    Block blockToSet = blockArr[offsetX + x][y][offsetZ + z];
                    
                    blockToSet = GenUtil.getStructBlock(
                        structureListIterator, 
                        jigsawListIterator, 
                        STRUCTURE_LIST.size(), 
                        JIGSAW_LIST.size(), 
                        absX, y, absZ, blockToSet);

                    if (blockToSet != Blocks.AIR) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.getBlockState(blockToSet), false);
                    }
                    
                    if (this.type == Type.FLOATING) continue;
                     
                    if (y <= 1 && blockToSet == Blocks.AIR) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.LAVA, false);
                    } else if (y <= 1) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.BEDROCK, false);
                    }
                    
                    heightmapOCEAN.trackUpdate(x, y, z, BlockStates.getBlockState(blockToSet));
                    heightmapSURFACE.trackUpdate(x, y, z, BlockStates.getBlockState(blockToSet));
                        
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
            this.groundLevel = this.waterLevel - 2;
            
            // Noise Generators (Here instead of constructor to randomize floating layer generation)    
            heightNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, true), new PerlinOctaveNoise(RAND, 8, true));
            heightNoise2 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, true), new PerlinOctaveNoise(RAND, 8, true));
            
            heightNoise3 = new PerlinOctaveNoise(RAND, 6, true);
            islandNoise = new PerlinOctaveNoise(RAND, 2, true);
            
            noise5 = new PerlinOctaveNoise(RAND, 8, true);
            noise6 = new PerlinOctaveNoise(RAND, 8, true);
            
            erodeNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, true), new PerlinOctaveNoise(RAND, 8, true));
            erodeNoise2 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(RAND, 8, true), new PerlinOctaveNoise(RAND, 8, true));
            
            sandNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
            gravelNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
            
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
                     int dirtThickness = (int)(noise5.sampleIndevOctaves(x, z) / 24.0) - 4;
                 
                     int stoneTransition = dirtTransition + dirtThickness;
                     heightmap[x + z * this.width] = Math.max(dirtTransition, stoneTransition);
                     
                     if (heightmap[x + z * this.width] > this.height - 2) {
                         heightmap[x + z * this.width] = this.height - 2;
                     }
                     
                     if (heightmap[x + z * this.width] <= 0) {
                         heightmap[x + z * this.width] = 1;
                     }
                     
                     double var4 = noise6.sampleIndevOctaves(x * 2.3, z * 2.3) / 24.0;
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
            carveTerrain(blockArr);
            floodFluid(blockArr);   
            floodLava(blockArr);
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
                
                double heightLow = heightNoise1.sampleIndevOctavesCombined(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = heightNoise2.sampleIndevOctavesCombined(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightCheck = heightNoise3.sampleIndevOctaves(x, z) / 8.0;
                
                if (heightCheck > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                if (this.type == Type.ISLAND) {
                    double islandVar3 = Math.sqrt(islandVar1 * islandVar1 + islandVar2 * islandVar2) * 1.2000000476837158;
                    islandVar3 = Math.min(islandVar3, islandNoise.sampleIndevOctaves(x * 0.05f, z * 0.05f) / 4.0 + 1.0);
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
                double var1 = erodeNoise1.sampleIndevOctavesCombined(x << 1, z << 1) / 8.0;
                int var2 = erodeNoise2.sampleIndevOctavesCombined(x << 1, z << 1) > 0.0 ? 1 : 0;
            
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
                boolean genSand = sandNoiseOctaves.sampleIndevOctaves(x, z) > 8.0;
                boolean genGravel = gravelNoiseOctaves.sampleIndevOctaves(x, z) > 12.0;
                
                if (this.type == Type.ISLAND) {
                    genSand = sandNoiseOctaves.sampleIndevOctaves(x, z) > -8.0;
                }
                
                if (this.theme == Theme.PARADISE) {
                    genSand = sandNoiseOctaves.sampleIndevOctaves(x, z) > -32.0;
                }
                
                if (this.theme == Theme.HELL || this.theme == Theme.WOODS) {
                    genSand = sandNoiseOctaves.sampleIndevOctaves(x, z) > -8.0;
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
                        surfaceBlock = (this.theme == Theme.HELL) ? Blocks.GRASS_BLOCK : Blocks.SAND; 
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
        
        if (this.type == Type.FLOATING) {
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
        
        if (this.type == Type.FLOATING) {
            return;
        }

        //int lavaSourceCount = this.width * this.length * this.height / 2000;
        int lavaSourceCount = this.width * this.length * this.height / 20000;
        //int lavaHeight = this.groundLevel;
        
        for (int i = 0; i < lavaSourceCount; ++i) {
            int randX = RAND.nextInt(this.width);
            int randZ = RAND.nextInt(this.length);
            int randY = (int)((this.waterLevel - 3) * RAND.nextFloat() * RAND.nextFloat());
            /*
            int randY = Math.min(
                Math.min(RAND.nextInt(lavaHeight), RAND.nextInt(lavaHeight)),
                Math.min(RAND.nextInt(lavaHeight), RAND.nextInt(lavaHeight)));
            */
            
            flood(blockArr, randX, randY, randZ, Blocks.LAVA);
        }
        
    }
    
    private void flood(Block[][][] blockArr, int x, int y, int z, Block toFill) {
        ArrayList<Vec3d> posToFill = new ArrayList<Vec3d>();
        posToFill.add(new Vec3d(x, y, z));
        
        while (posToFill.size() > 0) {
            Vec3d curPos = posToFill.get(0);
            x = (int)curPos.x;
            y = (int)curPos.y;
            z = (int)curPos.z;
            
            Block someBlock = blockArr[x][y][z];

            if (someBlock == Blocks.AIR) {
                blockArr[x][y][z] = toFill;
                
                if (y - 1 >= 0)          posToFill.add(new Vec3d(x, y - 1, z));
                if (x - 1 >= 0)          posToFill.add(new Vec3d(x - 1, y, z));
                if (x + 1 < this.width)  posToFill.add(new Vec3d(x + 1, y, z));
                if (z - 1 >= 0)          posToFill.add(new Vec3d(x, y, z - 1));
                if (z + 1 < this.length) posToFill.add(new Vec3d(x, y, z + 1));
            }
            
            posToFill.remove(0);
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
        BlockState topBlock = BlockStates.GRASS_BLOCK;
        
        if (this.theme == Theme.HELL) topBlock = BlockStates.DIRT;
         
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.BEDROCK, false);
                    } else if (y == this.waterLevel) {
                        chunk.setBlockState(POS.set(x, y, z), topBlock, false);
                    }
                }
            }
        }
    }
    
    private void generateWaterBorder(Chunk chunk) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel - 10) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.BEDROCK, false);
                    } else if (y == this.waterLevel - 10) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.DIRT, false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(POS.set(x, y, z), this.fluidBlock, false);
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
    
    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
        // Do nothing, for now.
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int height = this.height - 1;
        
        x = x + this.width / 2;
        z = z + this.length / 2;
        
        if (x < 0 || x >= this.width || z < 0 || z >= this.length) return waterLevel;
        
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
        
        if (height <= this.waterLevel) height = this.waterLevel;
         
        return height;
    }

    @Override
    public int getWorldHeight() {
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
    
    public int getSpawnX() {
        return this.spawnX;
    }
    
    public int getSpawnZ() {
        return this.spawnZ;
    }
    
    public Theme getTheme() {
        return this.theme;
    }

}
