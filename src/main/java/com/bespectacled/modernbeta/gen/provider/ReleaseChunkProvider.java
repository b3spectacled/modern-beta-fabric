package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.biome.provider.ReleaseBiomeProvider;
import com.bespectacled.modernbeta.biome.release.BiomeLayer;
import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;
import com.bespectacled.modernbeta.biome.release.TestVanillaBiomeLayer;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

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
public class ReleaseChunkProvider extends AbstractChunkProvider {
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
    
    private final double heightNoise[];
    
    private final ReleaseBiomeProvider popBiomeProvider;
    private final ReleaseBiome releaseBiomes[];
    
    private static final ReleaseBiome[] BIOMES = new ReleaseBiome[256];
    
    private static final float[] BIOME_WEIGHT = new float[25];
    
    private final int verticalNoiseResolution;
    private final int horizontalNoiseResolution;
    
    private final int noiseSizeX;
    private final int noiseSizeZ;
    private final int noiseSizeY;
    
    private final boolean generateBeaches;
    
    public ReleaseChunkProvider(long seed, CompoundTag settings) {
        super(seed, 128, 63);
        
        this.popBiomeProvider = new ReleaseBiomeProvider(seed);
        this.releaseBiomes = new ReleaseBiome[10 * 10];
        
        this.verticalNoiseResolution = 2 * 4;
        this.horizontalNoiseResolution = 1 * 4;
        
        this.noiseSizeX = 16 / this.horizontalNoiseResolution;
        this.noiseSizeZ = 16 / this.horizontalNoiseResolution;
        this.noiseSizeY = this.worldHeight / this.verticalNoiseResolution;
        
        this.heightNoise = new double[(this.noiseSizeX + 1) * (this.noiseSizeZ + 1) * (this.noiseSizeY + 1)];
        
        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, true);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, true);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, true);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, true);
        
        setForestOctaves(forestNoiseOctaves);
        
        this.generateBeaches = settings.contains("generateBeaches") ? settings.getBoolean("generateBeaches") : false;
        
        beachNoiseOctaves = this.generateBeaches ? new PerlinOctaveNoise(RAND, 4, true) : null;
        
    }

    @Override
    public void makeChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        this.popBiomeProvider.getBiomesForNoiseGen(this.releaseBiomes, chunk.getPos().x * 4 - 2, chunk.getPos().z * 4 - 2, 10, 10);
        generateTerrain(chunk, structureAccessor);
    }
    
    @Override
    public void makeSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        this.popBiomeProvider.getBiomeGenAt(BIOMES, chunk.getPos().getStartX(), chunk.getPos().getStartZ(), 16, 16, true);
        
        Biome curBiome;
      
        if (this.generateBeaches) {
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
        }
        
        stoneNoise = stoneNoiseOctaves.sampleArrBeta(
            stoneNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D
        );

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                boolean genSandBeach = false;
                boolean genGravelBeach = false;

                if (this.generateBeaches) {
                    genSandBeach = sandNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 0.0D;
                    genGravelBeach = gravelNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 3D;
                }
                
                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                    
                curBiome = biomeSource.isRelease() ? 
                    biomeSource.getBiomeRegistry().get(BIOMES[x + z * 16].getVanillaId()) :
                    region.getBiome(POS.set(absX, 0, absZ));    

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                    
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = this.worldHeight - 1; y >= 0; y--) {

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
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
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

                        if (y < this.seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = BlockStates.WATER;
                        }

                        flag = genStone;
                        if (y >= this.seaLevel - 1) {
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
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            this.popBiomeProvider.getBiomesForNoiseGen(this.releaseBiomes, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);
        
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
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        generateHeightmap(chunk.getPos().x * this.noiseSizeX, 0, chunk.getPos().z * this.noiseSizeZ);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; subChunkY++) {
                    double eighth = 0.125D;

                    double lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double upperNW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNW) * eighth; 
                    double upperSW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSW) * eighth;
                    double upperNE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNE) * eighth;
                    double upperSE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSE) * eighth;

                    for (int subY = 0; subY < this.verticalNoiseResolution; subY++) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        
                        double quarter = 0.25D;
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * quarter; // Lerp
                        double avgS = (lowerSE - lowerSW) * quarter;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = (subX + subChunkX * this.horizontalNoiseResolution);
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * quarter; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = (subChunkZ * this.horizontalNoiseResolution + subZ);
                                int absZ = (chunk.getPos().z << 4) + z;
                               
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);

                                BlockState blockToSet = getBlockState(clampedDensity, y, 0);

                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                density += progress;
                            }

                            curNW += avgN;
                            curSW += avgS;
                        }

                        lowerNW += upperNW;
                        lowerSW += upperSW;
                        lowerNE += upperNE;
                        lowerSE += upperSE;
                    }
                }
            }
        }
    }

    private void generateHeightmap(int x, int y, int z) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionX = this.noiseSizeX + 1;
        int noiseResolutionZ = this.noiseSizeZ + 1;

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
        
        double heightStretch = 12D;

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

        //x = z = 0;
        int heightNoiseNdx = 0;
        int flatNoiseNdx = 0;
        
        for (int noiseX = 0; noiseX < noiseResolutionX; noiseX++) {
            for (int noiseZ = 0; noiseZ < noiseResolutionZ; noiseZ++) {
                float biomeMaxHeight = 0.0F;
                float biomeMinHeight = 0.0F;
                float totalWeight = 0.0F;
                
                ReleaseBiome biome0 = this.releaseBiomes[noiseX + 2 + (noiseZ + 2) * (noiseResolutionX + 5)];

                for (int bX = -2; bX <= 2; bX++) {
                    for (int bZ = -2; bZ <= 2; bZ++) {
                        ReleaseBiome biome1 = this.releaseBiomes[noiseX + bX + 2 + (noiseZ + bZ + 2) * (noiseResolutionX + 5)];

                        float scale0 = biome0.getScale();
                        float depth0 = biome0.getDepth();
                        
                        float scale1 = biome1.getScale();
                        float depth1 = biome1.getDepth();
                        
                        float biomeHeightWeight = BIOME_WEIGHT[bX + 2 + (bZ + 2) * 5] / (depth1 + 2.0F);
                        if (depth1 > depth0) {
                            biomeHeightWeight /= 2.0F;
                        }
                        biomeMaxHeight += scale1 * biomeHeightWeight; // maxHeight = scale
                        biomeMinHeight += depth1 * biomeHeightWeight; // minHeight = depth
                        totalWeight += biomeHeightWeight;
                    }
                }

                biomeMaxHeight /= totalWeight;
                biomeMinHeight /= totalWeight;
                biomeMaxHeight = biomeMaxHeight * 0.9F + 0.1F;
                biomeMinHeight = (biomeMinHeight * 4F - 1.0F) / 8F;
                double depthVal = depthNoise[flatNoiseNdx] / 8000D;
                if (depthVal < 0.0D)
                {
                    depthVal = -depthVal * 0.29999999999999999D;
                }
                depthVal = depthVal * 3D - 2D;
                
                if (depthVal < 0.0D)
                {
                    depthVal /= 2D;
                    if (depthVal < -1D) {
                        depthVal = -1D;
                    }
                    
                    depthVal /= 1.3999999999999999D;
                    depthVal /= 2D;
                    
                } else {
                    if (depthVal > 1.0D) {
                        depthVal = 1.0D;
                    }
                    depthVal /= 8D;
                }
                
                flatNoiseNdx++;
                
                for (int noiseY = 0; noiseY < noiseResolutionY; noiseY++)
                {
                    double curMinHeight = biomeMinHeight;
                    double curMaxHeight = biomeMaxHeight;
                    
                    curMinHeight += depthVal * 0.20000000000000001D;
                    curMinHeight = (curMinHeight * (double)noiseResolutionY) / 16D;
                    
                    double depthVal2 = (double)noiseResolutionY / 2D + curMinHeight * 4D;
                    double heightVal = 0.0D;
                    double scaleVal2 = (((double)noiseY - depthVal2) * heightStretch * 128D) / this.getWorldHeight() / curMaxHeight;
                    
                    if (scaleVal2 < 0.0D)
                    {
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
                    
                    if (noiseY > noiseResolutionY - 4)
                    {
                        double d11 = (float)(noiseY - (noiseResolutionY - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d11) + -10D * d11;
                    }
                    
                    heightNoise[heightNoiseNdx] = heightVal;
                    heightNoiseNdx++;
                }
            }
        }
    }

    private void sampleHeightmap(int sampleX, int sampleZ) {
        int noiseResolutionY = this.noiseSizeY + 1;
        int noiseResolutionXZ = this.noiseSizeX + 1;
        
        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;

        generateHeightmap(chunkX * this.noiseSizeX, 0, chunkZ * this.noiseSizeZ);

        for (int subChunkX = 0; subChunkX < this.noiseSizeX; subChunkX++) {
            for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; subChunkZ++) {
                for (int subChunkY = 0; subChunkY < this.noiseSizeY; subChunkY++) {
                    double eighth = 0.125D;

                    double lowerNW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSW = heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerNE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 0)];
                    double lowerSE = heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 0)];

                    double upperNW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNW) * eighth; 
                    double upperSW = (heightNoise[((subChunkX + 0) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSW) * eighth;
                    double upperNE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 0)) * noiseResolutionY + (subChunkY + 1)] - lowerNE) * eighth;
                    double upperSE = (heightNoise[((subChunkX + 1) * noiseResolutionXZ + (subChunkZ + 1)) * noiseResolutionY + (subChunkY + 1)] - lowerSE) * eighth;

                    for (int subY = 0; subY < this.verticalNoiseResolution; subY++) {
                        int y = subChunkY * this.verticalNoiseResolution + subY;
                        
                        double quarter = 0.25D;
                        double curNW = lowerNW;
                        double curSW = lowerSW;
                        double avgN = (lowerNE - lowerNW) * quarter; // Lerp
                        double avgS = (lowerSE - lowerSW) * quarter;
                        
                        for (int subX = 0; subX < this.horizontalNoiseResolution; subX++) {
                            int x = (subX + subChunkX * this.horizontalNoiseResolution);
                            
                            double density = curNW; // var15
                            double progress = (curSW - curNW) * quarter; 

                            for (int subZ = 0; subZ < this.horizontalNoiseResolution; subZ++) {
                                int z = (subChunkZ * this.horizontalNoiseResolution + subZ);
                                
                                if (density > 0.0) {
                                    CHUNK_Y[x][z] = y;
                                }

                                density += progress;
                            }

                            curNW += avgN;
                            curSW += avgS;
                        }

                        lowerNW += upperNW;
                        lowerSW += upperSW;
                        lowerNE += upperNE;
                        lowerSE += upperSE;
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
    
    static {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                float f = 10F / MathHelper.sqrt((float)(i * i + j * j) + 0.2F);
                BIOME_WEIGHT[i + 2 + (j + 2) * 5] = f;
            }
        }
    }
    
}
