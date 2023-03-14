package mod.bespectacled.modernbeta.world.biome.voronoi;

import java.util.List;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public record VoronoiPointBiome(String biome, String oceanBiome, String deepOceanBiome, double temp, double rain) {
    public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome("modern_beta:beta_forest", "modern_beta:beta_ocean", 0.5, 0.5);
    
    public VoronoiPointBiome(String biome, String oceanBiome, double temp, double rain) {
        this(biome, oceanBiome, oceanBiome, temp, rain);
    }


    public static List<VoronoiPointBiome> listFromReader(NbtReader reader, List<VoronoiPointBiome> alternate) {
        if (reader.contains(NbtTags.VORONOI_POINTS)) {
            return reader.readListOrThrow(NbtTags.VORONOI_POINTS)
                .stream()
                .map(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    NbtReader pointReader = new NbtReader(point);
                    
                    return new VoronoiPointBiome(
                        pointReader.readStringOrThrow(NbtTags.BIOME),
                        pointReader.readStringOrThrow(NbtTags.OCEAN_BIOME),
                        pointReader.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME),
                        pointReader.readDoubleOrThrow(NbtTags.TEMP),
                        pointReader.readDoubleOrThrow(NbtTags.RAIN)
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
