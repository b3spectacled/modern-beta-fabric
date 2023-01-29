package mod.bespectacled.modernbeta.world.biome.provider;

import java.util.List;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends BiomeProvider implements ClimateSampler {
    private static final Identifier DEFAULT_BIOME_ID = new Identifier("plains");
    
    private final RegistryKey<Biome> biomeKey;
    private final Clime biomeClime;
    
    public SingleBiomeProvider(long seed, NbtCompound settings, Registry<Biome> biomeRegistry) {
        super(seed, settings, biomeRegistry);
        
        this.biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(NbtUtil.toString(settings.get(NbtTags.SINGLE_BIOME), DEFAULT_BIOME_ID.toString())));
        this.biomeClime = new Clime(
            MathHelper.clamp(biomeRegistry.get(this.biomeKey).getTemperature(), 0.0, 1.0),
            MathHelper.clamp(biomeRegistry.get(this.biomeKey).getDownfall(), 0.0, 1.0)
        );
    }

    @Override
    public RegistryEntry<Biome> getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        return this.biomeRegistry.getOrCreateEntry(this.biomeKey);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {
        return List.of(this.biomeRegistry.getOrCreateEntry(this.biomeKey));
    }

    @Override
    public Clime sample(int x, int z) {
        return this.biomeClime;
    }
}
