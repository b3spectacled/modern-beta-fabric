package com.bespectacled.modernbeta.world.structure;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class OldStructures {
    private static final Identifier OCEAN_SHRINE_ID = ModernBeta.createId("ocean_shrine");
    private static final Identifier OCEAN_SHRINE_BASE_ID = ModernBeta.createId("ocean_shrine/base");
    
    public static final StructurePieceType OCEAN_SHRINE_PIECE = OceanShrineGenerator.Piece::new;
    public static final StructureFeature<DefaultFeatureConfig> OCEAN_SHRINE_STRUCTURE = new OceanShrineStructure(DefaultFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> CONF_OCEAN_SHRINE_STRUCTURE = OCEAN_SHRINE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);
    public static final RegistryKey<ConfiguredStructureFeature<?, ?>> OCEAN_SHRINE_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, OCEAN_SHRINE_ID);
    
    public static void register() {
        Registry.register(
            Registry.STRUCTURE_PIECE, 
            OCEAN_SHRINE_BASE_ID,
            OCEAN_SHRINE_PIECE
        );
            
        FabricStructureBuilder.create(
            OCEAN_SHRINE_ID, 
            OCEAN_SHRINE_STRUCTURE)
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(64, 16, 357)
            //.defaultConfig(4, 2, 357) // Debug spacing
            .superflatFeature(CONF_OCEAN_SHRINE_STRUCTURE)
            .adjustsSurface()
            .register();
        
        BuiltinRegistries.add(
            BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, 
            OCEAN_SHRINE_KEY.getValue(), 
            CONF_OCEAN_SHRINE_STRUCTURE
        );
    }
}
