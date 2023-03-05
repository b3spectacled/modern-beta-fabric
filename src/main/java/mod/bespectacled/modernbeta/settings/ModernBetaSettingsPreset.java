package mod.bespectacled.modernbeta.settings;

import org.slf4j.event.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class ModernBetaSettingsPreset {
    public enum SettingsType {
        CHUNK,
        BIOME,
        CAVE_BIOME
    }
    
    private final ModernBetaSettingsChunk settingsChunk;
    private final ModernBetaSettingsBiome settingsBiome;
    private final ModernBetaSettingsCaveBiome settingsCaveBiome;
    
    public ModernBetaSettingsPreset(
        NbtCompound newSettingsChunk,
        NbtCompound newSettingsBiome,
        NbtCompound newSettingsCaveBiome
    ) {
        this(
            ModernBetaSettingsChunk.fromCompound(newSettingsChunk),
            ModernBetaSettingsBiome.fromCompound(newSettingsBiome),
            ModernBetaSettingsCaveBiome.fromCompound(newSettingsCaveBiome)
       );
    }
    
    public ModernBetaSettingsPreset(
        ModernBetaSettingsChunk settingsChunk,
        ModernBetaSettingsBiome settingsBiome,
        ModernBetaSettingsCaveBiome settingsCaveBiome
    ) {
        this.settingsChunk = settingsChunk;
        this.settingsBiome = settingsBiome;
        this.settingsCaveBiome = settingsCaveBiome;
    }
    
    public NbtCompound getCompound(SettingsType settingsType) {
        ModernBetaSettings settings = switch(settingsType) {
            case CHUNK -> this.settingsChunk;
            case BIOME -> this.settingsBiome;
            case CAVE_BIOME -> this.settingsCaveBiome;
        };
        
        return settings.toCompound();
    }
    
    public Pair<ModernBetaSettingsPreset, Boolean> set(String stringChunk, String stringBiome, String stringCaveBiome) {
        ModernBetaSettingsChunk settingsChunk;
        ModernBetaSettingsBiome settingsBiome;
        ModernBetaSettingsCaveBiome settingsCaveBiome;
        
        boolean successful = true;
        
        try {
            settingsChunk = stringChunk != null && !stringChunk.isBlank() ?
                ModernBetaSettingsChunk.fromString(stringChunk) :
                this.settingsChunk;
            
            settingsBiome = stringBiome != null && !stringBiome.isBlank() ?
                ModernBetaSettingsBiome.fromString(stringBiome) :
                this.settingsBiome;
            
            settingsCaveBiome = stringCaveBiome != null && !stringCaveBiome.isBlank() ?
                ModernBetaSettingsCaveBiome.fromString(stringCaveBiome) :
                this.settingsCaveBiome;
            
        } catch (Exception e) {
            ModernBeta.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            successful = false;
            
            settingsChunk = this.settingsChunk;
            settingsBiome = this.settingsBiome;
            settingsCaveBiome = this.settingsCaveBiome;
        }
        
        return new Pair<>(new ModernBetaSettingsPreset(settingsChunk, settingsBiome, settingsCaveBiome), successful);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof ModernBetaSettingsPreset))
            return false;
        
        ModernBetaSettingsPreset other = (ModernBetaSettingsPreset)obj;
        
        return 
            this.settingsChunk.toCompound().equals(other.settingsChunk.toCompound()) &&
            this.settingsBiome.toCompound().equals(other.settingsBiome.toCompound()) &&
            this.settingsCaveBiome.toCompound().equals(other.settingsCaveBiome.toCompound());
    }
}
