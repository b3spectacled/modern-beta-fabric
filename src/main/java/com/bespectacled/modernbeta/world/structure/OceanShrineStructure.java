package com.bespectacled.modernbeta.world.structure;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class OceanShrineStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final Pool<SpawnSettings.SpawnEntry> MONSTER_SPAWNS;
    
    public OceanShrineStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }
    
    @Override
    public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }
    
    @Override
    public Pool<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return OceanShrineStructure.MONSTER_SPAWNS;
    }
    
    @Override
    protected boolean shouldStartAt(
        ChunkGenerator chunkGenerator, 
        BiomeSource biomeSource, 
        long worldSeed, 
        ChunkRandom random, 
        ChunkPos pos, 
        Biome biome, 
        ChunkPos chunkPos, 
        DefaultFeatureConfig config, 
        HeightLimitView world
    ) {
        if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator && oldChunkGenerator.generatesOceanShrines()) {
            return true;
        }
        
        return false;
    }
    
    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int references, long seed) {
            super(structureFeature, chunkPos, references, seed);
        }
        
        @Override
        public void init(
            DynamicRegistryManager registryManager, 
            ChunkGenerator chunkGenerator, 
            StructureManager manager, 
            ChunkPos chunkPos,
            Biome biome, 
            DefaultFeatureConfig config, 
            HeightLimitView heightLimitView
        ) { 
            int x = chunkPos.getStartX();
            int z = chunkPos.getStartZ();
            int y = chunkGenerator.getHeight(x, z, Type.OCEAN_FLOOR_WG, heightLimitView);
            
            BlockPos pos = new BlockPos(x, y, z);
            BlockRotation rot = BlockRotation.random(this.random);
            
            OceanShrineGenerator.addPieces(manager, pos, rot, this, this.children);
        }
    }
    
    static {
        MONSTER_SPAWNS = Pool.<SpawnSettings.SpawnEntry>of(new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 1, 0, 1));
    }
}
