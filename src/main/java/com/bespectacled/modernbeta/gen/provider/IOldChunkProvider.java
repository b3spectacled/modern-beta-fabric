package com.bespectacled.modernbeta.gen.provider;

import com.bespectacled.modernbeta.biome.OldBiomeSource;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public interface IOldChunkProvider {
    public void makeChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, OldBiomeSource biomeSource);
    public void makeSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource);
    
    public int getHeight(int x, int z, Heightmap.Type type);
    
    public static IOldChunkProvider getChunkProvider(long seed, WorldType worldType, CompoundTag settings) {
        IOldChunkProvider provider;
        
        switch(worldType) {
            case BETA:
                provider = new BetaChunkProvider(seed);
                break;
            case SKYLANDS:
                provider = new SkylandsChunkProvider(seed);
                break;
            case ALPHA:
                provider = new AlphaChunkProvider(seed);
                break;
            case INFDEV:
                provider = new InfdevChunkProvider(seed);
                break;
            case INFDEV_OLD:
                provider = new InfdevOldChunkProvider(seed, settings);
                break;
            case INDEV:
                provider = new IndevChunkProvider(seed, settings);
                break;
            default:
                provider = new BetaChunkProvider(seed);
        }
        
        return provider;
    }
    
    public static BlockState getBlockState(double density, int y, double temp) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = BlockStates.STONE;
        } else if (y < 64) {
            if (temp < 0.5D && y >= 64 - 1) {
                // Get chunk errors so disabled for now.
                //blockStateToSet = Blocks.ICE.getDefaultState(); 
                blockStateToSet = BlockStates.WATER;
            } else {
                blockStateToSet = BlockStates.WATER;
            }

        }
        return blockStateToSet;
    }
}
