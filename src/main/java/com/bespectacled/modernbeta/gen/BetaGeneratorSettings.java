package com.bespectacled.modernbeta.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class BetaGeneratorSettings {
    public static final Codec<BetaGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped))
            .apply(instance, BetaGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;

    public BetaGeneratorSettings(ChunkGeneratorSettings wrapped) {
        this.wrapped = wrapped;

    }
}
