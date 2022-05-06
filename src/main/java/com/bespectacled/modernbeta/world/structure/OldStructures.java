package com.bespectacled.modernbeta.world.structure;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.mixin.MixinStructureFeatureAccessor;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class OldStructures {
    private static final Identifier OCEAN_SHRINE_ID = ModernBeta.createId("ocean_shrine");
    public static final StructurePieceType.ManagerAware OCEAN_SHRINE_PIECE = OceanShrineGenerator.Piece::new;

    public static void register() {
        MixinStructureFeatureAccessor.invokeRegister(
            OCEAN_SHRINE_ID.toString(),
            new OceanShrineStructure(DefaultFeatureConfig.CODEC),
            GenerationStep.Feature.SURFACE_STRUCTURES
        );
    }
}
