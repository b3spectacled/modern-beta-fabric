/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bespectacled.modernbeta.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeArray;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.bespectacled.modernbeta.util.BiomeMath;
import com.bespectacled.modernbeta.util.MutableBiomeArray;

@Mixin(BiomeArray.class)
public abstract class MixinBiomeArray implements MutableBiomeArray {
	@Shadow
	private Biome[] data;

	@Override
	public void setBiome(int x, int y, int z, Biome biome) {
		this.data[BiomeMath.computeBiomeIndex(x, y, z)] = biome;
	}
	
	public Biome getBiome(int x, int y, int z) {
		return this.data[BiomeMath.computeBiomeIndex(x, y, z)];
	}
}