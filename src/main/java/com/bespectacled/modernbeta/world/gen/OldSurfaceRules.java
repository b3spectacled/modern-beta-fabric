package com.bespectacled.modernbeta.world.gen;

import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

public class OldSurfaceRules {
    private final MaterialRules.MaterialRuleContext ruleContext;
    private final MaterialRules.BlockStateRule blockStateRule;
    
    public OldSurfaceRules(
        ChunkRegion region,
        Chunk chunk,
        OldChunkGenerator chunkGenerator
    ) {
        ChunkProvider chunkProvider = chunkGenerator.getChunkProvider();
        HeightContext context = new HeightContext(chunkGenerator, region);
        
        BiomeAccess biomeAccess = region.getBiomeAccess();
        Registry<Biome> biomeRegistry = region.getRegistryManager().get(Registry.BIOME_KEY);
        
        this.ruleContext = new MaterialRules.MaterialRuleContext(
            chunkProvider.getSurfaceBuilder(),
            chunk,
            chunkProvider.getChunkNoiseSampler(),
            biomeAccess::getBiome,
            biomeRegistry,
            context
        );
        
        this.blockStateRule = chunkProvider.getSurfaceRule().apply(ruleContext);
    }
    
    public MaterialRules.MaterialRuleContext getRuleContext() {
        return this.ruleContext;
    }
    
    public MaterialRules.BlockStateRule getBlockStateRule() {
        return this.blockStateRule;
    }
    
    public static MaterialRules.MaterialRule createVanilla(boolean hasSurface) {
        return VanillaSurfaceRules.createDefaultRule(hasSurface, false, false);
    }
}
