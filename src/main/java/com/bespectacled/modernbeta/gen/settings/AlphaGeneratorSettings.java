package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class AlphaGeneratorSettings {
    public static final Codec<AlphaGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, AlphaGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public AlphaGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putBoolean("alphaWinterMode", ModernBeta.BETA_CONFIG.alphaWinterMode);
        settings.putBoolean("alphaPlus", ModernBeta.BETA_CONFIG.alphaPlus);
        settings.putBoolean("generateVanillaBiomesAlpha", ModernBeta.BETA_CONFIG.generateVanillaBiomesAlpha);
        
        return settings;
    }
}
