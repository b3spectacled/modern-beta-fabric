package com.bespectacled.modernbeta.api.world.spawn;

import java.util.Optional;

import net.minecraft.util.math.BlockPos;

public interface SpawnLocator {
    Optional<BlockPos> locateSpawn();
    
    public static final SpawnLocator DEFAULT = new SpawnLocator() {
        @Override
        public Optional<BlockPos> locateSpawn() {
            return Optional.empty();
        }
    };
}
