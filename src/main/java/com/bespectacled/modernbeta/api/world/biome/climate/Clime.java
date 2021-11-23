package com.bespectacled.modernbeta.api.world.biome.climate;

public class Clime {
    private final double temp;
    private final double rain;
    
    public Clime(double temp, double rain) {
        this.temp = temp;
        this.rain = rain;
    }
    
    public double temp() {
        return this.temp;
    }
    
    public double rain() {
        return this.rain;
    }
}