package com.bespectacled.modernbeta.api.biome;

public class CaveBiomeProviderType {
    public enum BuiltInCaveBiomeType {
        NONE("none"),
        VANILLA("vanilla")
        ;
        
        public final String id;
        
        private BuiltInCaveBiomeType(String id) { this.id = id; }
    }
}
