package mod.bespectacled.modernbeta.command;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DebugProviderSettingsCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("printprovidersettings")
                .requires(source -> source.hasPermissionLevel(2))
                    .executes(ctx -> execute(ctx.getSource())));
        });
    }
    
    private static int execute(ServerCommandSource source) {
        boolean validWorld = false;
        
        if (source.getWorld().getChunkManager().getChunkGenerator() instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            validWorld = true;
            
            StringBuilder builder = new StringBuilder();
            NbtCompound chunkSettings = ModernBetaSettingsChunk.fromCompound(modernBetaChunkGenerator.getChunkSettings()).toCompound();
            
            chunkSettings.getKeys().forEach(key -> {
                builder.append(String.format("* %s: %s\n", key, chunkSettings.get(key).toString()));
            });

            source.sendFeedback(() -> Text.literal("Chunk Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(builder.toString()), false);
        }
        
        if (source.getWorld().getChunkManager().getChunkGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            validWorld = true;
            
            StringBuilder builder0 = new StringBuilder();
            NbtCompound biomeSettings = ModernBetaSettingsBiome.fromCompound(modernBetaBiomeSource.getBiomeSettings()).toCompound();
            
            biomeSettings.getKeys().forEach(key -> {
                builder0.append(String.format("* %s: %s\n", key, biomeSettings.get(key).toString()));
            });
            
            source.sendFeedback(() -> Text.literal("Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(builder0.toString()), false);
            
            StringBuilder builder1 = new StringBuilder();
            NbtCompound caveBiomeSettings = ModernBetaSettingsCaveBiome.fromCompound(modernBetaBiomeSource.getCaveBiomeSettings()).toCompound();
            
            caveBiomeSettings.getKeys().forEach(key -> {
                builder1.append(String.format("* %s: %s\n", key, caveBiomeSettings.get(key).toString()));
            });
            
            source.sendFeedback(() -> Text.literal("Cave Biome Provider Settings:").formatted(Formatting.YELLOW), false);
            source.sendFeedback(() -> Text.literal(builder1.toString()), false);
        }

        if (validWorld) {
            return 0;
        } 

        source.sendFeedback(() -> Text.literal("Not a Modern Beta world!").formatted(Formatting.RED), false);
        
        return -1;
    }
}
