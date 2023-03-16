package mod.bespectacled.modernbeta.settings;

import java.util.List;

import com.google.gson.Gson;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.BiomeKeys;

public class ModernBetaSettingsCaveBiome implements ModernBetaSettings {
    public final String biomeProvider;
    public final String singleBiome;
    
    public final float voronoiHorizontalNoiseScale;
    public final float voronoiVerticalNoiseScale;
    public final int voronoiDepthMinY;
    public final int voronoiDepthMaxY;
    public final List<VoronoiPointCaveBiome> voronoiPoints;
    
    public ModernBetaSettingsCaveBiome() {
        this(new Builder());
    }
    
    public ModernBetaSettingsCaveBiome(ModernBetaSettingsCaveBiome.Builder builder) {
        this.biomeProvider = builder.biomeProvider;
        this.singleBiome = builder.singleBiome;
        
        this.voronoiHorizontalNoiseScale = builder.voronoiHorizontalNoiseScale;
        this.voronoiVerticalNoiseScale = builder.voronoiVerticalNoiseScale;
        this.voronoiDepthMinY = builder.voronoiDepthMinY;
        this.voronoiDepthMaxY = builder.voronoiDepthMaxY;
        
        this.voronoiPoints = builder.voronoiPoints;
    }
    
    public static ModernBetaSettingsCaveBiome fromString(String string) {
        Gson gson = new Gson();
        
        return gson.fromJson(string, ModernBetaSettingsCaveBiome.class);
    }
    
    public static ModernBetaSettingsCaveBiome fromCompound(NbtCompound compound) {
        return new Builder().fromCompound(compound).build();
    }
    
    public NbtCompound toCompound() {
        return new NbtCompoundBuilder()
            .putString(NbtTags.BIOME_PROVIDER, this.biomeProvider)
            .putString(NbtTags.SINGLE_BIOME, this.singleBiome)
            
            .putFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, this.voronoiHorizontalNoiseScale)
            .putFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, this.voronoiVerticalNoiseScale)
            .putInt(NbtTags.VORONOI_DEPTH_MIN_Y, this.voronoiDepthMinY)
            .putInt(NbtTags.VORONOI_DEPTH_MAX_Y, this.voronoiDepthMaxY)
            .putList(NbtTags.VORONOI_POINTS, VoronoiPointCaveBiome.listToNbt(this.voronoiPoints))
            
            .build();
    }
    
    public static class Builder {
        public String biomeProvider;
        public String singleBiome;
        
        public float voronoiHorizontalNoiseScale;
        public float voronoiVerticalNoiseScale;
        public int voronoiDepthMinY;
        public int voronoiDepthMaxY;
        public List<VoronoiPointCaveBiome> voronoiPoints;
        
        public Builder() {
            String biomeLushCaves = BiomeKeys.LUSH_CAVES.getValue().toString();
            String biomeDripstoneCaves = BiomeKeys.DRIPSTONE_CAVES.getValue().toString();
            String biomeDeepDark = BiomeKeys.DEEP_DARK.getValue().toString();
            
            this.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
            this.singleBiome = BiomeKeys.LUSH_CAVES.getValue().toString();
            
            this.voronoiHorizontalNoiseScale = 32.0f;
            this.voronoiVerticalNoiseScale = 16.0f;
            this.voronoiDepthMinY = -64;
            this.voronoiDepthMaxY = 64;
            this.voronoiPoints = List.of(
                new VoronoiPointCaveBiome("", 0.0, 0.5, 0.75),
                new VoronoiPointCaveBiome(biomeLushCaves, 0.1, 0.5, 0.75),
                new VoronoiPointCaveBiome("", 0.5, 0.5, 0.75),
                new VoronoiPointCaveBiome(biomeDripstoneCaves, 0.9, 0.5, 0.75),
                new VoronoiPointCaveBiome("", 1.0, 0.5, 0.75),

                new VoronoiPointCaveBiome("", 0.0, 0.5, 0.25),
                new VoronoiPointCaveBiome(biomeLushCaves, 0.2, 0.5, 0.25),
                new VoronoiPointCaveBiome("", 0.4, 0.5, 0.25),
                new VoronoiPointCaveBiome(biomeDeepDark, 0.5, 0.5, 0.25),
                new VoronoiPointCaveBiome("", 0.6, 0.5, 0.25),
                new VoronoiPointCaveBiome(biomeDripstoneCaves, 0.8, 0.5, 0.25),
                new VoronoiPointCaveBiome("", 1.0, 0.5, 0.25)
            );
        }
        
        public Builder fromCompound(NbtCompound compound) {
            NbtReader reader = new NbtReader(compound);
            
            this.biomeProvider = reader.readString(NbtTags.BIOME_PROVIDER, this.biomeProvider);
            this.singleBiome = reader.readString(NbtTags.SINGLE_BIOME, this.singleBiome);
            
            this.voronoiHorizontalNoiseScale = reader.readFloat(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, this.voronoiHorizontalNoiseScale);
            this.voronoiVerticalNoiseScale = reader.readFloat(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, this.voronoiVerticalNoiseScale);
            this.voronoiDepthMinY = reader.readInt(NbtTags.VORONOI_DEPTH_MIN_Y, this.voronoiDepthMinY);
            this.voronoiDepthMaxY = reader.readInt(NbtTags.VORONOI_DEPTH_MAX_Y, this.voronoiDepthMaxY);
            this.voronoiPoints = VoronoiPointCaveBiome.listFromReader(reader, this.voronoiPoints);
            
            this.loadDatafix(reader);
            
            return this;
        }
        
        public ModernBetaSettingsCaveBiome build() {
            return new ModernBetaSettingsCaveBiome(this);
        }
        
        private void loadDatafix(NbtReader reader) {}
    }
}