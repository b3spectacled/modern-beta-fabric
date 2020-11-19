package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class OldGeneratorSettings {
    public static final Codec<OldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, OldGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public OldGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createBetaSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldEnum.WorldType.BETA.getName());
        settings.putString("biomeType", ModernBeta.BETA_CONFIG.biomeType);
        settings.putBoolean("generateBetaOceans", ModernBeta.BETA_CONFIG.generateBetaOceans);
        
        return settings;
    }
    
    public static CompoundTag createAlphaSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldEnum.WorldType.ALPHA.getName());
        settings.putString("biomeType", ModernBeta.BETA_CONFIG.biomeType);
        
        return settings;
    }
    
    public static CompoundTag createInfdevSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldEnum.WorldType.INFDEV.getName());
        settings.putString("biomeType", ModernBeta.BETA_CONFIG.biomeType);
        
        return settings;
    }
    
    public static CompoundTag createInfdevOldSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldEnum.WorldType.INFDEV_OLD.getName());
        settings.putString("biomeType", ModernBeta.BETA_CONFIG.biomeType);
        settings.putBoolean("generateInfdevPyramid", ModernBeta.BETA_CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", ModernBeta.BETA_CONFIG.generateInfdevWall);
        
        return settings;
    }
    
    public static CompoundTag createIndevSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", WorldEnum.WorldType.INDEV.getName());
        settings.putString("levelType", ModernBeta.BETA_CONFIG.indevLevelType);
        settings.putString("levelTheme", ModernBeta.BETA_CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", ModernBeta.BETA_CONFIG.indevLevelWidth);
        settings.putInt("levelLength", ModernBeta.BETA_CONFIG.indevLevelLength);
        settings.putInt("levelHeight", ModernBeta.BETA_CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", ModernBeta.BETA_CONFIG.indevCaveRadius);
        
        return settings;
    }
}
