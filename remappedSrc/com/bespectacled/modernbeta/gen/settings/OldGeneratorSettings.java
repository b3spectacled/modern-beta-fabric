package com.bespectacled.modernbeta.gen.settings;

import java.util.Optional;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.util.WorldEnum;
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
    
    public static final NoiseSamplingConfig noiseSampler = new NoiseSamplingConfig(1.0, 1.0, 40.0, 22.0);
    public static final GenerationShapeConfig noise = new GenerationShapeConfig(
        128, 
        noiseSampler,
        new SlideConfig(-10, 3, 0), 
        new SlideConfig(-30, 0, 0), 
        1, 2, 1.0, -60.0 / (256.0 / 2.0), true, true, false, false
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
    
    public static CompoundTag createInfSettings(String worldType, String biomeType, boolean genOceans) {
        CompoundTag settings = new CompoundTag();
        
        settings.putString("worldType", worldType);
        settings.putString("biomeType", biomeType);
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
