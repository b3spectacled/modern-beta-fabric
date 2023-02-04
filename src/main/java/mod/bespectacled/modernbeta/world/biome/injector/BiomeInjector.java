package mod.bespectacled.modernbeta.world.biome.injector;

import java.util.function.Predicate;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.mixin.MixinChunkSection;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

public class BiomeInjector {
    public static final int OCEAN_MIN_DEPTH = 4;
    public static final int DEEP_OCEAN_MIN_DEPTH = 16;
    
    public static final int CAVE_START_OFFSET = 8;
    public static final int CAVE_END_OFFSET = 16;
    
    public static final Predicate<BiomeInjectionContext> CAVE_PREDICATE = context ->
        context.getY() >= context.worldMinY && context.getY() + CAVE_START_OFFSET < context.minHeight;
        
    public static final Predicate<BiomeInjectionContext> DEEP_CAVE_PREDICATE = context ->
        context.getY() >= context.worldMinY && context.getY() < context.minHeight / 2;
    
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;
    private final ChunkProvider chunkProvider;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;
        this.chunkProvider = modernBetaChunkGenerator.getChunkProvider();
        
        boolean replaceOceanBiomes = new ModernBetaSettingsBiome.Builder(this.modernBetaBiomeSource.getBiomeSettings()).build().replaceOceanBiomes;
        
        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
        
        Predicate<BiomeInjectionContext> deepOceanPredicate = context ->
            this.atOceanDepth(context.topHeight, DEEP_OCEAN_MIN_DEPTH) && 
            context.topState.isOf(Blocks.WATER);
            
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder();
        
        builder.add(CAVE_PREDICATE, this.modernBetaBiomeSource::getCaveBiome);
        builder.add(DEEP_CAVE_PREDICATE, this.modernBetaBiomeSource::getCaveBiome);
        
        if (replaceOceanBiomes) {
            builder.add(deepOceanPredicate, this.modernBetaBiomeSource::getDeepOceanBiome);
            builder.add(oceanPredicate, this.modernBetaBiomeSource::getOceanBiome);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        BlockPos.Mutable pos = new BlockPos.Mutable();
        
        int startBiomeX = chunkPos.getStartX() >> 2;
        int startBiomeZ = chunkPos.getStartZ() >> 2;
        
        /*
         * Collect the following for an x/z coordinate:
         * -> Height at local biome coordinate.
         * -> Minimum height of area around local biome coordinate.
         * -> Blockstate at height of local biome coordinate.
         */
        
        // Replace biomes from biome container
        for (int sectionY = 0; sectionY < chunk.countVerticalSections(); ++sectionY) {
            ChunkSection section = chunk.getSection(sectionY);
            PalettedContainer<RegistryEntry<Biome>> container = section.getBiomeContainer().slice();
            
            int yOffset = section.getYOffset() >> 2;
            
            for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                    int biomeX = localBiomeX + startBiomeX;
                    int biomeZ = localBiomeZ + startBiomeZ;
                    
                    int x = (biomeX << 2) + 2;
                    int z = (biomeZ << 2) + 2;
                    
                    int worldMinY = this.chunkProvider.getWorldMinY();
                    int topHeight = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                    int minHeight = this.sampleMinHeightAround(biomeX, biomeZ);
                    
                    BlockState topState = chunk.getBlockState(pos.set(x, topHeight, z));
                    BlockState minState = chunk.getBlockState(pos.set(x, minHeight, z));
                    
                    BiomeInjectionContext context = new BiomeInjectionContext(
                        worldMinY,
                        topHeight,
                        minHeight,
                        topState,
                        minState
                    );
                    
                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeY = localBiomeY + yOffset;
                        int y = (localBiomeY + yOffset) << 2;
                        
                        context.setY(y);
                        RegistryEntry<Biome> biome = this.sample(context, biomeX, biomeY, biomeZ);
                        
                        if (biome == null) {
                            biome = this.modernBetaBiomeSource.getBiome(biomeX, biomeY, biomeZ, null);
                        }
                        
                        container.set(localBiomeX, localBiomeY, localBiomeZ, biome);
                    }   
                }
            }
            
            ((MixinChunkSection)section).setBiomeContainer(container);
        }
    }
    
    public RegistryEntry<Biome> sample(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ) {
        return this.rules.test(context, biomeX, biomeY, biomeZ);
    }
    
    public int getCenteredHeight(int biomeX, int biomeZ) {
        // Offset by 2 to get center of biome coordinate section in block coordinates
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider ?
            noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    public int sampleMinHeightAround(int centerBiomeX, int centerBiomeZ) {
        int minHeight = Integer.MAX_VALUE;
        
        for (int areaBiomeX = -1; areaBiomeX <= 1; ++areaBiomeX) {
            for (int areaBiomeZ = -1; areaBiomeZ <= 1; ++areaBiomeZ) {
                int biomeX = centerBiomeX + areaBiomeX;
                int biomeZ = centerBiomeZ + areaBiomeZ;
                
                minHeight = Math.min(minHeight, this.getCenteredHeight(biomeX, biomeZ));
            }
        }
        
        return minHeight;
    }

    private boolean atOceanDepth(int topHeight, int oceanDepth) {
        return topHeight < this.modernBetaChunkGenerator.getSeaLevel() - oceanDepth;
    }
}
