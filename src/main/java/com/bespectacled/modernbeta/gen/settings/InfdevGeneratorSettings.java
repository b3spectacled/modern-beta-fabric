package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class InfdevGeneratorSettings {
    public static final Codec<InfdevGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, InfdevGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public InfdevGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putBoolean("infdevWinterMode", ModernBeta.BETA_CONFIG.infdevWinterMode);
        settings.putBoolean("infdevPlus", ModernBeta.BETA_CONFIG.infdevPlus);
        settings.putBoolean("generateVanillaBiomesInfdev", ModernBeta.BETA_CONFIG.generateVanillaBiomesInfdev);
        
        return settings;
    }
}
