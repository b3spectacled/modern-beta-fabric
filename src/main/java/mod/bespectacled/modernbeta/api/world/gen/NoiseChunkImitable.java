package mod.bespectacled.modernbeta.api.world.gen;

import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.world.gen.blocksource.SimpleBlockSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.StructureWeightSampler;

public interface NoiseChunkImitable {
    /**
     * Gets blockstate at block coordinates given block and default fluid block.
     * Simulates a noise density for the purpose of masking terrain around structures.
     * Used for 2D noise terrain generators (i.e. Infdev 20100227 and Indev terrain generators).
     * 
     * @param x x-coordinate in absolute block coordinates.
     * @param y y-coordinate in absolute block coordinates.
     * @param z z-coordinate in absolute block coordinates.
     * @param block Block to get blockstate for.
     * @param defaultBlock Default base block.
     * @oaran defaultFluid Default fluid block.
     * 
     * @return A blockstate.
     */
    default BlockState getBlockState(
        StructureWeightSampler weightSampler, 
        SimpleBlockSource blockSource, 
        int x, int y, int z, 
        Block block, 
        Block defaultBlock,
        Block defaultFluid
    ) {
        boolean isFluid = block == Blocks.AIR || block == defaultFluid;
        double simDensity = isFluid ? -25D : 25D;
        
        double clampedDensity = MathHelper.clamp(simDensity / 200.0, -1.0, 1.0);
        clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
        clampedDensity += weightSampler.sample(new SimpleNoisePos(x, y, z));
        
        BlockState blockState = block.getDefaultState();
        
        if (clampedDensity > 0.0) { 
            blockState = blockSource.apply(x, y, z);
            
            if (blockState == null)
                blockState = block.getDefaultState();
            
            // Handle structures generating over water/air
            if (isFluid)
                blockState = defaultBlock.getDefaultState();
            
        } else if (clampedDensity <= 0.0) {
            // Handle structures generating inside pre-existing terrain
            if (!isFluid) 
                blockState = BlockStates.AIR;
        }
        
        return blockState;
    }
    
}
