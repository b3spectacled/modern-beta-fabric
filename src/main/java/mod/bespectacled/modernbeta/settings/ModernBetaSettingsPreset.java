package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class ModernBetaSettingsPreset {
    public static final ModernBetaSettingsPreset DEFAULT;
    
    private final String name;
    private final ModernBetaSettingsChunk settingsChunk;
    private final ModernBetaSettingsBiome settingsBiome;
    private final ModernBetaSettingsCaveBiome settingsCaveBiome;
    
    public ModernBetaSettingsPreset(
        String name,
        ModernBetaSettingsChunk settingsChunk,
        ModernBetaSettingsBiome settingsBiome,
        ModernBetaSettingsCaveBiome settingsCaveBiome
    ) {
        this.name = name;
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
    
    public Text getName() {
        return Text.translatable(this.name);
    }
    
    static {
        DEFAULT = new ModernBetaSettingsPreset(
            ModernBetaBuiltInTypes.DEFAULT_ID,
            new ModernBetaSettingsChunk(),
            new ModernBetaSettingsBiome(),
            new ModernBetaSettingsCaveBiome()
        );
    }
}
