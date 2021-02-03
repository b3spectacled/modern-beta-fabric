package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.GenUtil;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;

/**
 * 
 * @author Paulevs
 *
 */
public class InfdevChunkProvider extends AbstractChunkProvider {
    
    private final PerlinOctaveNoise noiseOctavesA;
    private final PerlinOctaveNoise noiseOctavesB;
    private final PerlinOctaveNoise noiseOctavesC;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final double heightNoise[];
    
    public InfdevChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, 0, 128, 64, 0, -10, 1, 1, 1.0, 1.0, 80, 160, BlockStates.STONE, BlockStates.WATER, settings.providerSettings);
        
        this.heightNoise = new double[(this.noiseSizeY + 1) * this.noiseSizeX];
        
        // Noise Generators
        noiseOctavesA = new PerlinOctaveNoise(RAND, 16, true);
        noiseOctavesB = new PerlinOctaveNoise(RAND, 16, true);
        noiseOctavesC = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);

        new PerlinOctaveNoise(RAND, 5, true); // Unused in original source
        
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 5, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        generateTerrain(chunk, structureAccessor); 
    }

    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double thirtysecond = 0.03125D; // eighth
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        // TODO: Really should be pooled or something
        ChunkRandom rand = this.createChunkRand(chunkX, chunkZ);
        ChunkRandom sandstoneRand = this.createChunkRand(chunkX, chunkZ);
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                
                boolean genSandBeach = this.beachNoiseOctaves.sample(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + rand.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sample(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + rand.nextDouble() * 0.2 > 3.0;
                
                int genStone = (int)(this.stoneNoiseOctaves.sample(
                    absX * thirtysecond * 2.0, 
                    absZ * thirtysecond * 2.0) / 3.0 + 3.0 + rand.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome curBiome = getBiomeForSurfaceGen(POS.set(absX, 0, absZ), region, biomeSource);
                
                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                for (int y = this.worldHeight - 1; y >= 0; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + rand.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    POS.set(x, y, z);
                    BlockState someBlock = chunk.getBlockState(POS);
                    
                    if (someBlock.equals(BlockStates.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(this.defaultBlock)) {
                        if (flag == -1) {
                            if (genStone <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = this.defaultBlock;
                                
                            } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) {
                                topBlock = biomeTopBlock;
                                fillerBlock = biomeFillerBlock;
                                
                                if (genGravelBeach) {
                                    topBlock = BlockStates.AIR;
                                    fillerBlock = BlockStates.GRAVEL;
                                }
                                
                                if (genSandBeach) {
                                    topBlock = BlockStates.SAND;
                                    fillerBlock = BlockStates.SAND;
                                }
                            }
                            
                            if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) {
                                topBlock = this.defaultFluid;
                            }
                            
                            flag = genStone;
                            
                            if (y >= this.seaLevel - 1) {
                                chunk.setBlockState(POS, topBlock, false);
                            } else {
                                chunk.setBlockState(POS, fillerBlock, false);
                            }
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(POS, fillerBlock, false);
                            
                            // Gens layer of sandstone starting at lowest block of sand, of height 1 to 4.
                            // Beta backport.
                            if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                                flag = sandstoneRand.nextInt(4);
                                fillerBlock = BlockStates.SANDSTONE;
                            }
                        }
                    }
                }
            }
        }
        
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (HEIGHTMAP_CACHE.get(structPos) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = HEIGHTMAP_CACHE.get(structPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.seaLevel)
            groundHeight = this.seaLevel;

        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return this.beachNoiseOctaves;
    }
    
    private void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int bX = chunkX * this.noiseSizeX + subChunkX;
                int bZ = chunkZ * this.noiseSizeZ + subChunkZ;
                
                for (int bY = 0; bY < this.noiseSizeY + 1; ++bY) {
                    this.heightNoise[bY * this.noiseSizeX + 0] = this.generateHeightmap(bX, bY, bZ);
                    this.heightNoise[bY * this.noiseSizeX + 1] = this.generateHeightmap(bX, bY, bZ + 1);
                    this.heightNoise[bY * this.noiseSizeX + 2] = this.generateHeightmap(bX + 1, bY, bZ);
                    this.heightNoise[bY * this.noiseSizeX + 3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int subChunkY = this.noiseMinY; subChunkY < this.noiseSizeY; ++subChunkY) {
                    
                    double lower0, lower1, lower2, lower3;
                    double upper0, upper1, upper2, upper3;
                    
                    lower0 = lower1 = lower2 = lower3 = 1.0;
                    upper0 = upper1 = upper2 = upper3 = 1.0;
                    
                    if (subChunkY >= 0) {
                        lower0 = this.heightNoise[(subChunkY) * this.noiseSizeX + 0];
                        lower1 = this.heightNoise[(subChunkY) * this.noiseSizeX + 1];
                        lower2 = this.heightNoise[(subChunkY) * this.noiseSizeX + 2];
                        lower3 = this.heightNoise[(subChunkY) * this.noiseSizeX + 3];
                        
                        upper0 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 0];
                        upper1 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 1];
                        upper2 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 2];
                        upper3 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 3];
                    }
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        
                        double lerpY = subY / (double)this.verticalNoiseResolution;
                        
                        double nx1 = lower0 + (upper0 - lower0) * lerpY;
                        double nx2 = lower1 + (upper1 - lower1) * lerpY;
                        double nx3 = lower2 + (upper2 - lower2) * lerpY;
                        double nx4 = lower3 + (upper3 - lower3) * lerpY;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double lerpX = subX / (double)this.horizontalNoiseResolution;
                            
                            double nz1 = nx1 + (nx3 - nx1) * lerpX;
                            double nz2 = nx2 + (nx4 - nx2) * lerpX;
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double lerpZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = nz1 + (nz2 - nz1) * lerpZ;
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);
                                
                                BlockState blockToSet = getBlockState(clampedDensity, y);
                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private double generateHeightmap(double x, double y, double z) {
        // Check if y (in scaled space) is below sealevel
        // and increase density accordingly.
        //double elevGrad = y * 4.0 - 64.0;
        double elevGrad = y * this.verticalNoiseResolution - (double)this.seaLevel;
        if (elevGrad < 0.0) {
            elevGrad *= 3.0;
        }
        
        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 984.412D * this.yScale;
        
        double limitScale = 512.0D;
        
        double res;
        double noise = this.noiseOctavesC.sample(x * 8.55515, y * 1.71103, z * 8.55515) / 2.0;
        
        if (noise < -1) {
            res = MathHelper.clamp(
                this.noiseOctavesA.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else if (noise > 1.0) {
            res = MathHelper.clamp(
                this.noiseOctavesB.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else {
            double noise2 = MathHelper.clamp(
                this.noiseOctavesA.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - elevGrad, 
                -10.0, 
                10.0
            );
            
            double noise3 = MathHelper.clamp(
                this.noiseOctavesB.sample(x * coordinateScale, y * heightScale, z * coordinateScale) / limitScale - elevGrad, 
                -10.0, 
                10.0
            );
            
            double mix = (noise + 1.0) / 2.0;
            
            res = noise2 + (noise3 - noise2) * mix;
        }
        
        return res;
    }
    
    private void sampleHeightmap(int absX, int absZ) {
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;
        
        for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++ subChunkZ) {
                int bX = chunkX * this.noiseSizeX + subChunkX;
                int bZ = chunkZ * this.noiseSizeZ + subChunkZ;
                
                for (int bY = 0; bY < this.noiseSizeY + 1; ++bY) {
                    this.heightNoise[bY * this.noiseSizeX + 0] = this.generateHeightmap(bX, bY, bZ);
                    this.heightNoise[bY * this.noiseSizeX + 1] = this.generateHeightmap(bX, bY, bZ + 1);
                    this.heightNoise[bY * this.noiseSizeX + 2] = this.generateHeightmap(bX + 1, bY, bZ);
                    this.heightNoise[bY * this.noiseSizeX + 3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
                    double lower0 = this.heightNoise[(subChunkY) * this.noiseSizeX + 0];
                    double lower1 = this.heightNoise[(subChunkY) * this.noiseSizeX + 1];
                    double lower2 = this.heightNoise[(subChunkY) * this.noiseSizeX + 2];
                    double lower3 = this.heightNoise[(subChunkY) * this.noiseSizeX + 3];
                    
                    double upper0 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 0];
                    double upper1 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 1];
                    double upper2 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 2];
                    double upper3 = this.heightNoise[(subChunkY + 1) * this.noiseSizeX + 3];
                    
                    for (int subY = 0; subY < this.verticalNoiseResolution; ++subY) {
                        int y = subY + subChunkY * this.verticalNoiseResolution;
                        
                        double mixY = subY / (double)this.verticalNoiseResolution;
                        
                        double nx1 = lower0 + (upper0 - lower0) * mixY;
                        double nx2 = lower1 + (upper1 - lower1) * mixY;
                        double nx3 = lower2 + (upper2 - lower2) * mixY;
                        double nx4 = lower3 + (upper3 - lower3) * mixY;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; ++subX) {
                            int x = subX + subChunkX * this.horizontalNoiseResolution;
                            
                            double mixX = subX / (double)this.horizontalNoiseResolution;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; ++subZ) {
                                int z = subZ + subChunkZ * this.horizontalNoiseResolution;
                                
                                double mixZ = subZ / (double)this.horizontalNoiseResolution;
                                
                                double density = nz1 + (nz2 - nz1) * mixZ;
                                
                                if (density > 0.0) {
                                    HEIGHTMAP_CHUNK[x][z] = y;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int pX = 0; pX < HEIGHTMAP_CHUNK.length; pX++) {
            for (int pZ = 0; pZ < HEIGHTMAP_CHUNK[pX].length; pZ++) {
                BlockPos structPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                
                HEIGHTMAP_CACHE.put(structPos, HEIGHTMAP_CHUNK[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
    }

}
