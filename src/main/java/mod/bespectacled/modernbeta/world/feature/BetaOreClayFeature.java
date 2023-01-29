package mod.bespectacled.modernbeta.world.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkSectionCache;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BetaOreClayFeature extends Feature<OreFeatureConfig> {
    public BetaOreClayFeature(Codec<OreFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<OreFeatureConfig> featureContext) {
        StructureWorldAccess world = featureContext.getWorld();
        BlockPos pos = featureContext.getOrigin();
        OreFeatureConfig config = featureContext.getConfig();
        Random random = featureContext.getRandom();
        
        int baseX = pos.getX();
        int baseY = pos.getY();
        int baseZ = pos.getZ();
        
        int numberOfBlocks = config.size;
        
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        
        if(!world.testBlockState(pos, state -> state.isOf(Blocks.WATER))) {
            return false;
        }
        
        float radius = random.nextFloat() * 3.141593F;
        
        double x0 = (float)(baseX + 8) + (MathHelper.sin(radius) * (float)numberOfBlocks) / 8F;
        double x1 = (float)(baseX + 8) - (MathHelper.sin(radius) * (float)numberOfBlocks) / 8F;
        double z0 = (float)(baseZ + 8) + (MathHelper.cos(radius) * (float)numberOfBlocks) / 8F;
        double z1 = (float)(baseZ + 8) - (MathHelper.cos(radius) * (float)numberOfBlocks) / 8F;
        
        double y0 = baseY + random.nextInt(3) + 2;
        double y1 = baseY + random.nextInt(3) + 2;
        
        try (ChunkSectionCache chunkSectionCache = new ChunkSectionCache(world)) {
            for(int block = 0; block <= numberOfBlocks; block++) {
                double d6 = x0 + ((x1 - x0) * (double)block) / (double)numberOfBlocks;
                double d7 = y0 + ((y1 - y0) * (double)block) / (double)numberOfBlocks;
                double d8 = z0 + ((z1 - z0) * (double)block) / (double)numberOfBlocks;
                
                double d9 = (random.nextDouble() * (double)numberOfBlocks) / 16D;
                
                double d10 = (double)(MathHelper.sin(((float)block * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                double d11 = (double)(MathHelper.sin(((float)block * 3.141593F) / (float)numberOfBlocks) + 1.0F) * d9 + 1.0D;
                
                int minX = MathHelper.floor(d6 - d10 / 2D);
                int maxX = MathHelper.floor(d6 + d10 / 2D);
                int minY = MathHelper.floor(d7 - d11 / 2D);
                int maxY = MathHelper.floor(d7 + d11 / 2D);
                int minZ = MathHelper.floor(d8 - d10 / 2D);
                int maxZ = MathHelper.floor(d8 + d10 / 2D);
                
                for(int x = minX; x <= maxX; x++) {
                    for(int y = minY; y <= maxY; y++) {
                        for(int z = minZ; z <= maxZ; z++) {
                            double dX = (((double)x + 0.5D) - d6) / (d10 / 2D);
                            double dY = (((double)y + 0.5D) - d7) / (d11 / 2D);
                            double dZ = (((double)z + 0.5D) - d8) / (d10 / 2D);
                            
                            // Check bounds
                            if(dX * dX + dY * dY + dZ * dZ >= 1.0D) {
                                continue;
                            }

                            ChunkSection chunkSection = chunkSectionCache.getSection(mutablePos.set(x, y, z));
                            
                            if (!world.isOutOfHeightLimit(y) && chunkSection != null) {
                                int localX = ChunkSectionPos.getLocalCoord(x); 
                                int localY = ChunkSectionPos.getLocalCoord(y);
                                int localZ = ChunkSectionPos.getLocalCoord(z);
                                BlockState state = chunkSection.getBlockState(localX, localY, localZ);
                                
                                for (final OreFeatureConfig.Target target : config.targets) {
                                    if (OreFeature.shouldPlace(state, chunkSectionCache::getBlockState, random, config, target, mutablePos)) {
                                        chunkSection.setBlockState(localX, localY, localZ, target.state, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
}
