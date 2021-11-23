package com.bespectacled.modernbeta.world.spawn;

import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.FiniteChunkProvider;
import com.bespectacled.modernbeta.api.world.spawn.SpawnLocator;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

/*
 * Port of Indev 20100223 player spawn locator.
 * 
 */
public class IndevSpawnLocator implements SpawnLocator {
    private final FiniteChunkProvider chunkProvider;
    
    public IndevSpawnLocator(FiniteChunkProvider chunkProvider) {
        this.chunkProvider = chunkProvider;
    }

    
    @Override
    public Optional<BlockPos> locateSpawn() {
        Random random = new Random();
        
        int spawnX;
        int spawnY;
        int spawnZ;
        
        int attempts = 0;
        
        int width = this.chunkProvider.getLevelWidth();
        int length = this.chunkProvider.getLevelLength();
        //int height = this.chunkProvider.getLevelHeight();
        
        // block0
        while (true) {
            spawnX = random.nextInt(width / 2) + width / 4;
            spawnZ = random.nextInt(length / 2) + length / 4;
            //spawnY = this.chunkProvider.getLevelHighestBlock(spawnX, spawnZ) + 1;
            spawnY = this.chunkProvider.getHeight(spawnX - width / 2, spawnZ - length / 2, Heightmap.Type.OCEAN_FLOOR_WG) + 1;
            
            if (attempts >= 1000000) {
                ModernBeta.log(Level.INFO, "[Indev] Exceeded spawn attempts, spawning anyway..");
                //spawnY = height + 100; From original code, but tends to fail on small worlds
                
                break;
            }
            
            attempts++;
            
            if (spawnY < 4) 
                continue;
            
            if (spawnY <= this.chunkProvider.getSeaLevel()) 
                continue;
            
            if (this.nearSolidBlocks(spawnX, spawnY, spawnZ))
                continue;
            
            if (this.nearSolidBlocks2(spawnX, spawnY, spawnZ))
                continue;
            
            /* From original source, delegated to nearSolidBlocks function to avoid weird continue block code.
            for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
                for (int y = spawnY - 1; y <= y + 2; ++y) {
                    for (int z = spawnZ - 3 - 2; z <= spawnZ + 3; ++z) {
                        if (this.getBlockMaterial(x, y, z).isSolid()) continue block0;
                    }
                }
            }
            */
            
            /* From original source, delegated to nearSolidBlocks2 function to avoid weird continue block code.
            int y = spawnY - 2;
            for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
                for (int z = spawnZ - 3 - 2; z <= spawnZ + 3; ++z) {
                    if (Block.opaqueCubeLookup[this.getBlockId(x, y, z)]) continue;
                    continue block0;
                }
            }
            */
            
            break;
        }
        
        // Offset spawn coordinates since Indev worlds are centered on 0/0.
        return Optional.of(new BlockPos(spawnX - width / 2, spawnY - 1, spawnZ - length / 2));
    }
    
    private boolean nearSolidBlocks(int spawnX, int spawnY, int spawnZ) {
        for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
            for (int y = spawnY - 1; y <= spawnY + 2; ++y) {
                for (int z = spawnZ - 3 - 2; z <= spawnZ + 3; ++z) {
                    Block block = this.chunkProvider.getLevelBlock(x, y, z);
                    
                    // Check if nearby block is solid and continue if so.
                    if (!(block.equals(Blocks.AIR) || block.equals(this.chunkProvider.getLevelFluidBlock())))
                        return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean nearSolidBlocks2(int spawnX, int spawnY, int spawnZ) {
        int y = spawnY - 2;
        for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
            for (int z = spawnZ - 3 - 2; z <= spawnZ + 3; ++z) {
                Block block = this.chunkProvider.getLevelBlock(x, y, z);
                
                if (!(block.equals(Blocks.AIR) || block.equals(this.chunkProvider.getLevelFluidBlock())))
                    continue;
                
                return true;
            }
        }
        
        return false;
    }
}
