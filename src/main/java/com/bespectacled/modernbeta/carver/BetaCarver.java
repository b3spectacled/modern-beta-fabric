package com.bespectacled.modernbeta.carver;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.surfacebuilders.BeachSurfaceBuilder;
import com.bespectacled.modernbeta.surfacebuilders.BetaSurfaceBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class BetaCarver {
    public static final Map<Identifier, Carver> CARVERS = new HashMap<>();
    public static final Map<Identifier, ConfiguredCarver> CONFIGURED_CARVERS = new HashMap<>();

    public static final Carver BETA_CAVE_CARVER = add("beta_cave", new BetaCaveCarver(ProbabilityConfig.CODEC, 128));
    public static final Carver INDEV_CAVE_CARVER = add("indev_cave", new IndevCaveCarver(ProbabilityConfig.CODEC, 128, 1f));
    
    public static final ConfiguredCarver CONF_BETA_CAVE_CARVER = add("beta_cave",
            new ConfiguredCarver(BETA_CAVE_CARVER, new ProbabilityConfig(1f)));
    public static final ConfiguredCarver CONF_INDEV_CAVE_CARVER = add("indev_cave",
            new ConfiguredCarver(INDEV_CAVE_CARVER, new ProbabilityConfig(0.01f)));

    static <C extends Carver> C add(String name, C carver) {
        CARVERS.put(new Identifier(ModernBeta.ID, name), carver);
        return carver;
    }

    static <C extends ConfiguredCarver> C add(String name, C configuredCarver) {
        CONFIGURED_CARVERS.put(new Identifier(ModernBeta.ID, name), configuredCarver);
        return configuredCarver;
    }

    public static void register() {
        for (Identifier id : CARVERS.keySet()) {
            Registry.register(Registry.CARVER, id, CARVERS.get(id));
        }
        for (Identifier id : CONFIGURED_CARVERS.keySet()) {
            Registry.register(BuiltinRegistries.CONFIGURED_CARVER, id, CONFIGURED_CARVERS.get(id));
        }

        ModernBeta.LOGGER.log(Level.INFO, "Registered carvers.");
    }
}
