package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.gen.OldGeneratorSettings;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.block.BlockState;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public class FlatChunkProvider extends AbstractChunkProvider {
    
    public FlatChunkProvider(long seed, OldGeneratorSettings settings) {
        //super(seed, settings);
        super(seed, 0, 128, 64, 0, -10, 2, 1, 1.0, 1.0, 80, 160, BlockStates.STONE, BlockStates.WATER, settings.providerSettings);
        
        // Noise Generators
        new PerlinOctaveNoise(RAND, 16, true);
        new PerlinOctaveNoise(RAND, 16, true);
        new PerlinOctaveNoise(RAND, 8, true);
        new PerlinOctaveNoise(RAND, 4, true);
        new PerlinOctaveNoise(RAND, 4, true);
        new PerlinOctaveNoise(RAND, 10, true);
        new PerlinOctaveNoise(RAND, 16, true);
        setForestOctaves(new PerlinOctaveNoise(RAND, 8, true));
    }

    @Override
    public void provideChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        BlockState blockToSet;
        
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                
                Biome curBiome = getBiomeForSurfaceGen(POS.set(absX, 0, absZ), region, biomeSource);
                
                BlockState topBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState fillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                for (int y = 0; y <= this.seaLevel; ++y) {
                    if (y == this.seaLevel) 
                        blockToSet = topBlock;
                    else if (y >= this.seaLevel - 2) 
                        blockToSet = fillerBlock;
                    else if (y >= 1) 
                        blockToSet = BlockStates.STONE;
                    else 
                        blockToSet = BlockStates.BEDROCK;
                    
                    chunk.setBlockState(POS.set(x, y, z), blockToSet, false);
                }
            }
        }
        
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        return this.seaLevel + 1;
    }
    
    @Override
    public PerlinOctaveNoise getBeachNoiseOctaves() {
        return null;
    }
}
