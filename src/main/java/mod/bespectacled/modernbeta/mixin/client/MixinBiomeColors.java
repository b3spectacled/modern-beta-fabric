package mod.bespectacled.modernbeta.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
@Mixin(BiomeColors.class)
public abstract class MixinBiomeColors {
    @Inject(method = "getWaterColor", at = @At("HEAD"), cancellable = true)
    private static void injectGetWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
        if (BlockColorSampler.INSTANCE.sampleWaterColor()) {
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(pos.getX(), pos.getZ());
            
            info.setReturnValue(BlockColorSampler.INSTANCE.colorMapWater.getColor(clime.temp(), clime.rain()));
        }
    }
}
