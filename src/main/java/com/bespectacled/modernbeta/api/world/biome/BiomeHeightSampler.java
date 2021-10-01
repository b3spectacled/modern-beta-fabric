package com.bespectacled.modernbeta.api.world.biome;

import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;

public interface BiomeHeightSampler {
	int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world);
	
	int getSeaLevel(); 
	
	public static final BiomeHeightSampler DEFAULT = new BiomeHeightSampler() {
		@Override
		public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
			return 64;
		}
		
		@Override
		public int getSeaLevel() {
			return 64;
		}
	};
}
