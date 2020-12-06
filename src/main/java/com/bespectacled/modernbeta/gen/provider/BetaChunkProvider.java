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
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public class BetaChunkProvider extends AbstractChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private double sandNoise[];
    private double gravelNoise[];
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
    
    public BetaChunkProvider(long seed) {
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
        byte seaLevel = (byte) 64;
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        BiomeUtil.sampleTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        BetaBiomes.getBiomesFromLookup(TEMPS, HUMIDS, BIOMES, null);
        
        Biome curBiome;

        sandNoise = beachNoiseOctaves.sampleArrBeta(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            thirtysecond, thirtysecond, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrBeta(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            thirtysecond, 1.0D, thirtysecond);
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(
            stoneNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                boolean genSandBeach = sandNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                    
                curBiome = biomeSource.isBeta() ? 
                    biomeSource.getBiomeRegistry().get(BIOMES[z + x * 16]) :
                    region.getBiome(POS.set(absX, 0, absZ));    

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = 127; y >= 0; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + RAND.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }

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
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = BlockStates.WATER;
                        }

                        flag = genStone;
                        if (y >= seaLevel - 1) {
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
    public int getHeight(int x, int z, Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            BiomeUtil.sampleTempHumid((x >> 4) << 4, (z >> 4) << 4, TEMPS, HUMIDS);
            
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);
        
        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < 64)
            groundHeight = 64;

        return groundHeight;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return this.beachNoiseOctaves;
    }
    
    private void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte noiseResolutionY = 17;
        int noiseResolutionXZ = 5;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * 4, 0, chunk.getPos().z * 4);
        
        for (int subChunkX = 0; subChunkX < 4; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < 4; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < 16; subChunkY++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var2 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double var3 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var4 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double var5 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var1) * eighth; 
                    double var6 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var2) * eighth;
                    double var7 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var3) * eighth;
                    double var8 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double quarter = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * quarter; // Lerp
                        double var13 = (var4 - var2) * quarter;
                        
                        for (int m = 0; m < 4; m++) {
                            int x = (m + subChunkX * 4);
                            int y = subChunkY * 8 + l;
                            int z = (subChunkZ * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; 

                            int absX = (chunk.getPos().x << 4) + subChunkX * 4 + m;

                            for (int n = 0; n < 4; n++) {
                                int absZ = (chunk.getPos().z << 4) + subChunkZ * 4 + n;
                                double temp = temps[(subChunkX * 4 + m) * 16 + (subChunkZ * 4 + n)];
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);

                                BlockState blockToSet = getBlockState(clampedDensity, y, temp);

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
        byte noiseResolutionY = 17;
        int noiseResolutionX = 5;
        int noiseResolutionZ = 5;

        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D; 
        double heightScale = 684.41200000000003D;

        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = 80D;
        double mainNoiseScaleY = 160D;
        double mainNoiseScaleZ = 80D;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        // Scale and Depth noise sample in 2D, noiseResolutionX * noiseResolutionZ
        
        scaleNoise = scaleNoiseOctaves.sampleArrBeta(
            scaleNoise, 
            x, z, 
            noiseResolutionX, noiseResolutionZ, 
            1.121D, 1.121D, 0.5D
        );
        
        depthNoise = depthNoiseOctaves.sampleArrBeta(
            depthNoise, 
            x, z, 
            noiseResolutionX, noiseResolutionZ, 
            depthNoiseScaleX, depthNoiseScaleZ, depthNoiseScaleExponent
        );

        // Main, Min Limit, and Max Limit noise sample in 3D, noiseResolutionX * noiseResolutionY * noiseResolutionZ
        
        mainNoise = mainNoiseOctaves.sampleArrBeta(
            mainNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoise = minLimitNoiseOctaves.sampleArrBeta(
            minLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoise = maxLimitNoiseOctaves.sampleArrBeta(
            maxLimitNoise, 
            x, y, z, 
            noiseResolutionX, noiseResolutionY, noiseResolutionZ,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int heightNoiseNdx = 0;
        int flatNoiseNdx = 0;
        int k = 16 / noiseResolutionX;

        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            int relX = noiseX * k + k / 2;

            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                int relZ = noiseZ * k + k / 2;

                double curTemp = TEMPS[relX * 16 + relZ];
                double curHumid = HUMIDS[relX * 16 + relZ] * curTemp;

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

                if (depthVal < 0.0D) {
                    depthVal /= 2D;

                    if (depthVal < -1D) {
                        depthVal = -1D;
                    }

                    depthVal /= 1.3999999999999999D;
                    depthVal /= 2D;

                    scaleVal = 0.0D;

                } else {
                    if (depthVal > 1.0D) {
                        depthVal = 1.0D;
                    }
                    depthVal /= 8D;
                }

                if (scaleVal < 0.0D) {
                    scaleVal = 0.0D;
                }

                scaleVal += 0.5D;
                depthVal = (depthVal * (double) noiseResolutionY) / 16D;

                double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;

                flatNoiseNdx++;

                for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++) {
                    double heightVal = 0.0D;
                    double scaleVal2 = (((double) noiseY - depthVal2) * 12D) / scaleVal;

                    if (scaleVal2 < 0.0D) {
                        scaleVal2 *= 4D;
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
                    heightVal -= scaleVal2;

                    if (noiseY > noiseResolutionY - 4) {
                        double d13 = (float) (noiseY - (noiseResolutionY - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d13) + -10D * d13;
                    }
                    
                    HEIGHT_NOISE[heightNoiseNdx] = heightVal;
                    heightNoiseNdx++;
                }
            }
        }
    }

    private void sampleHeightmap(int sampleX, int sampleZ) {
        byte noiseResolutionY = 17;
        int noiseResolutionXZ = 5;
        
        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;

        generateHeightmap(chunkX * 4, 0, chunkZ * 4);
        
        for (int subChunkX = 0; subChunkX < 4; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < 4; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < 16; subChunkY++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var2 = HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double var3 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double var4 = HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double var5 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var1) * eighth;
                    double var6 = (HEIGHT_NOISE[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var2) * eighth;
                    double var7 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - var3) * eighth;
                    double var8 = (HEIGHT_NOISE[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double var9 = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * var9;
                        double var13 = (var4 - var2) * var9;

                        for (int m = 0; m < 4; m++) {
                            int x = (m + subChunkX * 4);
                            int y = subChunkY * 8 + l;
                            int z = (subChunkZ * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14;

                            for (int n = 0; n < 4; n++) {
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
