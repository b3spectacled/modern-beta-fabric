package com.bespectacled.modernbeta.gen;

import java.util.HashMap;
import java.util.List;
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
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

//private final BetaGeneratorSettings settings;

public class BetaChunkGenerator extends NoiseChunkGenerator implements IOldChunkGenerator {

    public static final Codec<BetaChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(BetaChunkGenerator::new)));

    private final OldGeneratorSettings settings;
    private final OldBiomeSource biomeSource;
    private final long seed;

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

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[] = new double[425];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];
    
    private static final Identifier[] BIOMES = new Identifier[256];

    public BetaChunkGenerator(BiomeSource biomes, long seed, OldGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (OldBiomeSource) biomes;
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

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "beta"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Beta chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return BetaChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        BiomeUtil.fetchTempHumid(chunk.getPos().x << 4, chunk.getPos().z << 4, TEMPS, HUMIDS);
        generateTerrain(chunk, TEMPS, structureAccessor);
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
    }

    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        boolean genOceans = this.biomeSource.isVanilla() || this.biomeSource.generateOceans();
        
        genOceans = this.biomeSource.isVanilla();
        
        GenUtil.generateFeaturesWithOcean(chunkRegion, structureAccessor, this, FEATURE_RAND, genOceans);
    }
    
    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        boolean genOceans = this.biomeSource.isVanilla() || this.biomeSource.generateOceans();
        
        genOceans = this.biomeSource.isVanilla();
        
        GenUtil.carveWithOcean(this.seed, biomeAccess, chunk, carver, this, this.biomeSource, FEATURE_RAND, this.getSeaLevel(), genOceans);
    }

    // Modified to accommodate additional ocean biome replacements
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        boolean genOceans = this.biomeSource.isVanilla() || this.biomeSource.generateOceans();
        
        genOceans = this.biomeSource.isVanilla();
        
        GenUtil.setStructureStartsWithOcean(dynamicRegistryManager, structureAccessor, chunk, structureManager, seed, this, this.biomeSource, genOceans);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        buildBetaSurface(region, chunk);
        
        boolean genOceans = this.biomeSource.isVanilla() || this.biomeSource.generateOceans();
        
        genOceans = this.biomeSource.isVanilla();
        
        if (genOceans) {
            GenUtil.injectOceanBiomes(chunk, biomeSource);
        }
    }


    public void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        //int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * byte4, 0, chunk.getPos().z * byte4);
        
        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth; 
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double quarter = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * quarter; // Lerp
                        double var13 = (var4 - var2) * quarter;
                        
                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; // More lerp

                            int absX = (chunk.getPos().x << 4) + i * 4 + m;

                            for (int n = 0; n < 4; n++) {
                                int absZ = (chunk.getPos().z << 4) + j * 4 + n;
                                double temp = temps[(i * 4 + m) * 16 + (j * 4 + n)];
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);

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
        byte byte4 = 4;
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

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

        scaleNoise = scaleNoiseOctaves.sampleBetaOctaves(scaleNoise, x, z, int5_0, int5_1, 1.121D, 1.121D, 0.5D);
        depthNoise = depthNoiseOctaves.sampleBetaOctaves(depthNoise, x, z, int5_0, int5_1, depthNoiseScaleX, depthNoiseScaleZ, depthNoiseScaleExponent);

        mainNoise = mainNoiseOctaves.sampleBetaOctaves(
            mainNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoise = minLimitNoiseOctaves.sampleBetaOctaves(
            minLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoise = maxLimitNoiseOctaves.sampleBetaOctaves(
            maxLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int i = 0;
        int j = 0;
        int k = 16 / int5_0;

        for (int l = 0; l < int5_0; l++) {
            int idx0 = l * k + k / 2;

            for (int m = 0; m < int5_1; m++) {
                int idx1 = m * k + k / 2;

                double curTemp = TEMPS[idx0 * 16 + idx1];
                double curHumid = HUMIDS[idx0 * 16 + idx1] * curTemp;

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

                if (depthMod < 0.0D) {
                    depthMod /= 2D;

                    if (depthMod < -1D) {
                        depthMod = -1D;
                    }

                    depthMod /= 1.3999999999999999D;
                    depthMod /= 2D;

                    scaleMod = 0.0D;

                } else {
                    if (depthMod > 1.0D) {
                        depthMod = 1.0D;
                    }
                    depthMod /= 8D;
                }

                if (scaleMod < 0.0D) {
                    scaleMod = 0.0D;
                }

                scaleMod += 0.5D;
                depthMod = (depthMod * (double) byte17) / 16D;

                double depthMod2 = (double) byte17 / 2D + depthMod * 4D;

                j++;

                for (int n = 0; n < byte17; n++) {
                    double heightVal = 0.0D;
                    double scaleMod2 = (((double) n - depthMod2) * 12D) / scaleMod;

                    if (scaleMod2 < 0.0D) {
                        scaleMod2 *= 4D;
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
                    heightVal -= scaleMod2;

                    if (n > byte17 - 4) {
                        double d13 = (float) (n - (byte17 - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d13) + -10D * d13;
                    }
                    
                    HEIGHTMAP[i] = heightVal;
                    i++;
                }
            }
        }
    }

    private void buildBetaSurface(ChunkRegion region, Chunk chunk) {
        byte seaLevel = (byte) this.getSeaLevel();
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        BiomeUtil.fetchTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        BetaBiomes.getBiomesFromLookup(TEMPS, HUMIDS, BIOMES, null);
        
        Biome curBiome;

        sandNoise = beachNoiseOctaves.sampleBetaOctaves(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond, thirtysecond, 1.0D);
        gravelNoise = beachNoiseOctaves.sampleBetaOctaves(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1,
                16, thirtysecond, 1.0D, thirtysecond);
        stoneNoise = stoneNoiseOctaves.sampleBetaOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                boolean genSandBeach = sandNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                    
                curBiome = this.biomeSource.isBeta() ? 
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

    protected BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        } else if (y < this.getSeaLevel()) {
            if (temp < 0.5D && y >= this.getSeaLevel() - 1) {
                // Get chunk errors so disabled for now.
                //blockStateToSet = Blocks.ICE.getDefaultState(); 
                blockStateToSet = this.settings.wrapped.getDefaultFluid();
            } else {
                blockStateToSet = this.settings.wrapped.getDefaultFluid();
            }

        }
        return blockStateToSet;
    }

    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            BiomeUtil.fetchTempHumid((x >> 4) << 4, (z >> 4) << 4, TEMPS, HUMIDS);
            
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);
        
        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private void sampleHeightmap(int absX, int absZ) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        //int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;

        generateHeightmap(chunkX * byte4, 0, chunkZ * byte4);

        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth;
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double var9 = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * var9;
                        double var13 = (var4 - var2) * var9;

                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

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
    
    
    @Override
    public List<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor structureAccessor, SpawnGroup spawnGroup, BlockPos blockPos) {
        if (spawnGroup == SpawnGroup.MONSTER) {
            if (structureAccessor.getStructureAt(blockPos, false, BetaStructure.OCEAN_SHRINE_STRUCTURE).hasChildren()) {
                return BetaStructure.OCEAN_SHRINE_STRUCTURE.getMonsterSpawns();
            }
        }

        return super.getEntitySpawnList(biome, structureAccessor, spawnGroup, blockPos);
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
        return new BetaChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
    

}