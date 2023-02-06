package mod.bespectacled.modernbeta.world.biome;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class ModernBetaBiomeMobs {
    public static void addCommonMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);
    }
    
    public static void addSquid(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.SQUID, 10, 1, 4));
    }
    
    public static void addTurtles(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.TURTLE, 5, 2, 5));
    }
    
    public static void addWolves(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.WOLF, 5, 4, 4));
    }
    
    public static void addColdOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.SALMON, 15, 1, 5));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 1, 1, 2));
    }
    
    public static void addFrozenOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.SALMON, 15, 1, 5));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 1, 1, 2));
    }
    
    public static void addOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 1, 1, 2));
    }

    public static void addWarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addWarmOceanMobs(spawnSettings, 10, 4);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.PUFFERFISH, 15, 1, 3));
    }
    
    public static void addLukewarmOceanMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addOceanMobs(spawnSettings, 10, 4, 10);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.PUFFERFISH, 5, 1, 3));
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.TROPICAL_FISH, 25, 8, 8));
        spawnSettings.spawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 2, 1, 2));
    }
    
    public static void addDesertMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addDesertMobs(spawnSettings);
    }
    
    public static void addPlainsMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addPlainsMobs(spawnSettings);
    }
    
    public static void addRainforestMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.OCELOT, 2, 1, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.PANDA, 2, 1, 2));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.PARROT, 40, 1, 2));
    }
    
    public static void addSwamplandMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SLIME, 1, 1, 1));
    }
    
    public static void addTaigaMobs(SpawnSettings.Builder spawnSettings) {
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.WOLF, 5, 4, 4));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.RABBIT, 4, 2, 3));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.LLAMA, 4, 4, 6));
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.FOX, 8, 2, 4));
    }
    
    public static void addTundraMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addSnowyMobs(spawnSettings);
        
        // TODO: Move maybe later
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnEntry(EntityType.GOAT, 10, 4, 6));
    }
    
    public static void addSkyMobs(SpawnSettings.Builder spawnSettings) {
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 5, 20, false);
        spawnSettings.spawn(SpawnGroup.CREATURE,  new SpawnSettings.SpawnEntry(EntityType.CHICKEN, 10, 4, 4));
    }
}
