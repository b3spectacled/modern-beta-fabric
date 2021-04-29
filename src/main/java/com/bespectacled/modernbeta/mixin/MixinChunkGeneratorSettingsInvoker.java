package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Mixin(ChunkGeneratorSettings.class)
public interface MixinChunkGeneratorSettingsInvoker {
    @Invoker("hasAquifers")
    public boolean invokeHasAquifers();
    
    @Invoker("hasNoiseCaves")
    public boolean invokeHasNoiseCaves();
    
    @Invoker("hasDeepslate")
    public boolean invokeHasDeepslate();
    
    @Invoker("method_36396")
    public boolean invokeHasOreVeins();
    
    @Invoker("method_36468")
    public boolean invokeHasNoodleCaves();
}
