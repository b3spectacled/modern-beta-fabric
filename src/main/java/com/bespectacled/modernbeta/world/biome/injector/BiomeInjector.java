package com.bespectacled.modernbeta.world.biome.injector;

import java.util.function.Predicate;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.compat.Compat;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.util.mutable.MutableBiomeArray;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class BiomeInjector {
    public static final int OCEAN_MIN_DEPTH = 4;
    public static final int DEEP_OCEAN_MIN_DEPTH = 16;
    
    private final OldChunkGenerator oldChunkGenerator;
    private final OldBiomeSource oldBiomeSource;
    private final ChunkProvider chunkProvider;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(OldChunkGenerator oldChunkGenerator, OldBiomeSource oldBiomeSource) {
        this.oldChunkGenerator = oldChunkGenerator;
        this.oldBiomeSource = oldBiomeSource;
        this.chunkProvider = oldChunkGenerator.getChunkProvider();
        
        NbtCompound chunkSettings = this.oldChunkGenerator.getChunkSettings();
        NbtCompound biomeSettings = this.oldBiomeSource.getBiomeSettings();
        
        boolean legacyGeneratesOceans = NbtUtil.readBoolean(NbtTags.GEN_OCEANS, chunkSettings, false);
        boolean generatesOceans = NbtUtil.readBoolean(NbtTags.GEN_OCEANS, biomeSettings, false);
        
        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
        
        Predicate<BiomeInjectionContext> deepOceanPredicate = context ->
            this.atOceanDepth(context.topHeight, DEEP_OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
        
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder();
        
        if (!Compat.isLoaded("hydrogen") && (legacyGeneratesOceans || generatesOceans)) {
            builder.add(deepOceanPredicate, this.oldBiomeSource::getDeepOceanBiomeForNoiseGen);
            builder.add(oceanPredicate, this.oldBiomeSource::getOceanBiomeForNoiseGen);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk) {
        MutableBiomeArray biomeArray = MutableBiomeArray.inject(chunk.getBiomeArray());
        
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startBiomeX = chunkPos.getStartX() >> 2;
        int startBiomeZ = chunkPos.getStartZ() >> 2;
        
        int worldHeight = this.oldChunkGenerator.getWorldHeight();
        int worldMinY = this.oldChunkGenerator.getMinimumY();
        
        int biomeHeight = worldHeight >> 2;
        
        for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
            for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                int biomeX = localBiomeX + startBiomeX;
                int biomeZ = localBiomeZ + startBiomeZ;
                
                int x = (biomeX << 2) + 2;
                int z = (biomeZ << 2) + 2;
                    
                int topHeight = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                BlockState topState = chunk.getBlockState(pos.set(x, topHeight, z));
                
                BiomeInjectionContext context = new BiomeInjectionContext(topHeight, topState);
                Biome biome = this.sample(context, biomeX, 0, biomeZ);
                
                if (biome != null) {
                    // Fill biome column
                    for (int biomeY = 0; biomeY < biomeHeight; ++biomeY) {
                        int y = biomeY << 2;
                        
                        biomeArray.setBiome(x, y, z, biome, worldMinY, worldHeight);
                    }
                }
            }
        }
    }
    
    public Biome sample(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return this.rules.test(context, biomeX, biomeY, biomeZ);
    }
    
    public int getCenteredHeight(int biomeX, int biomeZ) {
        // Offset by 2 to get center of biome coordinate section in block coordinates
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.chunkProvider instanceof NoiseChunkProvider noiseChunkProvider ?
            noiseChunkProvider.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }

    private boolean atOceanDepth(int topHeight, int oceanDepth) {
        return topHeight < this.oldChunkGenerator.getSeaLevel() - oceanDepth;
    }
}
