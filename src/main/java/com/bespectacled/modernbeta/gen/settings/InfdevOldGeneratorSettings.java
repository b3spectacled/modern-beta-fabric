package com.bespectacled.modernbeta.gen.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class InfdevOldGeneratorSettings {
    public static final Codec<InfdevOldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.wrapped),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.settings))
            .apply(instance, InfdevOldGeneratorSettings::new));

    public final ChunkGeneratorSettings wrapped;
    public CompoundTag settings;

    public InfdevOldGeneratorSettings(ChunkGeneratorSettings wrapped, CompoundTag settings) {
        this.wrapped = wrapped;
        this.settings = settings;
    }
    
    public static CompoundTag createSettings() {
        CompoundTag settings = new CompoundTag();
        
        settings.putBoolean("infdevOldWinterMode", ModernBeta.BETA_CONFIG.infdevOldWinterMode);
        settings.putBoolean("infdevOldPlus", ModernBeta.BETA_CONFIG.infdevOldPlus);
        settings.putBoolean("generateVanillaBiomesInfdevOld", ModernBeta.BETA_CONFIG.generateVanillaBiomesInfdevOld);
        settings.putBoolean("generateInfdevPyramid", ModernBeta.BETA_CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", ModernBeta.BETA_CONFIG.generateInfdevWall);
        
        return settings;
    }
}
