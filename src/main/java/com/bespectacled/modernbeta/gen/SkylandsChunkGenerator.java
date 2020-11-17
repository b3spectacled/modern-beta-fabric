package com.bespectacled.modernbeta.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;

//private final BetaGeneratorSettings settings;

public class SkylandsChunkGenerator extends NoiseChunkGenerator implements IOldChunkGenerator {

    public static final Codec<SkylandsChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(SkylandsChunkGenerator::new)));

    private final OldGeneratorSettings settings;
    private final BetaBiomeSource biomeSource;
    private final long seed;

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
    
    private boolean generateSkyDim;

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];

    private static final double HEIGHTMAP[] = new double[397];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];
    
    private static final Biome[] BIOMES = new Biome[256];
    public SkylandsChunkGenerator(BiomeSource biomes, long seed, OldGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (BetaBiomeSource) biomes;
        this.seed = seed;

        RAND.setSeed(seed);

        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, false);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);

        if (settings.settings.contains("generateSkyDim"))
            this.generateSkyDim = settings.settings.getBoolean("generateSkyDim");

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "skylands"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Skylands chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return SkylandsChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        BiomeUtil.fetchTempHumid(chunk.getPos().x << 4, chunk.getPos().z << 4, TEMPS, HUMIDS);
        generateTerrain(chunk, TEMPS, structureAccessor);
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
    }

    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        GenUtil.carveWithOcean(this.seed, biomeAccess, chunk, carver, this, biomeSource, FEATURE_RAND, this.getSeaLevel(), false);
    }

    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {

        // Do not use the built-in surface builders..
        // This works better for Beta-accurate surface generation anyway.
        buildBetaSurface(chunkRegion, chunk);
    }

    public void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte byte2 = 2;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte33 = 33;

        int int3_1 = byte2 + 1;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * byte2, 0, chunk.getPos().z * byte2);

        // Noise is sampled in 4x16x4 sections?
        for (int i = 0; i < byte2; i++) {
            for (int j = 0; j < byte2; j++) {
                for (int k = 0; k < 32; k++) {

                    double quarter = 0.25D;

                    double var1 = HEIGHTMAP[((i + 0) * int3_1 + (j + 0)) * byte33 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int3_1 + (j + 1)) * byte33 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int3_1 + (j + 0)) * byte33 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int3_1 + (j + 1)) * byte33 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int3_1 + (j + 0)) * byte33 + (k + 1)] - var1) * quarter;
                    double var6 = (HEIGHTMAP[((i + 0) * int3_1 + (j + 1)) * byte33 + (k + 1)] - var2) * quarter;
                    double var7 = (HEIGHTMAP[((i + 1) * int3_1 + (j + 0)) * byte33 + (k + 1)] - var3) * quarter;
                    double var8 = (HEIGHTMAP[((i + 1) * int3_1 + (j + 1)) * byte33 + (k + 1)] - var4) * quarter;

                    for (int l = 0; l < 4; l++) {
                        double eighth = 0.125D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * eighth; 
                        double var13 = (var4 - var2) * eighth;

                        for (int m = 0; m < 8; m++) {
                            int x = (m + i * 8);
                            int y = k * 4 + l;
                            int z = (j * 8);

                            double var14 = 0.125D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; 
                            
                            int absX = (chunk.getPos().x << 4) + x;
                            
                            for (int n = 0; n < 8; n++) {
                                int absZ = (chunk.getPos().z << 4) + z;
                                double temp = 0;
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                while (structureListIterator.hasNext()) {
                                    StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
                                    BlockBox blockBox = curStructurePiece.getBoundingBox();

                                    int sX = Math.max(0, Math.max(blockBox.minX - absX, absX - blockBox.maxX));
                                    int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece) ? 
                                            ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
                                    int sZ = Math.max(0, Math.max(blockBox.minZ - absZ, absZ - blockBox.maxZ));

                                    clampedDensity += super.getNoiseWeight(sX, sY, sZ) * 0.8;
                                }
                                structureListIterator.back(STRUCTURE_LIST.size());

                                while (jigsawListIterator.hasNext()) {
                                    JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

                                    int jX = absX - curJigsawJunction.getSourceX();
                                    int jY = y - curJigsawJunction.getSourceGroundY();
                                    int jZ = absZ - curJigsawJunction.getSourceZ();

                                    clampedDensity += super.getNoiseWeight(jX, jY, jZ) * 0.4;
                                }
                                jigsawListIterator.back(JIGSAW_LIST.size());


                                BlockState blockToSet = this.getBlockState(clampedDensity, y, temp);

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
        byte byte2 = 2;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte33 = 33;

        int int3_0 = byte2 + 1;
        int int3_1 = byte2 + 1;

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

        scaleNoise = scaleNoiseOctaves.sampleBetaOctaves(scaleNoise, x, z, int3_0, int3_1, 1.121D, 1.121D, 0.5D);
        depthNoise = depthNoiseOctaves.sampleBetaOctaves(depthNoise, x, z, int3_0, int3_1, depthNoiseScaleX, depthNoiseScaleZ,
                depthNoiseScaleExponent);

        coordinateScale *= 2D;

        mainNoise = mainNoiseOctaves.sampleBetaOctaves(mainNoise, x, y, z, int3_0, byte33, int3_1,
                coordinateScale / mainNoiseScaleX, heightScale / mainNoiseScaleY, coordinateScale / mainNoiseScaleZ);

        minLimitNoise = minLimitNoiseOctaves.sampleBetaOctaves(minLimitNoise, x, y, z, int3_0, byte33, int3_1,
                coordinateScale, heightScale, coordinateScale);

        maxLimitNoise = maxLimitNoiseOctaves.sampleBetaOctaves(maxLimitNoise, x, y, z, int3_0, byte33, int3_1,
                coordinateScale, heightScale, coordinateScale);

        int i = 0;
        int j = 0;
        int k = 16 / int3_0;

        for (int l = 0; l < int3_0; l++) {
            int idx0 = l * k + k / 2;

            for (int m = 0; m < int3_1; m++) {
                int idx1 = m * k + k / 2;

                double curTemp = temps[idx0 * 16 + idx1];
                double curHumid = humids[idx0 * 16 + idx1] * curTemp;

                double humidMod = 1.0D - curHumid;
                humidMod *= humidMod;
                humidMod *= humidMod;
                humidMod = 1.0D - humidMod;

                double scaleMod = (scaleNoise[j] + 256D) / 512D;
                scaleMod *= humidMod;

                if (scaleMod > 1.0D) {
                    scaleMod = 1.0D;
                }

                double depthMod = depthNoise[j] / 8000D;

                if (depthMod < 0.0D) {
                    depthMod = -depthMod * 0.29999999999999999D;
                }

                depthMod = depthMod * 3D - 2D;

                if (depthMod > 1.0D) {
                    depthMod = 1.0D;
                }

                depthMod /= 8D;
                depthMod = 0.0D;

                if (scaleMod < 0.0D) {
                    scaleMod = 0.0D;
                }

                scaleMod += 0.5D;
                depthMod = (depthMod * (double) byte33) / 16D;

                double depthMod2 = (double) byte33 / 16D;

                j++;

                for (int n = 0; n < byte33; n++) {
                    double heightVal = 0.0D;
                    double scaleMod2 = (((double) n - depthMod2) * 8D) / scaleMod;

                    if (scaleMod2 < 0.0D) {
                        scaleMod2 *= -1D;
                    }

                    double minLimitMod = minLimitNoise[i] / lowerLimitScale;
                    double maxLimitMod = maxLimitNoise[i] / upperLimitScale;
                    double mainNoiseMod = (mainNoise[i] / 10D + 1.0D) / 2D;

                    if (mainNoiseMod < 0.0D) {
                        heightVal = minLimitMod;
                    } else if (mainNoiseMod > 1.0D) {
                        heightVal = maxLimitMod;
                    } else {
                        heightVal = minLimitMod + (maxLimitMod - minLimitMod) * mainNoiseMod;
                    }
                    heightVal -= 8D;
                    int int_32 = 32;

                    if (n > byte33 - int_32) {
                        double d13 = (float) (n - (byte33 - int_32)) / ((float) int_32 - 1.0F);
                        heightVal = heightVal * (1.0D - d13) + -30D * d13;
                    }

                    int_32 = 8;
                    if (n < int_32) {
                        double d14 = (float) (int_32 - n) / ((float) int_32 - 1.0F);
                        heightVal = heightVal * (1.0D - d14) + -30D * d14;
                    }

                    HEIGHTMAP[i] = heightVal;
                    i++;
                }
            }
        }
    }

    private void buildBetaSurface(ChunkRegion region, Chunk chunk) {
        this.getSeaLevel();
        double thirtysecond = 0.03125D;

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        BiomeUtil.fetchTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        biomeSource.fetchBiomes(TEMPS, HUMIDS, BIOMES, null);
        
        Biome curBiome;
        
        stoneNoise = stoneNoiseOctaves.sampleBetaOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;

                curBiome = this.biomeSource.generateVanillaBiomes() ? region.getBiome(POS.set(absX, 0, absZ)) : BIOMES[z + x * 16];

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

    protected BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;

        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        }

        return blockStateToSet;
    }

    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        fillChunkY(16);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            BiomeUtil.fetchTempHumid((x >> 4) << 4, (z >> 4) << 4, TEMPS, HUMIDS);
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);

        return groundHeight;
    }
    
    private static void fillChunkY(int y) {
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                CHUNK_Y[x][z] = y;
            }
        }
    }

    private void sampleHeightmap(int absX, int absZ) {
        byte byte2 = 2;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte33 = 33;

        int int3_1 = byte2 + 1;
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;

        generateHeightmap(chunkX * byte2, 0, chunkZ * byte2);

        for (int i = 0; i < byte2; i++) {
            for (int j = 0; j < byte2; j++) {
                for (int k = 0; k < 16; k++) {
                    double quarter = 0.25D;

                    double var1 = HEIGHTMAP[((i + 0) * int3_1 + (j + 0)) * byte33 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int3_1 + (j + 1)) * byte33 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int3_1 + (j + 0)) * byte33 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int3_1 + (j + 1)) * byte33 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int3_1 + (j + 0)) * byte33 + (k + 1)] - var1) * quarter;
                    double var6 = (HEIGHTMAP[((i + 0) * int3_1 + (j + 1)) * byte33 + (k + 1)] - var2) * quarter;
                    double var7 = (HEIGHTMAP[((i + 1) * int3_1 + (j + 0)) * byte33 + (k + 1)] - var3) * quarter;
                    double var8 = (HEIGHTMAP[((i + 1) * int3_1 + (j + 1)) * byte33 + (k + 1)] - var4) * quarter;

                    for (int l = 0; l < 4; l++) {
                        double eighth = 0.125D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * eighth; // Lerp
                        double var13 = (var4 - var2) * eighth;

                        for (int m = 0; m < 8; m++) {
                            int x = (m + i * 8);
                            int y = k * 4 + l;
                            int z = (j * 8);

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

    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius,
            boolean skipExistingChunks) {
        if (feature.equals(StructureFeature.OCEAN_RUIN) || 
            feature.equals(StructureFeature.SHIPWRECK) || 
            feature.equals(StructureFeature.BURIED_TREASURE) ||
            feature.equals(BetaStructure.OCEAN_SHRINE_STRUCTURE)) {
            return null;
        }

        return super.locateStructure(world, feature, center, radius, skipExistingChunks);
    }

    @Override
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new SkylandsChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
    
    public boolean isSkyDim() {
        return this.generateSkyDim;
    }
    

}
