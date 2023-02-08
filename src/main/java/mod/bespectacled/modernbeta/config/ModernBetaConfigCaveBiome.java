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
    public String biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
    public String singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
    
    public float voronoiHorizontalNoiseScale = 32.0f;
    public float voronoiVerticalNoiseScale = 16.0f;
    public int voronoiDepthMinY = -64;
    public int voronoiDepthMaxY = 64;
    public List<ConfigVoronoiPoint> voronoiPoints = List.of(
        new ConfigVoronoiPoint("", 0.0, 0.5, 0.75),
        new ConfigVoronoiPoint("minecraft:lush_caves", 0.1, 0.5, 0.75),
        new ConfigVoronoiPoint("", 0.5, 0.5, 0.75),
        new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
        new ConfigVoronoiPoint("", 1.0, 0.5, 0.75),

        new ConfigVoronoiPoint("", 0.0, 0.5, 0.25),
        new ConfigVoronoiPoint("minecraft:lush_caves", 0.2, 0.5, 0.25),
        new ConfigVoronoiPoint("", 0.4, 0.5, 0.25),
        new ConfigVoronoiPoint("minecraft:deep_dark", 0.5, 0.5, 0.25),
        new ConfigVoronoiPoint("", 0.6, 0.5, 0.25),
        new ConfigVoronoiPoint("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
        new ConfigVoronoiPoint("", 1.0, 0.5, 0.25)
    );
    
    public static class ConfigVoronoiPoint {
        public static final ConfigVoronoiPoint DEFAULT = new ConfigVoronoiPoint("minecraft:lush_caves", 0.5, 0.5, 0.0);
        
        public String biome;
        public double temp;
        public double rain;
        public double depth;
        
        public ConfigVoronoiPoint(String biome, double temp, double rain, double depth) {
            this.biome = biome;
            this.temp = temp;
            this.rain = rain;
            this.depth = depth;
        }
        
        public NbtCompound toCompound() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, this.biome)
                .putDouble(NbtTags.TEMP, this.temp)
                .putDouble(NbtTags.RAIN, this.rain)
                .putDouble(NbtTags.DEPTH, this.depth)
                .build();
        }
    }
}
