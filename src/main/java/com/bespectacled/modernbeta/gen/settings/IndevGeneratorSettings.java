package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class IndevGeneratorSettings {
    public static final Codec<IndevGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, IndevGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public IndevGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putInt("levelType", ModernBeta.BETA_CONFIG.indevLevelType);
        settings.putInt("levelTheme", ModernBeta.BETA_CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", ModernBeta.BETA_CONFIG.indevLevelWidth);
        settings.putInt("levelLength", ModernBeta.BETA_CONFIG.indevLevelLength);
        settings.putInt("levelHeight", ModernBeta.BETA_CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", ModernBeta.BETA_CONFIG.indevCaveRadius);
        
        return settings;
    }
}
