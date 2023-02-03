package mod.bespectacled.modernbeta.api.world.cavebiome.climate;

import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiComparable;

public record CaveClime(double temp, double rain, double depth) implements VoronoiComparable<CaveClime> {
    public CaveClime(double temp, double rain) {
        this(temp, rain, 0.0);
    }
    
    @Override
    public double calculateDistanceTo(CaveClime other) {
        return
            (this.temp() - other.temp()) * (this.temp() - other.temp()) +
            (this.rain() - other.rain()) * (this.rain() - other.rain()) +
            (this.depth() - other.depth()) * (this.depth() - other.depth());
    }
}

