package com.bespectacled.modernbeta.command;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import static net.minecraft.server.command.CommandManager.*;

import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

public class DebugProviderSettingsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("printprovidersettings")
                .requires(source -> source.hasPermissionLevel(2))
                    .executes(ctx -> execute(ctx.getSource())));
        });
    }
    
    private static int execute(ServerCommandSource source) {
        boolean validWorld = false;
        
        ChunkGenerator chunkGenerator = source.getWorld().getChunkManager().getChunkGenerator();
        BiomeSource biomeSource = source.getWorld().getChunkManager().getChunkGenerator().getBiomeSource();
        
        if (chunkGenerator instanceof OldChunkGenerator) {
            validWorld = true;
            
            String chunkProviderSettings = ((OldChunkGenerator)chunkGenerator).getProviderSettings().asString();
            
            source.sendFeedback(new LiteralText("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(chunkProviderSettings), false);
        }
        
        if (biomeSource instanceof OldBiomeSource) {
            validWorld = true;
            
            String biomeProviderSettings = ((OldBiomeSource)biomeSource).getProviderSettings().asString();
            
            source.sendFeedback(new LiteralText("Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(biomeProviderSettings), false);
        }

        if (validWorld) {
            return 0;
        } 

        source.sendFeedback(new LiteralText("Not a Modern Beta world!").formatted(Formatting.RED), false);
        return -1;
    }
}
