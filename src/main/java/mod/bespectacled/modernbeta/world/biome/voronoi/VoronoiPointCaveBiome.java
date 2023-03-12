package mod.bespectacled.modernbeta.world.biome.voronoi;

import java.util.List;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public record VoronoiPointCaveBiome(String biome, double temp, double rain, double depth) {
    public static final VoronoiPointCaveBiome DEFAULT = new VoronoiPointCaveBiome("minecraft:lush_caves", 0.5, 0.5, 0.0);
    
    public static List<VoronoiPointCaveBiome> listFromReader(NbtReader reader, List<VoronoiPointCaveBiome> alternate) {
        if (reader.contains(NbtTags.VORONOI_POINTS)) {
            return reader.readListOrThrow(NbtTags.VORONOI_POINTS)
                .stream()
                .map(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    NbtReader pointReader = new NbtReader(point);
                    
                    return new VoronoiPointCaveBiome(
                        pointReader.readStringOrThrow(NbtTags.BIOME),
                        pointReader.readDoubleOrThrow(NbtTags.TEMP),
                        pointReader.readDoubleOrThrow(NbtTags.RAIN),
                        pointReader.readDoubleOrThrow(NbtTags.DEPTH)
                    );
                })
                .toList();
        }
        
        return List.copyOf(alternate);
    }
    
    public static NbtList listToNbt(List<VoronoiPointCaveBiome> points) {
        NbtListBuilder builder = new NbtListBuilder();
        points.forEach(p -> builder.add(p.toCompound()));
        
        return builder.build();
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME, this.biome)
            .putDouble(NbtTags.TEMP, this.temp)
            .putDouble(NbtTags.RAIN, this.rain)
            .putDouble(NbtTags.DEPTH, this.depth)
            .build();
    }
}
