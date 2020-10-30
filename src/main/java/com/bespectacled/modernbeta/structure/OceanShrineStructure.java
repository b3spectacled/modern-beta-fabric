package com.bespectacled.modernbeta.structure;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class OceanShrineStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final List<SpawnSettings.SpawnEntry> MONSTER_SPAWNS = ImmutableList.<SpawnSettings.SpawnEntry>of(new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 100, 2, 4));
    
    public OceanShrineStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }
    
    @Override
    public List<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return OceanShrineStructure.MONSTER_SPAWNS;
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
            int x = chunkX * 16;
            int z = chunkZ * 16;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
            
            BlockPos pos = new BlockPos(x, y, z);
            BlockRotation rot = BlockRotation.random(this.random);
            
            OceanShrineGenerator.addPieces(structureManager, pos, rot, this.children);
            this.setBoundingBoxFromChildren();
        }
    }
}
