package com.bespectacled.modernbeta.biome.provider;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.biome.release.BiomeCache;
import com.bespectacled.modernbeta.biome.release.BiomeLayer;
import com.bespectacled.modernbeta.biome.release.IntCache;
import com.bespectacled.modernbeta.biome.release.TestVanillaBiomeLayer;
import com.bespectacled.modernbeta.biome.release.ReleaseBiomes.ReleaseBiome;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeLayerSampler;

public class ReleaseBiomeProvider extends AbstractBiomeProvider {
    
    private final BiomeLayer[] biomeSampler;
    private final BiomeCache biomeCache;
    
    public ReleaseBiomeProvider(long seed) {
        this.biomeSampler = BiomeLayer.build(seed, 4, 6);
        this.biomeCache = new BiomeCache(this);
    }
    
    /*
     * public BiomeGenBase[] func_35557_b(BiomeGenBase abiomegenbase[], int i, int j, int k, int l)
    {
        IntCache.resetIntCache();
        if (abiomegenbase == null || abiomegenbase.length < k * l)
        {
            abiomegenbase = new BiomeGenBase[k * l];
        }
        int ai[] = mainLayer.sample(i, j, k, l);
        for (int i1 = 0; i1 < k * l; i1++)
        {
            abiomegenbase[i1] = BiomeGenBase.biomeList[ai[i1]];
        }

        return abiomegenbase;
    }
     */

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> registry, int biomeX, int biomeY, int biomeZ) {
        int absX = biomeX << 2;
        int absZ = biomeZ << 2;
        
        // Works but slow and unstable!
        //IntCache.resetIntCache();
        //return registry.get(this.biomeSampler.sample(biomeX, biomeZ, 1, 1)[0]);
        
        ReleaseBiome biome = this.biomeCache.getBiomeGenAt(absX, absZ);
            
        
        return registry.get(biome.getVanillaId());
        
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();

        for (ReleaseBiome b : ReleaseBiome.values()) {
            biomeList.add(RegistryKey.of(Registry.BIOME_KEY, BuiltinRegistries.BIOME.getId(BuiltinRegistries.BIOME.get(b.getVanillaId()))));
        }
        
        return biomeList;
    }
    
    public void getBiomesForNoiseGen(ReleaseBiome[] biomes, int x, int z, int sizeX, int sizeZ) {
        IntCache.resetIntCache();
        
        if (biomes == null || biomes.length < sizeX * sizeZ) {
            biomes = new ReleaseBiome[sizeX * sizeZ];
        }
        
        int layer[] = biomeSampler[0].sample(x, z, sizeX, sizeZ);
        for (int i = 0; i < sizeX * sizeZ; i++){
            biomes[i] = ReleaseBiome.fromId(layer[i]);
        }
    }
    
    public ReleaseBiome[] getBiomeGenAt(ReleaseBiome[] biomes, int x, int z, int sizeX, int sizeZ, boolean isCached)
    {
        IntCache.resetIntCache();
        //System.out.println("Getting biomes");
        if (biomes == null || biomes.length < sizeX * sizeZ) {
            biomes = new ReleaseBiome[sizeX * sizeZ];
        }
        
        if (isCached && sizeX == 16 && sizeZ == 16 && (x & 0xf) == 0 && (z & 0xf) == 0) {
            ReleaseBiome abiomegenbase1[] = biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase1, 0, biomes, 0, sizeX * sizeZ);
            return biomes;
        }
        
        int layer[] = biomeSampler[1].sample(x, z, sizeX, sizeZ);
        for (int i = 0; i < sizeX * sizeZ; i++) {
            //System.out.println("Populating biomes");
            //biomes[i1] = BiomeGenBase.biomeList[layer[i1]];
            biomes[i] = ReleaseBiome.fromId(layer[i]);
        }

        return biomes;
    }
    

}
