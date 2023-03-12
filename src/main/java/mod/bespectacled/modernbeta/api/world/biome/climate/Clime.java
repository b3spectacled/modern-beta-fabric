package mod.bespectacled.modernbeta.api.world.biome.climate;

import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiComparable;

public record Clime(double temp, double rain) implements VoronoiComparable<Clime> {
    @Override
    public double calculateDistanceTo(Clime other) {
        return
            (this.temp() - other.temp()) * (this.temp() - other.temp()) +
            (this.rain() - other.rain()) * (this.rain() - other.rain());
    }
}
