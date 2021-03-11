package com.bespectacled.modernbeta.biome.indev;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.OldBiomes;
import com.bespectacled.modernbeta.biome.indev.IndevUtil.IndevTheme;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class IndevBiomes {
    public static final Identifier INDEV_NORMAL_ID = ModernBeta.createId("indev_normal");
    public static final Identifier INDEV_HELL_ID = ModernBeta.createId("indev_hell");
    public static final Identifier INDEV_PARADISE_ID = ModernBeta.createId("indev_paradise");
    public static final Identifier INDEV_WOODS_ID = ModernBeta.createId("indev_woods");
    public static final Identifier INDEV_SNOWY_ID = ModernBeta.createId("indev_snowy");
    
    public static final Map<IndevTheme, Identifier> BIOMES = new HashMap<IndevTheme, Identifier>();
    
    static {
        BIOMES.put(IndevTheme.NORMAL, INDEV_NORMAL_ID);
        BIOMES.put(IndevTheme.HELL, INDEV_HELL_ID);
        BIOMES.put(IndevTheme.PARADISE, INDEV_PARADISE_ID);
        BIOMES.put(IndevTheme.WOODS, INDEV_WOODS_ID);
        BIOMES.put(IndevTheme.SNOWY, INDEV_SNOWY_ID);
    }
    
    public static final List<RegistryKey<Biome>> BIOME_KEYS = 
        BIOMES
        .values()
        .stream()
        .map(i -> RegistryKey.of(Registry.BIOME_KEY, i))
        .collect(Collectors.toList());

    public static void registerBiomes() {
        // Unused, registered for compatibility with 1.16 versions
        OldBiomes.register(ModernBeta.createId("indev_edge"), IndevNormal.EDGE_COMPAT);
        OldBiomes.register(ModernBeta.createId("indev_hell_edge"), IndevHell.EDGE_COMPAT);
        OldBiomes.register(ModernBeta.createId("indev_paradise_edge"), IndevParadise.EDGE_COMPAT);
        OldBiomes.register(ModernBeta.createId("indev_woods_edge"), IndevWoods.EDGE_COMPAT);
        OldBiomes.register(ModernBeta.createId("indev_snowy_edge"), IndevSnowy.EDGE_COMPAT);
        
        OldBiomes.register(INDEV_NORMAL_ID, IndevNormal.BIOME);
        OldBiomes.register(INDEV_HELL_ID, IndevHell.BIOME);
        OldBiomes.register(INDEV_PARADISE_ID, IndevParadise.BIOME);
        OldBiomes.register(INDEV_WOODS_ID, IndevWoods.BIOME);
        OldBiomes.register(INDEV_SNOWY_ID, IndevSnowy.BIOME);
    }
}
