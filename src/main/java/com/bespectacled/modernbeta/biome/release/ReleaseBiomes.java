package com.bespectacled.modernbeta.biome.release;

public class ReleaseBiomes {
    public enum ReleaseBiome {
        OCEAN(0, 0, -1F, 0.4F),
        PLAINS(1, 1, 0.1F, 0.3F),
        DESERT(2, 2, 0.1F, 0.2F),
        MOUNTAINS(3, 3, 0.2F, 1.3F),
        FOREST(4, 4, 0.1F, 0.3F),
        TAIGA(5, 30, 0.1F, 0.4F),
        SWAMP(6, 6, -0.2F, 0.1F),
        RIVER(7, 7, -0.5F, 0.0F),
        FROZEN_OCEAN(10, 10, -1F, 0.5F),
        FROZEN_RIVER(11, 11, -0.5F, 0.0F),
        SNOWY_TUNDRA(12, 12, 0.1F, 0.3F),
        SNOWY_MOUNTAINS(13, 13, 0.2F, 1.2F),
        MUSHROOM_FIELDS(14, 14, 0.2F, 1.0F),
        MUSHROOM_SHORE(15, 15, -1F, 0.1F),
        BEACH(16, 16, 0.0F, 0.1F),
        DESERT_HILLS(17, 17, 0.2F, 0.7F),
        WOODED_HILLS(18, 18, 0.2F, 0.6F),
        TAIGA_HILLS(19, 31, 0.2F, 0.7F),
        MOUNTAIN_EDGE(20, 20, 0.2F, 0.3F);
        
        private final int id;
        private final int vanillaId;
        private final float depth;
        private final float scale;
        
        private ReleaseBiome(int id, int vanillaId, float depth, float scale) {
            this.id = id;
            this.vanillaId = vanillaId;
            this.depth = depth;
            this.scale = scale;
        }
        
        private ReleaseBiome(int id, int vanillaId) {
            this.id = id;
            this.vanillaId = vanillaId;
            this.depth = 0.1f;
            this.scale = 0.3f;
        }
        
        public int getId() {
            return this.id;
        }
        
        public int getVanillaId() {
            return this.vanillaId;
        }
        
        public float getDepth() {
            return this.depth;
        }
        
        public float getScale() {
            return this.scale;
        }
        
        public static ReleaseBiome fromId(int id) {
            for (ReleaseBiome b : ReleaseBiome.values()) {
                if (b.id == id) {
                    return b;
                }
            }
            
            throw new IllegalArgumentException("[Modern Beta] No release biome matching id: " + id);
        }
    }
}
