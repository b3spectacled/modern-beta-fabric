package mod.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.SimpleNoiseRouter;

@Mixin(DensityFunctions.class)
public interface MixinDensityFunctionsAccessor {
    @Invoker("method_41103")
    public static SimpleNoiseRouter invokeMethod_41103(GenerationShapeConfig shapeConfig, boolean amplified) {
        throw new AssertionError();
    }
}
