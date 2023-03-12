package mod.bespectacled.modernbeta.client.gui.screen;

import java.util.Set;
import java.util.function.Consumer;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.ModernBetaBuiltInWorldProviders;
import mod.bespectacled.modernbeta.api.registry.Registries;
import mod.bespectacled.modernbeta.api.world.WorldProvider;
import mod.bespectacled.modernbeta.client.gui.wrapper.ActionOptionWrapper;
import mod.bespectacled.modernbeta.client.gui.wrapper.CyclingOptionWrapper;
import mod.bespectacled.modernbeta.util.GUIUtil;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import mod.bespectacled.modernbeta.util.settings.MutableSettings;
import mod.bespectacled.modernbeta.util.settings.Settings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings;
import mod.bespectacled.modernbeta.util.settings.WorldSettings.WorldSetting;
import mod.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import mod.bespectacled.modernbeta.world.cavebiome.provider.settings.CaveBiomeProviderSettings;
import mod.bespectacled.modernbeta.world.gen.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.gen.provider.settings.ChunkProviderSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.DynamicRegistryManager;

public class WorldScreen extends GUIScreen {
    private final WorldSettings worldSettings;
    protected final Consumer<WorldSettings> consumer;
    
    protected final DynamicRegistryManager registryManager;
    protected final WorldProvider worldProvider;

    public WorldScreen(CreateWorldScreen parent, WorldSettings worldSettings, Consumer<WorldSettings> consumer) {
        super("createWorld.customize.worldType.title", parent);
        
        this.worldSettings = worldSettings;
        this.consumer = consumer;
        
        this.registryManager = parent.moreOptionsDialog.getRegistryManager();
        this.worldProvider = Registries.WORLD.get(NbtUtil.toStringOrThrow(this.getChunkSetting(NbtTags.WORLD_TYPE)));
    }
    
    @Override
    protected void init() {
        super.init();
        this.preProcessOptions();
        
        ButtonWidget doneButton;
        ButtonWidget cancelButton;
        
        doneButton = new ButtonWidget(
            this.width / 2 - 155, this.height - 28, 150, 20, 
            ScreenTexts.DONE, 
            buttonWidget -> {
                this.consumer.accept(this.worldSettings);
                this.client.setScreen(this.parent);
            }
        );
        
        cancelButton = new ButtonWidget(
            this.width / 2 + 5, this.height - 28, 150, 20, 
            ScreenTexts.CANCEL,
            buttonWidget -> {
                this.client.setScreen(this.parent);
            }
        );
        
        this.addDrawableChild(doneButton);
        this.addDrawableChild(cancelButton);
        
        String biomeType = NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.BIOME_TYPE));
        String caveBiomeType = NbtUtil.toStringOrThrow(this.getCaveBiomeSetting(NbtTags.CAVE_BIOME_TYPE));
        
        CyclingOptionWrapper<WorldProvider> worldTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.worldType", 
            Registries.WORLD.getEntries().stream().toArray(WorldProvider[]::new),
            () -> this.worldProvider,
            value -> {
                // Queue world type changes
                this.worldSettings.clear();
                this.worldSettings.replace(
                    WorldSetting.CHUNK, 
                    Registries.CHUNK_SETTINGS
                        .getOrEmpty(value.getChunkProvider())
                        .orElse(() -> ChunkProviderSettings.createSettingsDefault(value.getChunkProvider()))
                        .get()
                );

                this.worldSettings.replace(
                    WorldSetting.BIOME, 
                    Registries.BIOME_SETTINGS
                        .getOrEmpty(value.getBiomeProvider())
                        .orElse(() -> BiomeProviderSettings.createSettingsDefault(value.getBiomeProvider()))
                        .get()
                );
                
                this.worldSettings.replace(
                    WorldSetting.CAVE_BIOME, 
                    Registries.CAVE_BIOME_SETTINGS
                        .getOrEmpty(value.getCaveBiomeProvider())
                        .orElse(() -> CaveBiomeProviderSettings.createSettingsDefault(value.getCaveBiomeProvider()))
                        .get()
                );
                
                // Replace single biome if applicable
                this.setDefaultSingleBiome(value.getBiomeProvider(), value.getSingleBiome());
                this.postProcessOptions(value);
                
                // Create new world screen
                this.resetWorldScreen(value);
            }
        );
        
        CyclingOptionWrapper<String> biomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.biomeType",
            Registries.BIOME.getKeySet().stream().toArray(String[]::new),
            () -> NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.BIOME, NbtTags.BIOME_TYPE)),
            value -> {
                // Queue biome settings changes
                this.worldSettings.clear(WorldSetting.BIOME);
                this.worldSettings.replace(
                    WorldSetting.BIOME,
                    Registries.BIOME_SETTINGS
                        .getOrEmpty(value)
                        .orElse(() -> BiomeProviderSettings.createSettingsDefault(value))
                        .get()
                );
                
                // Replace single biome if applicable
                this.setDefaultSingleBiome(value, this.worldProvider.getSingleBiome());
                this.postProcessOptions(this.worldProvider);
                
                this.resetWorldScreen(this.worldProvider);
            }
        );
        
        CyclingOptionWrapper<String> caveBiomeTypeOption = new CyclingOptionWrapper<>(
            "createWorld.customize.caveBiomeType",
            Registries.CAVE_BIOME.getKeySet().stream().toArray(String[]::new),
            () -> NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.CAVE_BIOME, NbtTags.CAVE_BIOME_TYPE)),
            value -> {
                // Queue cave biome settings changes
                this.worldSettings.clear(WorldSetting.CAVE_BIOME);
                this.worldSettings.replace(
                    WorldSetting.CAVE_BIOME, 
                    Registries.CAVE_BIOME_SETTINGS
                        .getOrEmpty(value)
                        .orElse(() -> CaveBiomeProviderSettings.createSettingsDefault(value))
                        .get()
                );
            
                this.resetWorldScreen(this.worldProvider);
            }
        );
        
        Screen worldSettingsScreen = Registries.WORLD_SCREEN
            .getOrDefault(this.worldProvider.getWorldScreen())
            .apply(this, WorldSetting.CHUNK);
        
        ActionOptionWrapper worldSettingsOption = new ActionOptionWrapper(
            "createWorld.customize.settings",
            worldSettingsScreen != null ? widget -> this.client.setScreen(worldSettingsScreen) : null
        ).tooltips(() -> this.client.textRenderer.wrapLines(new LiteralText(this.settingsToString(WorldSetting.CHUNK)), 250));
        
        Screen biomeSettingsScreen = Registries.BIOME_SCREEN
            .getOrDefault(NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.BIOME, NbtTags.BIOME_TYPE)))
            .apply(this, WorldSetting.BIOME); 
        
        ActionOptionWrapper biomeSettingsOption = new ActionOptionWrapper(
            biomeType.equals(ModernBetaBuiltInTypes.Biome.SINGLE.name) ? "createWorld.customize.biome" :  "createWorld.customize.settings",
            biomeSettingsScreen != null ? widget -> this.client.setScreen(biomeSettingsScreen) : null
        ).suffix(biomeType.equals(ModernBetaBuiltInTypes.Biome.SINGLE.name) ? 
            GUIUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.SINGLE_BIOME))) : ""
        ).tooltips(() -> this.client.textRenderer.wrapLines(new LiteralText(this.settingsToString(WorldSetting.BIOME)), 250));
        
        Screen caveBiomeSettingsScreen = Registries.CAVE_BIOME_SCREEN
            .getOrDefault(NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.CAVE_BIOME, NbtTags.CAVE_BIOME_TYPE)))
            .apply(this, WorldSetting.CAVE_BIOME);
        
        ActionOptionWrapper caveBiomeSettingsOption = new ActionOptionWrapper(
            caveBiomeType.equals(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name) ? "createWorld.customize.biome" : "createWorld.customize.settings",
            caveBiomeSettingsScreen != null ? widget -> this.client.setScreen(caveBiomeSettingsScreen) : null
        ).suffix(caveBiomeType.equals(ModernBetaBuiltInTypes.CaveBiome.SINGLE.name) ? 
            GUIUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getCaveBiomeSetting(NbtTags.SINGLE_BIOME))) :  ""
        ).tooltips(() -> this.client.textRenderer.wrapLines(new LiteralText(this.settingsToString(WorldSetting.CAVE_BIOME)), 250));
        
        this.addDualOption(worldTypeOption, worldSettingsOption);
        this.addDualOption(biomeTypeOption, biomeSettingsOption);
        this.addDualOption(caveBiomeTypeOption, caveBiomeSettingsOption);
    }
    
    public DynamicRegistryManager getRegistryManager() {
        return this.registryManager;
    }
    
    public WorldSettings getWorldSettings() {
        return this.worldSettings;
    }
    
    /* Convenience methods */
    
    protected void setDefaultSingleBiome(String defaultBiome) {
        String biomeType = NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.BIOME, NbtTags.BIOME_TYPE));
        
        this.setDefaultSingleBiome(biomeType, defaultBiome);
    }
    
    protected void setDefaultSingleBiome(String biomeType, String defaultBiome) {
        // Replace default single biome with one supplied by world provider, if switching to Single biome type
        if (biomeType.equals(ModernBetaBuiltInTypes.Biome.SINGLE.name)) { 
            this.worldSettings.put(WorldSetting.BIOME, NbtTags.SINGLE_BIOME, NbtString.of(defaultBiome));
        }
    }
    
    protected NbtElement getChunkSetting(String key) {
        return this.worldSettings.get(WorldSetting.CHUNK, key);
    }
    
    protected NbtElement getBiomeSetting(String key) {
        return this.worldSettings.get(WorldSetting.BIOME, key);
    }
    
    protected NbtElement getCaveBiomeSetting(String key) {
        return this.worldSettings.get(WorldSetting.CAVE_BIOME, key);
    }
    
    protected void resetWorldScreen(WorldProvider worldProvider) {
        this.client.setScreen(
            new WorldScreen(
                (CreateWorldScreen)this.parent,
                this.worldSettings,
                this.consumer
            )
        );
    }
    
    private void preProcessOptions() {
        // Replace single biome if Indev world type
        if (this.worldProvider == ModernBetaBuiltInWorldProviders.INDEV) {
            String levelTheme = NbtUtil.toStringOrThrow(this.worldSettings.get(WorldSetting.CHUNK, NbtTags.LEVEL_THEME));

            this.setDefaultSingleBiome(IndevTheme.fromName(levelTheme).getDefaultBiome().toString());   
        }
    }
    
    private void postProcessOptions(WorldProvider worldProvider) {}
    
    private String settingsToString(WorldSetting setting) {
        Settings settings = MutableSettings.copyOf(this.worldSettings.get(setting));
        
        StringBuilder builder = new StringBuilder().append("Settings\n");
        Set<String> keys = settings.keySet();
        
        // Remove main settings tag
        keys.remove(setting.tag);
        
        int row = 0;
        int cutoff = 5;
        
        for (String key : keys) {
            if (row >= cutoff) {
                builder.append(String.format("... and %d more...", keys.size() - cutoff));
                break;
            }

            NbtElement element = settings.get(key);
            String elementStr = (element instanceof NbtByte nbtByte) ?
                Boolean.valueOf(nbtByte.byteValue() == 1).toString():
                element.toString();
            
            // Truncate string if too long
            if (elementStr.length() > 50)
                elementStr = "[ ... ]";
            
            builder.append(String.format("* %s: %s", key, elementStr));
            if (row < keys.size() - 1)
                builder.append("\n");
            
            row++;
        }
        
        return builder.toString();
    }
}
