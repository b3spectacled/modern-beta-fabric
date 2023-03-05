package mod.bespectacled.modernbeta.world.biome.voronoi;

import java.util.List;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class VoronoiPointCaveBiome {
    public static final VoronoiPointCaveBiome DEFAULT = new VoronoiPointCaveBiome("minecraft:lush_caves", 0.5, 0.5, 0.0);
    
    public String biome;
    public double temp;
    public double rain;
    public double depth;
    
    public VoronoiPointCaveBiome(String biome, double temp, double rain, double depth) {
        this.biome = biome;
        this.temp = temp;
        this.rain = rain;
        this.depth = depth;
    }
    
    public static List<VoronoiPointCaveBiome> listFromCompound(NbtCompound compound, List<VoronoiPointCaveBiome> alternate) {
        if (compound.contains(NbtTags.VORONOI_POINTS)) {
            return NbtUtil.toListOrThrow(compound.get(NbtTags.VORONOI_POINTS))
                .stream()
                .map(e -> {
                    NbtCompound point = NbtUtil.toCompoundOrThrow(e);
                    
                    return new VoronoiPointCaveBiome(
                        NbtUtil.readStringOrThrow(NbtTags.BIOME, point),
                        NbtUtil.readDoubleOrThrow(NbtTags.TEMP, point),
                        NbtUtil.readDoubleOrThrow(NbtTags.RAIN, point),
                        NbtUtil.readDoubleOrThrow(NbtTags.DEPTH, point)
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
