package com.bespectacled.modernbeta.world.biome.voronoi;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class VoronoiPointRules<T, S extends VoronoiComparable<S>> {
    private final List<VoronoiPoint<T, S>> voronoiPoints;
    
    private VoronoiPointRules(List<VoronoiPoint<T, S>> voronoiPoints) {
        this.voronoiPoints = ImmutableList.copyOf(voronoiPoints);
    }
    
    public VoronoiPoint<T, S> calculateClosestPointTo(S comparable) {
        VoronoiPoint<T, S> closestPoint = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (VoronoiPoint<T, S> point : this.voronoiPoints) {
            double distance = point.comparable().calculateDistanceTo(comparable);
            
            if (distance < closestDistance) {
                closestPoint = point;
                closestDistance = distance;
            }
        }
        
        return closestPoint;
    }
    
    public List<VoronoiPoint<T, S>> getRules() {
        return this.voronoiPoints;
    }
    
    public static class Builder<T, S extends VoronoiComparable<S>> {
        private final List<VoronoiPoint<T, S>> voronoiPoints;
        
        public Builder() {
            this.voronoiPoints = new ArrayList<>();
        }
        
        public Builder<T, S> add(T item, S comparable) {
            this.voronoiPoints.add(new VoronoiPoint<>(item, comparable));
            
            return this;
        }
        
        public VoronoiPointRules<T, S> build() {
            return new VoronoiPointRules<>(this.voronoiPoints);
        }
    }
    
    public record VoronoiPoint<T, S extends VoronoiComparable<S>>(T item, S comparable) {}
}
