package com.bespectacled.modernbeta.world.structure;

import java.util.function.Predicate;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class OceanShrineStructure extends StructureFeature<DefaultFeatureConfig> {
    public static final Pool<SpawnSettings.SpawnEntry> MONSTER_SPAWNS;
    
    public OceanShrineStructure(Codec<DefaultFeatureConfig> codec) {
        super(codec, StructureGeneratorFactory.simple(checkIfOceanShrinesValid(), OceanShrineStructure::init));
    }
    
    private static void init(StructurePiecesCollector structureHolder, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        int x = context.chunkPos().getStartX();
        int z = context.chunkPos().getStartZ();
        int y = context.chunkGenerator().getHeight(x, z, Type.OCEAN_FLOOR_WG, context.world());
        
        BlockPos pos = new BlockPos(x, y, z);
        BlockRotation rot = BlockRotation.random(context.random());
        
        OceanShrineGenerator.addPieces(context.structureManager(), pos, rot, structureHolder, context.random(), context.config());
    }
    
    public static Pool<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return OceanShrineStructure.MONSTER_SPAWNS;
    }
    
    public static <C extends FeatureConfig> Predicate<StructureGeneratorFactory.Context<C>> checkIfOceanShrinesValid() {
        return structureInfo -> {
            ChunkGenerator chunkGenerator = structureInfo.chunkGenerator();
            
            if (chunkGenerator instanceof OldChunkGenerator oldChunkGenerator && oldChunkGenerator.generatesOceanShrines()) {
                return structureInfo.isBiomeValid(Heightmap.Type.OCEAN_FLOOR_WG);
            }
            
            return false;
        };
    }
    
    static {
        MONSTER_SPAWNS = Pool.<SpawnSettings.SpawnEntry>of(new SpawnSettings.SpawnEntry(EntityType.GUARDIAN, 1, 0, 1));
    }
}
