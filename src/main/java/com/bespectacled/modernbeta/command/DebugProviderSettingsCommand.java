package com.bespectacled.modernbeta.command;

import static net.minecraft.server.command.CommandManager.literal;

import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

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
        
        if (source.getWorld().getChunkManager().getChunkGenerator() instanceof OldChunkGenerator oldChunkGenerator) {
            validWorld = true;
            
            String chunkProviderSettings = oldChunkGenerator.getChunkSettings().asString();
            
            source.sendFeedback(new LiteralText("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(chunkProviderSettings), false);
        }
        
        if (source.getWorld().getChunkManager().getChunkGenerator().getBiomeSource() instanceof OldBiomeSource oldBiomeSource) {
            validWorld = true;
            
            String biomeProviderSettings = oldBiomeSource.getBiomeSettings().asString();
            
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
