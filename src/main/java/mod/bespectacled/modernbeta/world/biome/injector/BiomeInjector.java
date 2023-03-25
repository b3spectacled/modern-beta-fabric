package mod.bespectacled.modernbeta.world.biome.injector;

import java.util.function.Predicate;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.mixin.AccessorChunkSection;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjectionRules.BiomeInjectionContext;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

public class BiomeInjector {
    public static final int OCEAN_START_DEPTH = 4;
    public static final int OCEAN_DEEP_START_DEPTH = 16;
    public static final int CAVE_START_DEPTH = 8;
    
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;
    
    private final BiomeInjectionRules rules;
    
    public BiomeInjector(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;

        boolean useOceanBiomes = ModernBetaSettingsBiome.fromCompound(this.modernBetaBiomeSource.getBiomeSettings()).useOceanBiomes;
        
        Predicate<BiomeInjectionContext> cavePredicate = context -> 
            context.getY() >= context.worldMinY && context.getY() + CAVE_START_DEPTH < context.minHeight;

        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_START_DEPTH);

        Predicate<BiomeInjectionContext> deepOceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_DEEP_START_DEPTH);
            
        BiomeInjectionRules.Builder builder = new BiomeInjectionRules.Builder();
        
        builder.add(cavePredicate, this.modernBetaBiomeSource::getCaveBiome);
        
        if (useOceanBiomes) {
            builder.add(deepOceanPredicate, this.modernBetaBiomeSource::getDeepOceanBiome);
            builder.add(oceanPredicate, this.modernBetaBiomeSource::getOceanBiome);
        }
        
        this.rules = builder.build();
    }
    
    public void injectBiomes(Chunk chunk, MultiNoiseSampler noiseSampler) {
        ChunkPos chunkPos = chunk.getPos();
        
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
                    
                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeY = localBiomeY + yOffset;
                        RegistryEntry<Biome> biome = this.getBiome(biomeX, biomeY, biomeZ, noiseSampler);
                        
                        container.set(localBiomeX, localBiomeY, localBiomeZ, biome);
                    }   
                }
            }
            
            ((AccessorChunkSection)section).setBiomeContainer(container);
        }
    }
    
    public RegistryEntry<Biome> getBiomeAtBlock(int x, int y, int z, MultiNoiseSampler noiseSampler) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        return this.getBiome(biomeX, biomeY, biomeZ, noiseSampler);
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler) {
        int y = biomeY << 2;
        
        int worldMinY = this.modernBetaChunkGenerator.getMinimumY();
        int topHeight = this.sampleTopHeight(biomeX, biomeZ);
        int minHeight = this.sampleMinHeight(biomeX, biomeZ);
        
        BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, topHeight, minHeight).setY(y);

        return this.getBiome(context, biomeX, biomeY, biomeZ, noiseSampler);
    }
    
    private RegistryEntry<Biome> getBiome(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler) {
        RegistryEntry<Biome> biome = this.rules.test(context, biomeX, biomeY, biomeZ);
        
        if (biome == null) {
            biome = this.modernBetaBiomeSource.getBiome(biomeX, biomeY, biomeZ, noiseSampler);
        }
        
        return biome;
    }
    
    private int sampleTopHeight(int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        return this.modernBetaChunkGenerator.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    private int sampleFloorHeight(int biomeX, int biomeZ) {
        int x = (biomeX << 2) + 2;
        int z = (biomeZ << 2) + 2;
        
        ChunkProvider chunkProvider = this.modernBetaChunkGenerator.getChunkProvider();
        
        return chunkProvider instanceof ChunkProviderNoise chunkProviderNoise ?
            chunkProviderNoise.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
            chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
    }
    
    private int sampleMinHeight(int centerBiomeX, int centerBiomeZ) {
        int minHeight = Integer.MAX_VALUE;
        
        for (int localBiomeX = -1; localBiomeX <= 1; ++localBiomeX) {
            for (int localBiomeZ = -1; localBiomeZ <= 1; ++localBiomeZ) {
                int biomeX = centerBiomeX + localBiomeX;
                int biomeZ = centerBiomeZ + localBiomeZ;
                
                minHeight = Math.min(minHeight, this.sampleFloorHeight(biomeX, biomeZ));
            }
        }
        
        return minHeight;
    }

    private boolean atOceanDepth(int topHeight, int oceanDepth) {
        return topHeight < this.modernBetaChunkGenerator.getSeaLevel() - oceanDepth;
    }
}
