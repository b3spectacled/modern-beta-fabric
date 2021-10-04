package com.bespectacled.modernbeta.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.class_6622;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(class_6622.class_6623.class)
public class Mixin_class_6623 {
    @Shadow private ChunkPos comp_127;
    @Shadow private ChunkGenerator comp_125;
    @Shadow private Predicate<Biome> comp_128;
    @Shadow private HeightLimitView comp_129;
    
    /*
     * Inject custom biome location to properly generate ocean structures
     */
    @Inject(method = "method_38707", at = @At("HEAD"), cancellable = true)
    private void injectMethod_38707(Heightmap.Type type, CallbackInfoReturnable<Boolean> info) {
        if (this.comp_125 instanceof OldChunkGenerator oldChunkGenerator) {
            int x = this.comp_127.getCenterX();
            int z = this.comp_127.getCenterZ();
            int y = this.comp_125.getHeightInGround(x, z, type, this.comp_129);
            
            Biome biome = oldChunkGenerator.getInjectedBiomeAtBlock(x, y, z);
            
            info.setReturnValue(this.comp_128.test(biome));
        }
    }
    
}
