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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
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
import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.biome.PreBetaBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.structure.BetaStructure;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

//private final BetaGeneratorSettings settings;

public class InfdevChunkGenerator extends NoiseChunkGenerator implements IOldChunkGenerator {

    public static final Codec<InfdevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    OldGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(InfdevChunkGenerator::new)));

    private final OldGeneratorSettings settings;
    private final OldBiomeSource biomeSource;
    private final long seed;

    private final PerlinOctaveNoise noiseOctavesA;
    private final PerlinOctaveNoise noiseOctavesB;
    private final PerlinOctaveNoise noiseOctavesC;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[][] = new double[33][4];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);

    public InfdevChunkGenerator(BiomeSource biomes, long seed, OldGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (OldBiomeSource) biomes;
        this.seed = seed;
        
        RAND.setSeed(seed);
        
        // Noise Generators
        noiseOctavesA = new PerlinOctaveNoise(RAND, 16, false);
        noiseOctavesB = new PerlinOctaveNoise(RAND, 16, false);
        noiseOctavesC = new PerlinOctaveNoise(RAND, 8, false);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);

        new PerlinOctaveNoise(RAND, 5, false); // Unused in original source
        
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 5, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "infdev"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return InfdevChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, structureAccessor);  
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
    }
    
    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        GenUtil.generateFeaturesWithOcean(chunkRegion, structureAccessor, this, FEATURE_RAND, this.biomeSource.isVanilla());
    }
    
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        GenUtil.setStructureStartsWithOcean(dynamicRegistryManager, structureAccessor, chunk, structureManager, seed, this, this.biomeSource, this.biomeSource.isVanilla());
    }


    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        GenUtil.carveWithOcean(this.seed, biomeAccess, chunk, carver, this, biomeSource, FEATURE_RAND, this.getSeaLevel(), this.biomeSource.isVanilla());
    }

    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
        buildInfdevSurface(chunkRegion, chunk);

        if (this.biomeSource.isVanilla()) {
            GenUtil.injectOceanBiomes(chunk, biomeSource);
        }
    }
     

    public void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    HEIGHTMAP[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    HEIGHTMAP[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    HEIGHTMAP[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    HEIGHTMAP[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = HEIGHTMAP[bY][0];
                    double n2 = HEIGHTMAP[bY][1];
                    double n3 = HEIGHTMAP[bY][2];
                    double n4 = HEIGHTMAP[bY][3];
                    double n5 = HEIGHTMAP[bY + 1][0];
                    double n7 = HEIGHTMAP[bY + 1][1];
                    double n8 = HEIGHTMAP[bY + 1][2];
                    double n9 = HEIGHTMAP[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        int y = (bY << 2) + pY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double density = nz1 + (nz2 - nz1) * mixZ;
                                
                                int absX = (chunk.getPos().x << 4) + x;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);
                                
                                BlockState blockToSet = this.getBlockState(clampedDensity, y);
                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                            }
                        }
                    }
                }
            }
        }
    }

    private double generateHeightmap(double x, double y, double z) {
        double elevGrad;
        if ((elevGrad = y * 4.0 - 64.0) < 0.0) {
            elevGrad *= 3.0;
        }
        
        double noise;
        double res;
        
        if ((noise = this.noiseOctavesC.sampleInfdevOctaves(x * 8.55515, y * 1.71103, z * 8.55515) / 2.0) < -1) {
            res = MathHelper.clamp(
                this.noiseOctavesA.sampleInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else if (noise > 1.0) {
            res = MathHelper.clamp(
                this.noiseOctavesB.sampleInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else {
            double noise2 = MathHelper.clamp(
                this.noiseOctavesA.sampleInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double noise3 = MathHelper.clamp(
                this.noiseOctavesB.sampleInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double mix = (noise + 1.0) / 2.0;
            
            res = noise2 + (noise3 - noise2) * mix;
        }
        
        return res;
    }

    private void buildInfdevSurface(ChunkRegion region, Chunk chunk) {
        double thirtysecond = 0.03125D; // eighth
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                
                boolean genSandBeach = this.beachNoiseOctaves.sampleInfdevOctaves(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + RAND.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sampleInfdevOctaves(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + RAND.nextDouble() * 0.2 > 3.0;
                
                int genStone = (int)(this.stoneNoiseOctaves.sampleInfdevOctaves(
                    absX * thirtysecond * 2.0, 
                    absZ * thirtysecond * 2.0) / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome curBiome = region.getBiome(POS.set(absX, 0, absZ));
                
                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                for (int y = 127; y >= 0; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + RAND.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    POS.set(x, y, z);
                    Block someBlock = chunk.getBlockState(POS).getBlock();
                    
                    if (someBlock.equals(Blocks.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(Blocks.STONE)) {
                        if (flag == -1) {
                            if (genStone <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = BlockStates.STONE;
                            } else if (y >= 60 && y <= 65) {
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
                            
                            if (y < this.getSeaLevel() && topBlock.equals(BlockStates.AIR)) {
                                topBlock = BlockStates.WATER;
                            }
                            
                            flag = genStone;
                            
                            if (y >= this.getSeaLevel() - 1) {
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
                                flag = RAND.nextInt(4);
                                fillerBlock = BlockStates.SANDSTONE;
                            }
                        }
                    }
                }
            }
        }
    }

    
    protected BlockState getBlockState(double density, int y) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        } else if (y < this.getSeaLevel()) {
            blockStateToSet = this.settings.wrapped.getDefaultFluid();

        }
        return blockStateToSet;
    }

    
    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private void sampleHeightmap(int absX, int absZ) {
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    HEIGHTMAP[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    HEIGHTMAP[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    HEIGHTMAP[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    HEIGHTMAP[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = HEIGHTMAP[bY][0];
                    double n2 = HEIGHTMAP[bY][1];
                    double n3 = HEIGHTMAP[bY][2];
                    double n4 = HEIGHTMAP[bY][3];
                    double n5 = HEIGHTMAP[bY + 1][0];
                    double n7 = HEIGHTMAP[bY + 1][1];
                    double n8 = HEIGHTMAP[bY + 1][2];
                    double n9 = HEIGHTMAP[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            int y = (bY << 2) + pY;
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double noiseValue = nz1 + (nz2 - nz1) * mixZ;
                                if (noiseValue > 0.0D) {
                                    CHUNK_Y[x][z] = y;
                                }
                                
                                z++;
                            }
                        }
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
        return this.settings.wrapped.getSeaLevel();
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new InfdevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
    
}
