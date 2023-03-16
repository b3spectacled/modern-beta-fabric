package mod.bespectacled.modernbeta.mixin;

import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Inject(method = "setupSpawn", at = @At("RETURN"))
    private static void injectSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo info) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();

        // Set old spawn angle (doesn't seem to work?)
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            worldProperties.setSpawnAngle(-90.0f);
        }
    }
    
    @Redirect(
        method = "setupSpawn", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/network/SpawnLocating;findServerSpawnPoint(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/ChunkPos;)Lnet/minecraft/util/math/BlockPos;"
        )
    )
    private static BlockPos redirectSpawnLocating(ServerWorld world, ChunkPos chunkPos) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        BlockPos spawnPos = SpawnLocating.findServerSpawnPoint(world, chunkPos);
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            world.getGameRules().get(GameRules.SPAWN_RADIUS).set(0, world.getServer()); // Ensure a centered spawn
            spawnPos = modernBetaChunkGenerator.getChunkProvider().getSpawnLocator().locateSpawn().orElse(spawnPos);
            
            if (spawnPos != null && ModernBeta.DEV_ENV) {
                ModernBeta.log(Level.INFO, String.format("Spawning at %d/%d/%d", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()));
            }
            
            if (spawnPos != null && modernBetaChunkGenerator.getChunkProvider() instanceof ChunkProviderIndev chunkProviderIndev) {
                
                // Generate Indev house
                chunkProviderIndev.generateIndevHouse(world, spawnPos);
                
                // Set Indev world properties.
                setIndevProperties(world, chunkProviderIndev.getLevelTheme());
            }
        }
        
        return spawnPos;
    }
    
    @Unique
    private static void setIndevProperties(ServerWorld world, IndevTheme theme) {
        switch(theme) {
            case HELL -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(18000);
            } case PARADISE -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(6000);
            } case WOODS -> {
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setWeather(0, Integer.MAX_VALUE, true, false);
            } default -> {}
        }
    }
}
