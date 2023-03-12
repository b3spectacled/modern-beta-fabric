package mod.bespectacled.modernbeta.command;

import static net.minecraft.server.command.CommandManager.literal;

import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.world.biome.OldBiomeSource;
import mod.bespectacled.modernbeta.world.gen.OldChunkGenerator;
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
            
            StringBuilder builder = new StringBuilder();
            Settings chunkSettings = oldChunkGenerator.getChunkSettings();
            
            chunkSettings.keySet().forEach(key -> {
                builder.append(String.format("* %s: %s\n", key, chunkSettings.get(key).toString()));
            });

            source.sendFeedback(new LiteralText("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(builder.toString()), false);
        }
        
        if (source.getWorld().getChunkManager().getChunkGenerator().getBiomeSource() instanceof OldBiomeSource oldBiomeSource) {
            validWorld = true;
            
            StringBuilder builder0 = new StringBuilder();
            Settings biomeSettings = oldBiomeSource.getBiomeSettings();
            
            biomeSettings.keySet().forEach(key -> {
                builder0.append(String.format("* %s: %s\n", key, biomeSettings.get(key).toString()));
            });
            
            source.sendFeedback(new LiteralText("Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(builder0.toString()), false);
            
            StringBuilder builder1 = new StringBuilder();
            Settings caveBiomeSettings = oldBiomeSource.getCaveBiomeSettings();
            
            caveBiomeSettings.keySet().forEach(key -> {
                builder1.append(String.format("* %s: %s\n", key, caveBiomeSettings.get(key).toString()));
            });
            
            source.sendFeedback(new LiteralText("Cave Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(new LiteralText(builder1.toString()), false);
        }

        if (validWorld) {
            return 0;
        } 

        source.sendFeedback(new LiteralText("Not a Modern Beta world!").formatted(Formatting.RED), false);
        return -1;
    }
}
