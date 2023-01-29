package mod.bespectacled.modernbeta.config;

import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtTags;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "config_cave_biome")
public class ModernBetaConfigCaveBiome implements ConfigData {
    public String biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.name;
    public String singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
    
    public float voronoiHorizontalNoiseScale = 32.0f;
    public float voronoiVerticalNoiseScale = 16.0f;
    public List<VoronoiPointCaveBiome> voronoiPoints = List.of(
        new VoronoiPointCaveBiome("minecraft:the_void", 0.0, 0.5, true),
        new VoronoiPointCaveBiome("minecraft:lush_caves", 0.1, 0.5, false),
        new VoronoiPointCaveBiome("minecraft:the_void", 0.5, 0.5, true),
        new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.9, 0.5, false),
        new VoronoiPointCaveBiome("minecraft:the_void", 1.0, 0.5, true)
    );
    
    public static class VoronoiPointCaveBiome {
        public static final VoronoiPointCaveBiome DEFAULT = new VoronoiPointCaveBiome("minecraft:lush_caves", 0.5, 0.5, false);
        
        public String biome;
        public double temp;
        public double rain;
        public boolean nullBiome;
        
        public VoronoiPointCaveBiome(String biome, double temp, double rain, boolean nullBiome) {
            this.biome = biome;
            this.temp = temp;
            this.rain = rain;
            this.nullBiome = nullBiome;
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putDouble(NbtTags.TEMP, this.temp)
                .putDouble(NbtTags.RAIN, this.rain)
                .putBoolean(NbtTags.NULL_BIOME, this.nullBiome)
                .build();
        }
    }
}
