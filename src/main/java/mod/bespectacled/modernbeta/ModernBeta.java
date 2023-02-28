package mod.bespectacled.modernbeta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import mod.bespectacled.modernbeta.client.color.BlockColors;
import mod.bespectacled.modernbeta.client.resource.ModernBetaColormapResource;
import mod.bespectacled.modernbeta.command.DebugProviderSettingsCommand;
import mod.bespectacled.modernbeta.compat.Compat;
import mod.bespectacled.modernbeta.config.ModernBetaConfig;
import mod.bespectacled.modernbeta.config.ModernBetaConfigBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome;
import mod.bespectacled.modernbeta.config.ModernBetaConfigChunk;
import mod.bespectacled.modernbeta.config.ModernBetaConfigRendering;
import mod.bespectacled.modernbeta.world.ModernBetaWorldInitializer;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.carver.ModernBetaCarvers;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import mod.bespectacled.modernbeta.world.feature.placement.ModernBetaPlacementTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class ModernBeta implements ModInitializer {
    public static final String MOD_ID = "modern_beta";
    public static final String MOD_NAME = "Modern Beta";
    
    public static final boolean CLIENT_ENV = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    public static final boolean DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static final ModernBetaConfig CONFIG = AutoConfig.register(
        ModernBetaConfig.class, 
        PartitioningSerializer.wrap(GsonConfigSerializer::new)
    ).getConfig();
    
    public static final ModernBetaConfigChunk CHUNK_CONFIG = CONFIG.chunkConfig;
    public static final ModernBetaConfigBiome BIOME_CONFIG = CONFIG.biomeConfig;
    public static final ModernBetaConfigCaveBiome CAVE_BIOME_CONFIG = CONFIG.caveBiomeConfig;
    public static final ModernBetaConfigRendering RENDER_CONFIG = CONFIG.renderingConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    public static Identifier createId(String name) {
        return new Identifier(MOD_ID, name);
    }
    
    public static void log(Level level, String message) {
        LOGGER.atLevel(level).log("[" + MOD_NAME + "] {}", message);
    }
    
    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing Modern Beta...");
        
        // Register mod stuff
        ModernBetaPlacementTypes.register();
        ModernBetaFeatures.register();
        ModernBetaCarvers.register();
        
        ModernBetaBiomeSource.register();
        ModernBetaChunkGenerator.register();
        
        // Set up mod compatibility
        Compat.setupCompat();
        
        // Register default providers
        ModernBetaBuiltInProviders.registerChunkProviders();
        ModernBetaBuiltInProviders.registerBiomeProviders();
        ModernBetaBuiltInProviders.registerCaveBiomeProviders();
        ModernBetaBuiltInProviders.registerSurfaceConfigs();
        ModernBetaBuiltInProviders.registerNoisePostProcessors();
        ModernBetaBuiltInProviders.registerBlockSources();

        if (CLIENT_ENV) {
            // Override default biome grass/foliage colors
            BlockColors.register();

            // Load colormaps
            BlockColorSampler colorSampler = BlockColorSampler.INSTANCE;
            ResourceManagerHelper resourceManager = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
            
            resourceManager.registerReloadListener(new ModernBetaColormapResource("colormap/water.png", colorSampler.colorMapWater::setColorMap));
            resourceManager.registerReloadListener(new ModernBetaColormapResource("colormap/underwater.png", colorSampler.colorMapUnderwater::setColorMap));
        }
        
        if (DEV_ENV) {
            DebugProviderSettingsCommand.register();
        }
        
        // Initializes chunk and biome providers at server start-up.
        ServerLifecycleEvents.SERVER_STARTING.register(ModernBetaWorldInitializer::init);
    }
}
