package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class BetaGeneratorSettings {
    public static final Codec<BetaGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, BetaGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public BetaGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putBoolean("generateOceans", ModernBeta.BETA_CONFIG.generateOceans);
        settings.putBoolean("generateBetaOceans", ModernBeta.BETA_CONFIG.generateBetaOceans);
        settings.putBoolean("generateIceDesert", ModernBeta.BETA_CONFIG.generateIceDesert);
        
        return settings;
    }
}
