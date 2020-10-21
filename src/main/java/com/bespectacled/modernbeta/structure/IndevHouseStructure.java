package com.bespectacled.modernbeta.structure;

import com.mojang.serialization.Codec;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.Heightmap;

public class IndevHouseStructure extends StructureFeature<DefaultFeatureConfig> {
    public IndevHouseStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }
    
    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, int chunkX, int chunkZ, BlockBox blockBox, int references, long seed) {
            super(structureFeature, chunkX, chunkZ, blockBox, references, seed);
        }
        
        @Override
        public void init(
            DynamicRegistryManager dynamicRegistryManager, 
            ChunkGenerator chunkGenerator, 
            StructureManager structureManager, 
            int chunkX, int chunkZ, 
            Biome biome, 
            DefaultFeatureConfig defaultFeatureConfig
        ) {
            int x = chunkX * 16 - 3;  // -  3 to center house
            int z = chunkZ * 16 - 3;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG) - 1; // So stone floor touches ground

            BlockPos pos = new BlockPos(x, y, z);
            BlockRotation rot = BlockRotation.NONE; // Indev House structure already positioned northward
            
            IndevHouseGenerator.addPieces(structureManager, pos, rot, this.children);
            this.setBoundingBoxFromChildren();
        }
    }
}
