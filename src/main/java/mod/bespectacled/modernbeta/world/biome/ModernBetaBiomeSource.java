package mod.bespectacled.modernbeta.world.biome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverBlock;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverOcean;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;

public class ModernBetaBiomeSource extends BiomeSource {
    public static final Codec<ModernBetaBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            RegistryOps.getEntryLookupCodec(RegistryKeys.BIOME),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings),
            NbtCompound.CODEC.fieldOf("cave_provider_settings").forGetter(biomeSource -> biomeSource.caveBiomeSettings)
        ).apply(instance, (instance).stable(ModernBetaBiomeSource::new)));
    
    private final NbtCompound biomeSettings;
    private final NbtCompound caveBiomeSettings;
    
    private final BiomeProvider biomeProvider;
    private final CaveBiomeProvider caveBiomeProvider;
    
    private ModernBetaChunkGenerator chunkGenerator;
    
    private boolean initializedBiomeProvider;
    private boolean initializedCaveBiomeProvider;
    
    private static List<RegistryEntry<Biome>> getBiomesForRegistry(
        RegistryEntryLookup<Biome> biomeRegistry,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        ModernBetaSettingsBiome modernBetaBiomeSettings = new ModernBetaSettingsBiome.Builder(biomeSettings).build();
        ModernBetaSettingsCaveBiome modernBetaCaveBiomeSettings = new ModernBetaSettingsCaveBiome.Builder(caveBiomeSettings).build();
        
        BiomeProvider biomeProvider  = ModernBetaRegistries.BIOME
            .get(modernBetaBiomeSettings.biomeProvider)
            .apply(biomeSettings, biomeRegistry);
        
        CaveBiomeProvider caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
            .get(modernBetaCaveBiomeSettings.biomeProvider)
            .apply(caveBiomeSettings, biomeRegistry);
        
        biomeProvider.initProvider(0L);
        caveBiomeProvider.initProvider(0L);
        
        List<RegistryEntry<Biome>> biomes = new ArrayList<>();
        biomes.addAll(biomeProvider.getBiomesForRegistry());
        biomes.addAll(caveBiomeProvider.getBiomesForRegistry());
        
        return biomes;
    }
    
    public ModernBetaBiomeSource(
        RegistryEntryLookup<Biome> biomeRegistry,
        NbtCompound biomeSettings,
        NbtCompound caveBiomeSettings
    ) {
        super(getBiomesForRegistry(biomeRegistry, biomeSettings, caveBiomeSettings));
        
        this.biomeSettings = biomeSettings;
        this.caveBiomeSettings = caveBiomeSettings;
        
        ModernBetaSettingsBiome modernBetaBiomeSettings = new ModernBetaSettingsBiome.Builder(this.biomeSettings).build();
        ModernBetaSettingsCaveBiome modernBetaCaveBiomeSettings = new ModernBetaSettingsCaveBiome.Builder(this.caveBiomeSettings).build();
        
        this.biomeProvider = ModernBetaRegistries.BIOME
            .get(modernBetaBiomeSettings.biomeProvider)
            .apply(biomeSettings, biomeRegistry);
        
        this.caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
            .get(modernBetaCaveBiomeSettings.biomeProvider)
            .apply(caveBiomeSettings, biomeRegistry);
        
        this.chunkGenerator = null;
        
        this.initializedBiomeProvider = false;
        this.initializedCaveBiomeProvider = false;
    }
    
    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        this.initializeBiomeProvider();

        return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    @Override
    public Set<RegistryEntry<Biome>> getBiomesInArea(int startX, int startY, int startZ, int radius, MultiNoiseSampler noiseSampler) {
        if (this.chunkGenerator == null)
            return super.getBiomesInArea(startX, startY, startZ, radius, noiseSampler);
        
        int minX = BiomeCoords.fromBlock(startX - radius);
        int minZ = BiomeCoords.fromBlock(startZ - radius);
        
        int maxX = BiomeCoords.fromBlock(startX + radius);
        int maxZ = BiomeCoords.fromBlock(startZ + radius);
        
        int rangeX = maxX - minX + 1;
        int rangeZ = maxZ - minZ + 1;
        
        HashSet<RegistryEntry<Biome>> set = Sets.newHashSet();
        for (int localZ = 0; localZ < rangeZ; ++localZ) {
            for (int localX = 0; localX < rangeX; ++localX) {
                int biomeX = minX + localX;
                int biomeZ = minZ + localZ;
                
                int x = biomeX << 2;
                int z = biomeZ << 2;
                int y = this.chunkGenerator.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
                
                set.add(this.chunkGenerator.getInjectedBiomeAtBlock(x, y, z, noiseSampler));
            }
        }
        
        return set;
    }
    
    @Override
    public Pair<BlockPos, RegistryEntry<Biome>> locateBiome(
        BlockPos origin,
        int radius,
        int horizontalBlockCheckInterval,
        int verticalBlockCheckInterval,
        Predicate<RegistryEntry<Biome>> predicate,
        MultiNoiseUtil.MultiNoiseSampler noiseSampler,
        WorldView world
    ) {
        if (this.chunkGenerator == null)
            return super.locateBiome(origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
        
        Set<RegistryEntry<Biome>> set = this.getBiomes().stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
        if (set.isEmpty()) {
            return null;
        }
        
        int searchRadius = Math.floorDiv(radius, horizontalBlockCheckInterval);
        int[] sections = MathHelper.stream(origin.getY(), world.getBottomY() + 1, world.getTopY(), verticalBlockCheckInterval).toArray();
        
        for (BlockPos.Mutable mutable : BlockPos.iterateInSquare(BlockPos.ORIGIN, searchRadius, Direction.EAST, Direction.SOUTH)) {
            int x = origin.getX() + mutable.getX() * horizontalBlockCheckInterval;
            int z = origin.getZ() + mutable.getZ() * horizontalBlockCheckInterval;
            
            int biomeX = BiomeCoords.fromBlock(x);
            int biomeZ = BiomeCoords.fromBlock(z);
            
            for (int y : sections) {
                RegistryEntry<Biome> biome = this.chunkGenerator.getInjectedBiome(biomeX, BiomeCoords.fromBlock(y), biomeZ, noiseSampler);
                
                if (!set.contains(biome)) continue;
                
                return Pair.of(new BlockPos(x, y, z), biome);
            }
        }
        return null;
    }

    public RegistryEntry<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        if (this.biomeProvider instanceof BiomeResolverOcean oceanBiomeResolver)
            return oceanBiomeResolver.getOceanBiome(biomeX, biomeY, biomeZ);
        
        return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        if (this.biomeProvider instanceof BiomeResolverOcean oceanBiomeResolver)
            return oceanBiomeResolver.getDeepOceanBiome(biomeX, biomeY, biomeZ);
        
        return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getCaveBiome(int biomeX, int biomeY, int biomeZ) {
        this.initializeCaveBiomeProvider();
        
        return this.caveBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
    }
    
    public RegistryEntry<Biome> getBiomeForSurfaceGen(int x, int y, int z) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver) {
            return biomeResolver.getBiomeBlock(x, y, z);
        }
        
        return this.biomeProvider.getBiome(x >> 2, y >> 2, z >> 2);
    }
    
    public RegistryEntry<Biome> getBiomeForSurfaceGen(ChunkRegion region, BlockPos pos) {
        if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver)
            return biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());
        
        return region.getBiome(pos);
    }
    
    public void setChunkGenerator(ModernBetaChunkGenerator chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.biomeProvider;
    }
    
    public CaveBiomeProvider getCaveBiomeProvider() {
        return this.caveBiomeProvider;
    }
    
    public NbtCompound getBiomeSettings() {
        return this.biomeSettings;
    }
    
    public NbtCompound getCaveBiomeSettings() {
        return this.caveBiomeSettings;
    }
    
    public long getWorldSeed() {
        return ModernBeta.getWorldSeed();
    }

    public static void register() {
        Registry.register(net.minecraft.registry.Registries.BIOME_SOURCE, ModernBeta.createId(ModernBeta.MOD_ID), CODEC);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return ModernBetaBiomeSource.CODEC;
    }
    
    private void initializeBiomeProvider() {
        if (!this.initializedBiomeProvider)
            this.initializedBiomeProvider = this.biomeProvider.initProvider(this.getWorldSeed());
    }
    
    private void initializeCaveBiomeProvider() {
        if (!this.initializedCaveBiomeProvider)
            this.initializedCaveBiomeProvider = this.caveBiomeProvider.initProvider(this.getWorldSeed());
    }
}