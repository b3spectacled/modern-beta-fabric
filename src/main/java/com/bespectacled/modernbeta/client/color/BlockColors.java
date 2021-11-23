package com.bespectacled.modernbeta.client.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;

public final class BlockColors {
    public static void register() {
        // Grass blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BetaBlockColors.INSTANCE.getGrassColor(state, view, pos, tintNdx), 
            Blocks.GRASS_BLOCK
        );
        
        // Tall grass blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BetaBlockColors.INSTANCE.getTallGrassColor(state, view, pos, tintNdx), 
            Blocks.FERN,
            Blocks.GRASS,
            Blocks.POTTED_FERN,
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
        );
        
        // Foliage blocks
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BetaBlockColors.INSTANCE.getFoliageColor(state, view, pos, tintNdx),
            Blocks.OAK_LEAVES, 
            Blocks.JUNGLE_LEAVES, 
            Blocks.ACACIA_LEAVES, 
            Blocks.DARK_OAK_LEAVES, 
            Blocks.VINE
        );
        
        // Reeds
        ColorProviderRegistry.BLOCK.register(
            (state, view, pos, tintNdx) -> BetaBlockColors.INSTANCE.getReedColor(state, view, pos, tintNdx), 
            Blocks.SUGAR_CANE
        );
    }
}
