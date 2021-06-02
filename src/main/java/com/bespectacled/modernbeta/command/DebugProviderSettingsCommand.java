package com.bespectacled.modernbeta.command;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

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
        if (!(source.getWorld().getChunkManager().getChunkGenerator() instanceof OldChunkGenerator)) {
            source.sendFeedback(new LiteralText("Not a Modern Beta world!").formatted(Formatting.RED), false);
            return -1;
        }
        
        OldChunkGenerator oldChunkGenerator = (OldChunkGenerator)source.getWorld().getChunkManager().getChunkGenerator();
        
        String chunkProviderSettings = oldChunkGenerator.getProviderSettings().asString();
        String biomeProviderSettings = ((OldBiomeSource)oldChunkGenerator.getBiomeSource()).getProviderSettings().asString();
        
        source.sendFeedback(new LiteralText("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
        source.sendFeedback(new LiteralText(chunkProviderSettings), false);
        
        source.sendFeedback(new LiteralText("Biome Provider Settings:").formatted(Formatting.YELLOW), false);
        source.sendFeedback(new LiteralText(biomeProviderSettings), false);
        
        return 0;
    }
}
