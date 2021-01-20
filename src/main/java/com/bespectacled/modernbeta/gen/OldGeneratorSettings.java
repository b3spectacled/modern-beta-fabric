package com.bespectacled.modernbeta.gen;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.WorldEnum;
import com.bespectacled.modernbeta.util.WorldEnum.BiomeType;
import com.bespectacled.modernbeta.util.WorldEnum.WorldType;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class OldGeneratorSettings {
    public static final StructuresConfig structures = new StructuresConfig(true);
    public static final Optional<StrongholdConfig> guaranteedStronghold = Optional.of(new StrongholdConfig(0, 0, 1));
    public static final StructuresConfig structuresWithStronghold = new StructuresConfig(
        guaranteedStronghold, 
        Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES)
    );
    
    public static final NoiseSamplingConfig noiseSampler = new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0);
    public static final GenerationShapeConfig noise = GenerationShapeConfig.method_32994(
        0, 128, 
        noiseSampler,
        new SlideConfig(-10, 3, 0), 
        new SlideConfig(-30, 0, 0), 
        1, 2, 1.0, -0.46875, true, true, false, false
    );
    
    public static final Codec<OldGeneratorSettings> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(ChunkGeneratorSettings.CODEC.fieldOf("type").forGetter(settings -> settings.chunkGenSettings),
                    CompoundTag.CODEC.fieldOf("settings").forGetter(settings -> settings.providerSettings))
            .apply(instance, OldGeneratorSettings::new));

    public final ChunkGeneratorSettings chunkGenSettings;
    public CompoundTag providerSettings;

    public OldGeneratorSettings(ChunkGeneratorSettings chunkGenSettings, CompoundTag providerSettings) {
        this.chunkGenSettings = chunkGenSettings;
        this.providerSettings = providerSettings;
    }
    
    public OldGeneratorSettings(CompoundTag settings, boolean isIndev) { 
        this.chunkGenSettings = new ChunkGeneratorSettings(
            isIndev ? structuresWithStronghold : structures, 
            noise, 
            Blocks.STONE.getDefaultState(), 
            Blocks.WATER.getDefaultState(), 
            -10, 0, 64, false
        );
        
        this.providerSettings = settings;
    }
    
    public static CompoundTag createInfSettings(WorldType worldType, BiomeType biomeType, boolean genOceans) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", worldType.getName());
        settings.putString("biomeType", biomeType.getName());
        settings.putBoolean("generateOceans", genOceans);
        
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
