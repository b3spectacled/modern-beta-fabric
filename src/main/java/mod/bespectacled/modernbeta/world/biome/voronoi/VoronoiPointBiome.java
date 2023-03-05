package mod.bespectacled.modernbeta.world.biome.voronoi;

import java.util.List;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class VoronoiPointBiome {
    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome("modern_beta:beta_forest", "modern_beta:beta_ocean", 0.5, 0.5);
    
    public String biome;
    public String oceanBiome;
    public String deepOceanBiome;
    public double temp;
    public double rain;
    
    public VoronoiPointBiome(String biome, String oceanBiome, double temp, double rain) {
        this(biome, oceanBiome, oceanBiome, temp, rain);
    }
    
    public VoronoiPointBiome(String biome, String oceanBiome, String deepOceanBiome,  double temp, double rain) {
        this.biome = biome;
        this.oceanBiome = oceanBiome;
        this.deepOceanBiome = deepOceanBiome;
        this.temp = temp;
        this.rain = rain;
    }
    
    public static List<VoronoiPointBiome> listFromCompound(NbtCompound compound, List<VoronoiPointBiome> alternate) {
        if (compound.contains(NbtTags.VORONOI_POINTS)) {
            return NbtUtil.toListOrThrow(compound.get(NbtTags.VORONOI_POINTS))
                .stream()
                .map(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    
                    return new VoronoiPointBiome(
                        NbtUtil.readStringOrThrow(NbtTags.BIOME, point),
                        NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, point),
                        NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, point),
                        NbtUtil.readDoubleOrThrow(NbtTags.TEMP, point),
                        NbtUtil.readDoubleOrThrow(NbtTags.RAIN, point)
                    );
                })
                .toList();
        }
        
        return List.copyOf(alternate);
    }
    
    public static NbtList listToNbt(List<VoronoiPointBiome> points) {
        NbtListBuilder builder = new NbtListBuilder();
        points.forEach(p -> builder.add(p.toCompound()));
        
        return builder.build();
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME, this.biome)
            .putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
            .putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
            .putDouble(NbtTags.TEMP, this.temp)
            .putDouble(NbtTags.RAIN, this.rain)
            .build();
    }
}
