package com.bespectacled.modernbeta.mixin.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
public interface MixinGeneratorTypeAccessor {
    @Accessor("VALUES")
    public static List<GeneratorType> getValues() {
      throw new AssertionError();
    }
    
    @Accessor("SCREEN_PROVIDERS")
    public static Map<Optional<GeneratorType>, ScreenProvider> getScreenProviders() {
        throw new AssertionError();
    }
    
    @Accessor("SCREEN_PROVIDERS")
    public static void setScreenProviders(Map<Optional<GeneratorType>, ScreenProvider> providers) {
        throw new AssertionError();
    }
    
}
