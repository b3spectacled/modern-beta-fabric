package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public class SkylandsChunkProvider extends AbstractChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;

    private double stoneNoise[];

    private double mainNoise[]; 
    private double minLimitNoise[];
    private double maxLimitNoise[];

    private double scaleNoise[];
    private double depthNoise[];
    
    private static final double HEIGHT_NOISE[] = new double[425];
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];
    
    private static final Identifier[] BIOMES = new Identifier[256];
    
    public SkylandsChunkProvider(long seed) {
        super(seed);

        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);

        setForestOctaves(forestNoiseOctaves);
    }

    @Override
    public void makeChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        BiomeUtil.sampleTempHumid(chunk.getPos().x << 4, chunk.getPos().z << 4, TEMPS, HUMIDS);
        generateTerrain(chunk, TEMPS, structureAccessor);
    }

    @Override
    public void makeSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double thirtysecond = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        BiomeUtil.sampleTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        BetaBiomes.getBiomesFromLookup(TEMPS, HUMIDS, BIOMES, null);
        
        Biome curBiome;
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;

                curBiome = biomeSource.isBeta() ? 
                        biomeSource.getBiomeRegistry().get(BIOMES[z + x * 16]) :
                        region.getBiome(POS.set(absX, 0, absZ));

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = 127; y >= 0; y--) {

                    Block someBlock = chunk.getBlockState(POS.set(x, y, z)).getBlock();

                    if (someBlock.equals(Blocks.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(Blocks.STONE)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (genStone <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = BlockStates.STONE;
                        }

                        flag = genStone;
                        if (y >= 0) {
                            chunk.setBlockState(POS.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(POS.set(x, y, z), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(POS.set(x, y, z), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = RAND.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
        
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        fillChunkY(16);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            BiomeUtil.sampleTempHumid((x >> 4) << 4, (z >> 4) << 4, TEMPS, HUMIDS);
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);

        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
    
    private static void fillChunkY(int y) {
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                CHUNK_Y[x][z] = y;
            }
        }
    }
    
    public void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte noiseResolutionY = 33;
        int noiseResolutionXZ = 3;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * 2, 0, chunk.getPos().z * 2);

        for (int subChunkX = 0; subChunkX < 2; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < 2; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < 32; subChunkY++) {

                    double quarter = 0.25D;

                    double var1 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var2 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double var3 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var4 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double var5 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var1) * quarter;
                    double var6 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var2) * quarter;
                    double var7 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var3) * quarter;
                    double var8 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var4) * quarter;

                    for (int l = 0; l < 4; l++) {
                        double eighth = 0.125D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * eighth; 
                        double var13 = (var4 - var2) * eighth;

                        for (int m = 0; m < 8; m++) {
                            int x = (m + subChunkX * 8);
                            int y = subChunkY * 4 + l;
                            int z = (subChunkZ * 8);

                            double var14 = 0.125D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; 
                            
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            for (int n = 0; n < 8; n++) {
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);

                                BlockState blockToSet = clampedDensity > 0.0 ? BlockStates.STONE : BlockStates.AIR;

                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }
    }
    
    private void generateHeightmap(int x, int y, int z) {
        byte noiseResolutionY = 33;
        int noiseResolutionX = 3;
        int noiseResolutionZ = 3;

        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D; // d
        double heightScale = 684.41200000000003D; // d1

        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = 80D;
        double mainNoiseScaleY = 160D;
        double mainNoiseScaleZ = 80D;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        double temps[] = TEMPS;
        double humids[] = HUMIDS;

        scaleNoise = scaleNoiseOctaves.sampleArrBeta(scaleNoise, x, z, noiseResolutionX, noiseResolutionZ, 1.121D, 1.121D, 0.5D);
        depthNoise = depthNoiseOctaves.sampleArrBeta(depthNoise, x, z, noiseResolutionX, noiseResolutionZ, depthNoiseScaleX, depthNoiseScaleZ,
                depthNoiseScaleExponent);

        coordinateScale *= 2D;

        mainNoise = mainNoiseOctaves.sampleArrBeta(mainNoise, x, y, z, noiseResolutionX, noiseResolutionY, noiseResolutionZ,
                coordinateScale / mainNoiseScaleX, heightScale / mainNoiseScaleY, coordinateScale / mainNoiseScaleZ);

        minLimitNoise = minLimitNoiseOctaves.sampleArrBeta(minLimitNoise, x, y, z, noiseResolutionX, noiseResolutionY, noiseResolutionZ,
                coordinateScale, heightScale, coordinateScale);

        maxLimitNoise = maxLimitNoiseOctaves.sampleArrBeta(maxLimitNoise, x, y, z, noiseResolutionX, noiseResolutionY, noiseResolutionZ,
                coordinateScale, heightScale, coordinateScale);

        int heightNoiseNdx = 0;
        int flatNoiseNdx = 0;
        int k = 16 / noiseResolutionX;

        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            int relX = noiseX * k + k / 2;

            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                int relZ = noiseZ * k + k / 2;

                double curTemp = temps[relX * 16 + relZ];
                double curHumid = humids[relX * 16 + relZ] * curTemp;

                double humidVal = 1.0D - curHumid;
                humidVal *= humidVal;
                humidVal *= humidVal;
                humidVal = 1.0D - humidVal;

                double scaleVal = (scaleNoise[flatNoiseNdx] + 256D) / 512D;
                scaleVal *= humidVal;

                if (scaleVal > 1.0D) {
                    scaleVal = 1.0D;
                }

                double depthVal = depthNoise[flatNoiseNdx] / 8000D;

                if (depthVal < 0.0D) {
                    depthVal = -depthVal * 0.29999999999999999D;
                }

                depthVal = depthVal * 3D - 2D;

                if (depthVal > 1.0D) {
                    depthVal = 1.0D;
                }

                depthVal /= 8D;
                depthVal = 0.0D;

                if (scaleVal < 0.0D) {
                    scaleVal = 0.0D;
                }

                scaleVal += 0.5D;
                depthVal = (depthVal * (double) noiseResolutionY) / 16D;

                double depthVal2 = (double) noiseResolutionY / 16D;

                flatNoiseNdx++;

                for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    double scaleVal2 = (((double) noiseY - depthVal2) * 8D) / scaleVal;

                    if (scaleVal2 < 0.0D) {
                        scaleVal2 *= -1D;
                    }

                    double minLimitVal = minLimitNoise[heightNoiseNdx] / lowerLimitScale;
                    double maxLimitVal = maxLimitNoise[heightNoiseNdx] / upperLimitScale;
                    double mainNoiseVal = (mainNoise[heightNoiseNdx] / 10D + 1.0D) / 2D;

                    if (mainNoiseVal < 0.0D) {
                        heightVal = minLimitVal;
                    } else if (mainNoiseVal > 1.0D) {
                        heightVal = maxLimitVal;
                    } else {
                        heightVal = minLimitVal + (maxLimitVal - minLimitVal) * mainNoiseVal;
                    }
                    heightVal -= 8D;
                    int int_32 = 32;

                    if (noiseY > noiseResolutionY - int_32) {
                        double d13 = (float) (noiseY - (noiseResolutionY - int_32)) / ((float) int_32 - 1.0F);
                        heightVal = heightVal * (1.0D - d13) + -30D * d13;
                    }

                    int_32 = 8;
                    if (noiseY < int_32) {
                        double d14 = (float) (int_32 - noiseY) / ((float) int_32 - 1.0F);
                        heightVal = heightVal * (1.0D - d14) + -30D * d14;
                    }

                    HEIGHT_NOISE[heightNoiseNdx] = heightVal;
                    heightNoiseNdx++;
                }
            }
        }
    }
    
    private void sampleHeightmap(int absX, int absZ) {
        byte noiseResolutionY = 33;
        int noiseResolutionXZ = 3;
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;

        generateHeightmap(chunkX * 2, 0, chunkZ * 2);

        for (int subChunkX = 0; subChunkX < 2; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < 2; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < 16; subChunkY++) {
                    double quarter = 0.25D;

                    double var1 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var2 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double var3 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var4 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double var5 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var1) * quarter;
                    double var6 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var2) * quarter;
                    double var7 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var3) * quarter;
                    double var8 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var4) * quarter;

                    for (int l = 0; l < 4; l++) {
                        double eighth = 0.125D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * eighth; // Lerp
                        double var13 = (var4 - var2) * eighth;

                        for (int m = 0; m < 8; m++) {
                            int x = (m + subChunkX * 8);
                            int y = subChunkY * 4 + l;
                            int z = (subChunkZ * 8);

                            double var14 = 0.125D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; // More lerp

                            for (int n = 0; n < 8; n++) {
                                if (density > 0.0) {
                                    CHUNK_Y[x][z] = y;
                                }

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }

        for (int pX = 0; pX < CHUNK_Y.length; pX++) {
            for (int pZ = 0; pZ < CHUNK_Y[pX].length; pZ++) {
                BlockPos structPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                
                GROUND_CACHE_Y.put(structPos, CHUNK_Y[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
    }

}
