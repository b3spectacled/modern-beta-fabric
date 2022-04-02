package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.world.biome.beta.BetaBiomes;
import com.bespectacled.modernbeta.world.biome.pe.PEBiomes;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

public class OldSurfaceRules {
    private static final MaterialRules.MaterialRule SAND = block(Blocks.SAND);
    private static final MaterialRules.MaterialRule SANDSTONE = block(Blocks.SANDSTONE);
    
    private static MaterialRules.MaterialRule block(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
    
    public static MaterialRules.MaterialRule create() {
        MaterialRules.MaterialRule materialRules = VanillaSurfaceRules.createOverworldSurfaceRule();
        
        MaterialRules.MaterialRule sandRule = MaterialRules.sequence(MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, SANDSTONE), SAND);
        MaterialRules.MaterialCondition desertCondition = MaterialRules.biome(
            BetaBiomes.DESERT_KEY,
            BetaBiomes.ICE_DESERT_KEY,
            PEBiomes.PE_DESERT_KEY,
            PEBiomes.PE_ICE_DESERT_KEY
        );
        MaterialRules.MaterialRule desertRule = MaterialRules.condition(desertCondition, sandRule);
        
        MaterialRules.MaterialRule desertWithDepthCondition = MaterialRules.condition(MaterialRules.waterWithStoneDepth(-6, -1), desertRule);
        MaterialRules.MaterialRule runDepthCondition = MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH, desertWithDepthCondition);
        
        return MaterialRules.sequence(runDepthCondition, materialRules);
    }
    
    public static MaterialRules.MaterialRule createVanilla(boolean hasSurface) {
        return VanillaSurfaceRules.createDefaultRule(hasSurface, false, false);
    }
}
