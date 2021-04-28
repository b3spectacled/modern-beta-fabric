package com.bespectacled.modernbeta.mixin;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.world.biome.indev.IndevUtil.IndevTheme;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.gen.provider.IndevChunkProvider;

import org.spongepowered.asm.mixin.injection.At;
import net.fabricmc.fabric.mixin.gamerule.IntRuleAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Unique private static Random spawnRand = new Random();
    
    @Redirect(
        method = "setupSpawn", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/network/SpawnLocating;findServerSpawnPoint(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/ChunkPos;Z)Lnet/minecraft/util/math/BlockPos;"
        )
    )
    private static BlockPos redirectSpawnLocating(ServerWorld world, ChunkPos chunkPos, boolean validSpawnNeeded) {
        ChunkGenerator gen = world.getChunkManager().getChunkGenerator();
        BlockPos spawnPos = SpawnLocating.findServerSpawnPoint(world, chunkPos, validSpawnNeeded);
        
        if (gen instanceof OldChunkGenerator) {
            ((IntRuleAccessor)world.getGameRules().get(GameRules.SPAWN_RADIUS)).setValue(0); // Ensure a centered spawn
            OldChunkGenerator oldGen = (OldChunkGenerator)gen;
           
            PerlinOctaveNoise beachNoiseOctaves = oldGen.getChunkProvider().getBeachNoise();
            
            if (beachNoiseOctaves != null) { // Attempt to place a beach spawn if provider generates classic beaches.
                ModernBeta.LOGGER.log(Level.INFO, "Setting a beach spawn..");
                spawnPos = getInitialOldSpawn(oldGen, beachNoiseOctaves, oldGen.getSeaLevel());
            }
            
            if (spawnPos != null && oldGen.isProviderInstanceOf(IndevChunkProvider.class)) {
                ModernBeta.LOGGER.log(Level.INFO, "[Indev] Spawning..");
                IndevChunkProvider indevChunkProvider = (IndevChunkProvider)oldGen.getChunkProvider();
                
                // Ensure spawn location and Indev house is within level bounds.
                if (!indevChunkProvider.inWorldBounds(spawnPos.getX(), spawnPos.getZ())) {
                    spawnPos = new BlockPos(0, indevChunkProvider.getHeight(0, 0, Heightmap.Type.WORLD_SURFACE_WG), 0);
                }
                
                // Generate Indev house
                indevChunkProvider.generateIndevHouse(world, spawnPos);
                
                // Set Indev world properties
                IndevTheme theme = indevChunkProvider.getTheme();
                setIndevProperties(world, theme);
            }
        } 
        
        return spawnPos;
    }
    
    @Unique
    private static BlockPos getInitialOldSpawn(OldChunkGenerator gen, PerlinOctaveNoise beachNoiseOctaves, int seaLevel) {
        int x = 0;
        int z;
        int attempts = 0;
        
        for (z = 0; !isBlockSand(x, z, gen, beachNoiseOctaves, seaLevel); z += spawnRand.nextInt(64) - spawnRand.nextInt(64)) {
            if (attempts >= 10000) {
                ModernBeta.LOGGER.log(Level.INFO, "Exceeded spawn attempts, spawning anyway..");
                break;
            }
            attempts++;
            
            x += spawnRand.nextInt(64) - spawnRand.nextInt(64);
        }
        
        int y = gen.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        
        return new BlockPos(x, y - 1, z);
    }
    
    @Unique
    private static boolean isBlockSand(int x, int z, OldChunkGenerator gen, PerlinOctaveNoise beachNoiseOctaves, int seaLevel) {
        double thirtysecond = 0.03125D;
        int y = gen.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR);
        Biome biome = gen.getBiomeSource().getBiomeForNoiseGen(x >> 2, 0, z >> 2);
        
        //System.out.println("Height found at " + x + "/" + z + ": " + y);
        
        return 
            (biome.getGenerationSettings().getSurfaceConfig().getTopMaterial() == BlockStates.SAND && y >= seaLevel - 1) || 
            (beachNoiseOctaves.sample(x * thirtysecond, z * thirtysecond, 0.0) + spawnRand.nextDouble() * 0.2 > 0.0 && y > seaLevel - 1 && y <= seaLevel + 1);
    }
    
    @Unique
    private static void setIndevProperties(ServerWorld world, IndevTheme theme) {
        switch(theme) {
            case HELL:
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(18000);
                break;
            case WOODS:
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setWeather(0, Integer.MAX_VALUE, true, false);
                break;
            case PARADISE:
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(6000);
                break;
            default:
                break;
        }
    }
    
}
