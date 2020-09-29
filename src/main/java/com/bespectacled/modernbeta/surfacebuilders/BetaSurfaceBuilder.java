package com.bespectacled.modernbeta.surfacebuilders;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

/** 
 * Based on Traverse/Terraform
 *
 */
public class BetaSurfaceBuilder {
	
	public static final Map<Identifier, SurfaceBuilder> SURFACE_BUILDERS = new HashMap<>();
	public static final Map<Identifier, ConfiguredSurfaceBuilder> CONFIGURED_SURFACE_BUILDERS = new HashMap<>();
	
	public static final SurfaceBuilder BEACH_SURFACE_BUILDER = add("beach_surface_builder", new BeachSurfaceBuilder(TernarySurfaceConfig.CODEC));
	public static final ConfiguredSurfaceBuilder BEACH = add("beach", new ConfiguredSurfaceBuilder<TernarySurfaceConfig>(
			BetaSurfaceBuilder.BEACH_SURFACE_BUILDER, 
			SurfaceBuilder.GRASS_CONFIG
		));
	
	static <S extends SurfaceBuilder<? extends SurfaceConfig>> S add(String name, S surfaceBuilder) {
		SURFACE_BUILDERS.put(new Identifier(ModernBeta.ID, name), surfaceBuilder); 
		return surfaceBuilder;
	}
	
	static <C extends ConfiguredSurfaceBuilder> C add(String name, C configuredSurfaceBuilder) {
		CONFIGURED_SURFACE_BUILDERS.put(new Identifier(ModernBeta.ID, name), configuredSurfaceBuilder); 
		return configuredSurfaceBuilder;
	}
	
	public static void register() {
		ModernBeta.LOGGER.log(Level.INFO, "Registering surface builders...");
		for (Identifier id : SURFACE_BUILDERS.keySet()) {
			Registry.register(Registry.SURFACE_BUILDER, id, SURFACE_BUILDERS.get(id));
		}
		for (Identifier id : CONFIGURED_SURFACE_BUILDERS.keySet()) {
			Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, id, CONFIGURED_SURFACE_BUILDERS.get(id));
		}
		
		ModernBeta.LOGGER.log(Level.INFO, "Registered surface builders.");
	}
}
