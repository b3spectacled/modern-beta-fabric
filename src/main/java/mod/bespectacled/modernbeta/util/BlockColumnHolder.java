package mod.bespectacled.modernbeta.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.BlockColumn;

public class BlockColumnHolder {
    private final BlockPos.Mutable pos;
    private final BlockColumn blockColumn;
    
    public BlockColumnHolder(Chunk chunk) {
        this.pos = new BlockPos.Mutable();
        this.blockColumn = new BlockColumn(){
            @Override
            public BlockState getState(int y) {
                return chunk.getBlockState(pos.setY(y));
            }

            @Override
            public void setState(int y, BlockState state) {
                HeightLimitView view = chunk.getHeightLimitView();
                
                if (y >= view.getBottomY() && y < view.getTopY()) {
                    chunk.setBlockState(pos.setY(y), state, false);
                    
                    if (!state.getFluidState().isEmpty()) {
                        chunk.markBlockForPostProcessing(pos);
                    }
                }
            }

            public String toString() {
                return "ChunkBlockColumn " + chunk.getPos();
            }
        };
    }
    
    public void setPos(int x, int z) {
        this.pos.setX(x).setZ(z);
    }
    
    public BlockState getState(int y) {
        return this.blockColumn.getState(y);
    }
    
    public void setState(int y, BlockState state) {
        this.blockColumn.setState(y, state);
    }
    
    public BlockColumn getBlockColumn() {
        return this.blockColumn;
    }
}
