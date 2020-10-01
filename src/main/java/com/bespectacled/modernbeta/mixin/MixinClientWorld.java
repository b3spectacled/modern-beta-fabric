package com.bespectacled.modernbeta.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.world.ClientWorld.Properties;
import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ColorResolver;

import com.bespectacled.modernbeta.util.MathHelper;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;
import com.bespectacled.modernbeta.gen.BetaGeneratorType;
import com.bespectacled.modernbeta.noise.BetaNoiseGeneratorOctaves2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/*
 * Based on Colormatic ClientWorldMixin
 */
@Mixin(value = ClientWorld.class, priority = 1) 
public abstract class MixinClientWorld extends World {
	
	private static BetaNoiseGeneratorOctaves2 tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 9871L), 4);
    private static BetaNoiseGeneratorOctaves2 humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 39811L), 4);
    private static BetaNoiseGeneratorOctaves2 noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 543321L), 2);
	
	private static BetaNoiseGeneratorOctaves2 skyTempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(0 * 9871L), 4);
	
	private double skyTemps[];
	private double temps[];
	private double humids[];
	private double noises[];
	private Long seed;
	
	private static final boolean renderBetaSkyColor = ModernBetaConfig.loadConfig().render_beta_sky_color;
	
	// TempHumid cache
    public Map<BlockPos, Double> tempCache = new HashMap<>();
    public Map<BlockPos, Double> humidCache = new HashMap<>();
	
    public Map<BlockPos, double[]> tempHumidCache = new HashMap<>();
    
	@Shadow
	public Object2ObjectArrayMap<ColorResolver, BiomeColorCache> colorCache;
	
	@Shadow
	private int lightningTicksLeft;
	
	private MixinClientWorld() {
        super(null, null, null, null, false, false, 0L);
    }
	
	/*
	@Redirect(
	        method = "method_23777",
	        at = @At(
	            value = "INVOKE",
	            target = "Lnet/minecraft/world/biome/Biome;getSkyColor()I"
	        )
	    )
    private int injectBetaSkyColor(Biome self, BlockPos pos, float partialTicks) {
	    DimensionType type = this.getDimension();
	    
		if (this.seed == null || this.seed != BetaChunkGenerator.seed) {
			this.seed = BetaChunkGenerator.seed;
			initOctaves(this.seed);
			
		}
	
		if (renderBetaSkyColor && type.hasSkyLight()) {
		 
	        int x = pos.getX();
	        int z = pos.getZ();
	        
	        float temp = (float)getTemperature(x, z);
	        int skyColor = getSkyColorByTemp(temp);
	        
	        return skyColor;
		}
		
		return self.getSkyColor();
    }
	*/
	
	/* Yes I know this is bad, if there is a better way to achieve compatibility with Colormatic tell me! */
	public Vec3d method_23777(BlockPos blockPos, float float3) {
        float float4 = this.getSkyAngle(float3);
        float float5 = MathHelper.cos(float4 * 6.2831855f) * 2.0f + 0.5f;
        float5 = MathHelper.clamp(float5, 0.0f, 1.0f);     
        Biome biome6 = this.getBiome(blockPos);
        int integer7;
        
        if (renderBetaSkyColor && this.getDimension().hasSkyLight()) {
            integer7 = getBetaSkyColor(blockPos);
        } else {
            integer7 = biome6.getSkyColor(); // Captured by Colormatic so I can't redirect.
        }
            
        
        float float8 = (integer7 >> 16 & 0xFF) / 255.0f;
        float float9 = (integer7 >> 8 & 0xFF) / 255.0f;
        float float10 = (integer7 & 0xFF) / 255.0f;
        float8 *= float5;
        float9 *= float5;
        float10 *= float5;
        float float11 = this.getRainGradient(float3);
        if (float11 > 0.0f) {
            float float12 = (float8 * 0.3f + float9 * 0.59f + float10 * 0.11f) * 0.6f;
            float float13 = 1.0f - float11 * 0.75f;
            float8 = float8 * float13 + float12 * (1.0f - float13);
            float9 = float9 * float13 + float12 * (1.0f - float13);
            float10 = float10 * float13 + float12 * (1.0f - float13);
        }
        float float12 = this.getThunderGradient(float3);
        if (float12 > 0.0f) {
            float float13 = (float8 * 0.3f + float9 * 0.59f + float10 * 0.11f) * 0.2f;
            float float14 = 1.0f - float12 * 0.75f;
            float8 = float8 * float14 + float13 * (1.0f - float14);
            float9 = float9 * float14 + float13 * (1.0f - float14);
            float10 = float10 * float14 + float13 * (1.0f - float14);
        }
        if (this.lightningTicksLeft > 0) {
            float float13 = this.lightningTicksLeft - float3;
            if (float13 > 1.0f) {
                float13 = 1.0f;
            }
            float13 *= 0.45f;
            float8 = float8 * (1.0f - float13) + 0.8f * float13;
            float9 = float9 * (1.0f - float13) + 0.8f * float13;
            float10 = float10 * (1.0f - float13) + 1.0f * float13;
        }
        return new Vec3d(float8, float9, float10);
    }
	
	private int getBetaSkyColor(BlockPos pos) {
        
        if (this.seed == null || this.seed != BetaChunkGenerator.seed) {
            this.seed = BetaChunkGenerator.seed;
            initOctaves(this.seed);
            
        }
         
        int x = pos.getX();
        int z = pos.getZ();
        
        float temp = (float)getTemperature(x, z);
        int skyColor = getSkyColorByTemp(temp);
        
        return skyColor;
    }
	
	// Doesn't quite work, artifacts with random blocks...
	/*
	private int getBetaBiomeColor(BlockPos pos, ColorResolver colorResolver) {
		double[] tempHumids;
		int x = pos.getX();
		int z = pos.getZ();
		
		
		if (colorResolver.equals(BiomeColors.GRASS_COLOR)) {
		    tempHumids = tempHumidCache.get(pos);
		    
		    if (tempHumids == null) {
		        tempHumids = getTempHumid(x, z);
		        tempHumidCache.put(pos, tempHumids);
		    } 
		    
            return GrassColors.getColor(tempHumids[0], tempHumids[1]);
			
		} else if (colorResolver.equals(BiomeColors.FOLIAGE_COLOR)) {
		    tempHumids = tempHumidCache.get(pos);
            
            if (tempHumids == null) {
                tempHumids = getTempHumid(x, z);
                tempHumidCache.put(pos, tempHumids);
            }
            
			return FoliageColors.getColor(tempHumids[0], tempHumids[1]);
		} 
		
		return colorResolver.getColor(this.getBiome(pos), pos.getX(), pos.getZ());
	}

	
	@Override
    public int getColor(BlockPos pos, ColorResolver colorResolver) {
        BiomeColorCache biomeColorCache = (BiomeColorCache)this.colorCache.get(colorResolver);
        return biomeColorCache.getBiomeColor(pos, () -> getBetaBiomeColor(pos, colorResolver));
    }
	*/
	
	@Unique
	private void initOctaves(long seed) {
		tempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
	    humidNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 39811L), 4);
	    noiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 0x84a59L), 2);
		
		skyTempNoiseOctaves = new BetaNoiseGeneratorOctaves2(new Random(seed * 9871L), 4);
	}
	
	@Unique
	private int getSkyColorByTemp(float temp)
    {
		temp /= 3F;
		
        if(temp < -1F) {
        	temp = -1F;
        }
        
        if(temp > 1.0F) {
        	temp = 1.0F;
        }
        return java.awt.Color.getHSBColor(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F).getRGB();
    }
	
	@Unique
	private double getTemperature(int i, int j)
    {
		skyTemps = skyTempNoiseOctaves.func_4112_a(skyTemps, i, j, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
        return skyTemps[0];
    }
	
	@Unique
	private double[] getTempHumid(int x, int z) {
		temps = tempNoiseOctaves.func_4112_a(temps, x, z, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        humids = humidNoiseOctaves.func_4112_a(humids, x, z, 1, 1, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        noises = noiseOctaves.func_4112_a(noises, x, z, 1, 1, 0.25D, 0.25D, 0.58823529411764708D);
        
        double d = noises[0] * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;
        
        double temp = (temps[0] * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;
        
        d1 = 0.002D;
        d2 = 1.0D - d1;
        
        double humid = (humids[0] * 0.14999999999999999D + 0.5D) * d2 + d * d1;
        
        temp = 1.0D - (1.0D - temp) * (1.0D - temp);
        if(temp < 0.0D) {
            temp = 0.0D;
        }
        if(humid < 0.0D) {
            humid = 0.0D;
        }
        if(temp > 1.0D) {
            temp = 1.0D;
        }
        if(humid > 1.0D) {
            humid = 1.0D;
        }
        
        return new double[]{temp, humid};
	}
	

}
