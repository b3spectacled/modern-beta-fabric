package com.bespectacled.modernbeta.api.world.biome;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class BiomeProvider {
    protected final long seed;
    protected final NbtCompound settings;
    
    private final Set<Identifier> missingBiomes;
    
    /**
     * Constructs a Modern Beta biome provider initialized with seed.
     * Additional settings are supplied in NbtCompound parameter.
     * 
     * @param biomeSource Parent OldBiomeSource object used to initialize fields.
     */
    public BiomeProvider(OldBiomeSource biomeSource) {
        this.seed = biomeSource.getWorldSeed();
        this.settings = biomeSource.getProviderSettings();

        this.missingBiomes = new HashSet<>();
    }
    
    /**
     * Gets a biome for biome source at given biome coordinates.
     * Note that a single biome coordinate unit equals 4 blocks.
     * 
     * @param biomeRegistry
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public abstract Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ);
    
    /**
     * Gets a biome to overwrite the original biome at given biome coordinates and sufficient depth.
     * 
     * @param biomeRegistry
     * @param biomeX x-coordinate in biome coordinates.
     * @param biomeY y-coordinate in biome coordinates.
     * @param biomeZ z-coordinate in biome coordinates.
     * 
     * @return A biome at given biome coordinates.
     */
    public Biome getOceanBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        return this.getBiomeForNoiseGen(biomeRegistry, biomeX, biomeY, biomeZ);
    }
    
    /**
     * Gets a list of biome registry keys for biome source, for the purpose of locating structures, etc.
     * 
     * @return A list of biome registry keys.
     */
    public abstract List<RegistryKey<Biome>> getBiomesForRegistry();
    
    
    /**
     * Gets a biome from the registry given a biome Identifier.
     * If the requested biome is not found (e.g. modded biome from removed mod),
     * then the provided default biome Identifier is used instead 
     * and an error is emitted once per missing biome.
     * 
     * @param biomeRegistry
     * @param biomeId Requested biome Identifier.
     * @param defaultBiomeId Fallback biome Identifier.
     * 
     * @return The requested biome.
     */
    protected Biome getBiomeOrElse(Registry<Biome> biomeRegistry, Identifier biomeId, Identifier defaultBiomeId) {
        Optional<Biome> biome = biomeRegistry.getOrEmpty(biomeId);
        
        if (biome.isEmpty() && !this.missingBiomes.contains(biomeId)) {
            ModernBeta.log(Level.ERROR, "Biome provider cannot retrieve biome named " + biomeId.toString() + ", getting default entry named " + defaultBiomeId.toString());
            this.missingBiomes.add(biomeId);
        }
        
        // If custom biome is not present for whatever reason, fetch the default.
        return biome.orElse(biomeRegistry.get(defaultBiomeId));
    }
}
