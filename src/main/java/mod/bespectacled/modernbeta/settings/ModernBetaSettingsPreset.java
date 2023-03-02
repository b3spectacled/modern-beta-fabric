package mod.bespectacled.modernbeta.settings;

import org.slf4j.event.Level;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.nbt.NbtCompound;

public class ModernBetaSettingsPreset {
    public static final ModernBetaSettingsPreset DEFAULT;
    
    private final ModernBetaSettingsChunk settingsChunk;
    private final ModernBetaSettingsBiome settingsBiome;
    private final ModernBetaSettingsCaveBiome settingsCaveBiome;
    
    public ModernBetaSettingsPreset(
        NbtCompound newSettingsChunk,
        NbtCompound newSettingsBiome,
        NbtCompound newSettingsCaveBiome
    ) {
        this(
            new ModernBetaSettingsChunk.Builder(newSettingsChunk).build(),
            new ModernBetaSettingsBiome.Builder(newSettingsBiome).build(),
            new ModernBetaSettingsCaveBiome.Builder(newSettingsCaveBiome).build()
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
    
    public NbtCompound getNbtChunk() {
        return this.settingsChunk.toCompound();
    }
    
    public NbtCompound getNbtBiome() {
        return this.settingsBiome.toCompound();
    }
    
    public NbtCompound getNbtCaveBiome() {
        return this.settingsCaveBiome.toCompound();
    }
    
    public ModernBetaSettingsPreset set(String stringChunk, String stringBiome, String stringCaveBiome) {
        ModernBetaSettingsChunk settingsChunk;
        ModernBetaSettingsBiome settingsBiome;
        ModernBetaSettingsCaveBiome settingsCaveBiome;
        
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
            ModernBeta.log(Level.ERROR, "Unable to read settings JSON!");
            
            settingsChunk = this.settingsChunk;
            settingsBiome = this.settingsBiome;
            settingsCaveBiome = this.settingsCaveBiome;
        }
        
        return new ModernBetaSettingsPreset(settingsChunk, settingsBiome, settingsCaveBiome);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof ModernBetaSettingsPreset))
            return false;
        
        ModernBetaSettingsPreset other = (ModernBetaSettingsPreset)obj;
        
        return 
            this.getNbtChunk().equals(other.getNbtChunk()) &&
            this.getNbtBiome().equals(other.getNbtBiome()) &&
            this.getNbtCaveBiome().equals(other.getNbtCaveBiome());
    }
    
    static {
        DEFAULT = new ModernBetaSettingsPreset(
            new ModernBetaSettingsChunk(),
            new ModernBetaSettingsBiome(),
            new ModernBetaSettingsCaveBiome()
        );
    }
}
