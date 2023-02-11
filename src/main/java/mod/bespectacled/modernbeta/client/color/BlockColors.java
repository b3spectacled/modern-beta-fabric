package mod.bespectacled.modernbeta.client.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;

public final class BlockColors {
    public static void register() {
        // Grass blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BlockColorsBeta.INSTANCE.getGrassColor(state, view, pos, tintNdx), 
            Blocks.GRASS_BLOCK
        );
        
        // Tall grass blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BlockColorsBeta.INSTANCE.getTallGrassColor(state, view, pos, tintNdx), 
            Blocks.FERN,
            Blocks.GRASS,
            Blocks.POTTED_FERN,
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
        );
        
        // Foliage blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BlockColorsBeta.INSTANCE.getFoliageColor(state, view, pos, tintNdx),
            Blocks.OAK_LEAVES, 
            Blocks.JUNGLE_LEAVES, 
            Blocks.ACACIA_LEAVES, 
            Blocks.DARK_OAK_LEAVES, 
            Blocks.VINE
        );
        
        // Sugar cane
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BlockColorsBeta.INSTANCE.getSugarCaneColor(state, view, pos, tintNdx), 
            Blocks.SUGAR_CANE
        );
        
        // Water blocks
        /*
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BlockColorsBeta.INSTANCE.getWaterColor(state, view, pos, tintNdx), 
            Blocks.WATER,
            Blocks.BUBBLE_COLUMN,
            Blocks.WATER_CAULDRON
        );
        */
    }   
}
