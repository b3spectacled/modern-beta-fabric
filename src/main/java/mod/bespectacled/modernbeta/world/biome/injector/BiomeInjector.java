package mod.bespectacled.modernbeta.world.biome.injector;

import java.util.Optional;
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
import net.minecraft.world.chunk.ReadableContainer;

public class BiomeInjector {
    public enum BiomeInjectionStep {
        PRE,  // Injects before surface generation step.
        POST, // Injects after surface generation step.
        ALL   // Injects for structure generation, spawn location.
    }
    
    public static final int OCEAN_START_DEPTH = 4;
    public static final int OCEAN_DEEP_START_DEPTH = 16;
    public static final int CAVE_START_DEPTH = 8;
    
    private final ModernBetaChunkGenerator modernBetaChunkGenerator;
    private final ModernBetaBiomeSource modernBetaBiomeSource;
    
    private final BiomeInjectionRules rulesPre;
    private final BiomeInjectionRules rulesPost;
    private final BiomeInjectionRules rulesAll;
    
    public BiomeInjector(ModernBetaChunkGenerator modernBetaChunkGenerator, ModernBetaBiomeSource modernBetaBiomeSource) {
        this.modernBetaChunkGenerator = modernBetaChunkGenerator;
        this.modernBetaBiomeSource = modernBetaBiomeSource;
        
        ModernBetaSettingsBiome settingsBiome;
        settingsBiome = ModernBetaSettingsBiome.fromCompound(this.modernBetaBiomeSource.getBiomeSettings());

        boolean useOceanBiomes = settingsBiome.useOceanBiomes;
        
        Predicate<BiomeInjectionContext> cavePredicate = context -> 
            context.getY() >= context.worldMinY && context.getY() + CAVE_START_DEPTH < context.minHeight;

        Predicate<BiomeInjectionContext> oceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_START_DEPTH);

        Predicate<BiomeInjectionContext> deepOceanPredicate = context -> 
            this.atOceanDepth(context.topHeight, OCEAN_DEEP_START_DEPTH);
        
        BiomeInjectionRules.Builder builderPre = new BiomeInjectionRules.Builder();
        BiomeInjectionRules.Builder builderPost = new BiomeInjectionRules.Builder();
        BiomeInjectionRules.Builder builderAll = new BiomeInjectionRules.Builder();
        
        builderPost.add(cavePredicate, this.modernBetaBiomeSource::getCaveBiome);
        
        if (useOceanBiomes) {
            builderPost.add(deepOceanPredicate, this.modernBetaBiomeSource::getDeepOceanBiome);
            builderPost.add(oceanPredicate, this.modernBetaBiomeSource::getOceanBiome);
        }
        
        builderAll.add(builderPre).add(builderPost);
        
        this.rulesPre = builderPre.build();
        this.rulesPost = builderPost.build();
        this.rulesAll = builderAll.build();
    }
    
    public void injectBiomes(Chunk chunk, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
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
            
            ReadableContainer<RegistryEntry<Biome>> readableContainer = section.getBiomeContainer();
            PalettedContainer<RegistryEntry<Biome>> palettedContainer = section.getBiomeContainer().slice();
            
            int yOffset = section.getYOffset() >> 2;
            
            for (int localBiomeX = 0; localBiomeX < 4; ++localBiomeX) {
                for (int localBiomeZ = 0; localBiomeZ < 4; ++localBiomeZ) {
                    int biomeX = localBiomeX + startBiomeX;
                    int biomeZ = localBiomeZ + startBiomeZ;
                    
                    for (int localBiomeY = 0; localBiomeY < 4; ++localBiomeY) {
                        int biomeY = localBiomeY + yOffset;
                        
                        RegistryEntry<Biome> initialBiome = readableContainer.get(localBiomeX, localBiomeY, localBiomeZ);
                        RegistryEntry<Biome> replacementBiome = this.getOptionalBiome(biomeX, biomeY, biomeZ, noiseSampler, step).orElse(initialBiome);
                        
                        palettedContainer.set(localBiomeX, localBiomeY, localBiomeZ, replacementBiome);
                    }   
                }
            }
            
            ((AccessorChunkSection)section).setBiomeContainer(palettedContainer);
        }
    }
    
    public RegistryEntry<Biome> getBiomeAtBlock(int x, int y, int z, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
        int biomeX = x >> 2;
        int biomeY = y >> 2;
        int biomeZ = z >> 2;
        
        return this.getBiome(biomeX, biomeY, biomeZ, noiseSampler, step);
    }
    
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
        BiomeInjectionContext context = this.createContext(biomeX, biomeY, biomeZ);

        return this
            .getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step)
            .orElse(this.modernBetaBiomeSource.getBiome(biomeX, biomeY, biomeZ, noiseSampler));
    }
    
    public Optional<RegistryEntry<Biome>> getOptionalBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
        BiomeInjectionContext context = this.createContext(biomeX, biomeY, biomeZ);

        return this.getBiome(context, biomeX, biomeY, biomeZ, noiseSampler, step);
    }
    
    private Optional<RegistryEntry<Biome>> getBiome(BiomeInjectionContext context, int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
        RegistryEntry<Biome> biome = switch(step) {
            case PRE -> this.rulesPre.test(context, biomeX, biomeY, biomeZ);
            case POST -> this.rulesPost.test(context, biomeX, biomeY, biomeZ);
            case ALL -> this.rulesAll.test(context, biomeX, biomeY, biomeZ);
        };
        
        return Optional.ofNullable(biome);
    }
    
    private BiomeInjectionContext createContext(int biomeX, int biomeY, int biomeZ) {
        int y = biomeY << 2;
        
        int worldMinY = this.modernBetaChunkGenerator.getMinimumY();
        int topHeight = this.sampleTopHeight(biomeX, biomeZ);
        int minHeight = this.sampleMinHeight(biomeX, biomeZ);
        
        BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, topHeight, minHeight).setY(y);
        
        return context;
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
