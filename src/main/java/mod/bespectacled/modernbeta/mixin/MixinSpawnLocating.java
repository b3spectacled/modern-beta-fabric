package mod.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(SpawnLocating.class)
public abstract class MixinSpawnLocating {
    /*
     * Override vanilla behavior of moving player to highest solid block, 
     * even after finding initial spawn point.
     */
    @Inject(method = "findOverworldSpawn", at = @At("HEAD"), cancellable = true)
    private static void injectFindOverworldSpawnHeight(ServerWorld world, int x, int z, CallbackInfoReturnable<BlockPos> info) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator && 
            modernBetaChunkGenerator.getChunkProvider().getSpawnLocator() != SpawnLocator.DEFAULT
        ) {
            int spawnY = world.getLevelProperties().getSpawnY();
            
            info.setReturnValue(new BlockPos(x, spawnY, z));
        }
    }
}
