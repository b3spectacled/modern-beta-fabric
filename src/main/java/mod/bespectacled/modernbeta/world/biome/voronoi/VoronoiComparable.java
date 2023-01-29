package mod.bespectacled.modernbeta.world.biome.voronoi;

public interface VoronoiComparable<T> {
    double calculateDistanceTo(T other);
}
