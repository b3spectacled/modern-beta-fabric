package com.bespectacled.modernbeta.world.structure;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.mojang.serialization.Codec;

import net.minecraft.class_6622;
import net.minecraft.class_6626;
import net.minecraft.entity.EntityType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.Heightmap;

public class OceanShrineStructure extends StructureFeature<DefaultFeatureConfig> {
    public static final Pool<SpawnSettings.SpawnEntry> MONSTER_SPAWNS;
    
    public OceanShrineStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec, OceanShrineStructure::init);
    }
    
    private static void init(class_6626 structureHolder, FeatureConfig config, class_6622.class_6623 arg3) {
        if (!arg3.method_38707(Heightmap.Type.OCEAN_FLOOR_WG)) {
            return;
        }
        
        int x = arg3.chunkPos().getStartX();
        int z = arg3.chunkPos().getStartZ();
        int y = arg3.chunkGenerator().getHeight(x, z, Type.OCEAN_FLOOR_WG, arg3.heightAccessor());
        
        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation rot = BlockRotation.random(arg3.random());
        
        OceanShrineGenerator.addPieces(null, pos, rot, structureHolder, arg3.random(), config);
    }
    
    public static Pool<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return OceanShrineStructure.MONSTER_SPAWNS;
    }
    
    @Override
    protected boolean shouldStartAt(
        ChunkGenerator chunkGenerator, 
        BiomeSource biomeSource, 
        long worldSeed, 
        ChunkRandom random, 
        ChunkPos pos,
        ChunkPos chunkPos, 
        DefaultFeatureConfig config, 
        HeightLimitView world
    ) {
        if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator && oldChunkGenerator.generatesOceanShrines()) {
            return true;
        }
        
        return false;
    }
    
    /*
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
    */
    
    static {
        MONSTER_SPAWNS = Pool.<SpawnSettings.SpawnEntry>of(new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 1, 0, 1));
    }
}
