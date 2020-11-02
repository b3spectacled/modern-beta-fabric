package com.bespectacled.modernbeta.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.level.storage.AnvilLevelStorage;

@Mixin(AnvilLevelStorage.class)
public class MixinAnvilLevelStorage {
    @Unique
    private static DynamicRegistryManager.Impl registry;
    
    @Unique
    private static long storedSeed;
    
    /*
    @ModifyVariable(
        method = "convertLevel", 
        at = @At(
            value = "INVOKE_ASSIGN", 
            target = "Lnet/minecraft/world/biome/source/VanillaLayeredBiomeSource;<init>(JZZLnet/minecraft/util/registry/Registry;)V"
        )
    )
    private static BiomeSource injectBetaBiomeSource(BiomeSource original) {
        
        return null;
    }*/
    
    
    @ModifyVariable(
        method = "convertRegion",
        at = @At("HEAD")
    )
    private static BiomeSource injectConvertRegions(BiomeSource biomeSource) {
        if (biomeSource instanceof VanillaLayeredBiomeSource) {
            System.out.println("Found vanilla biome source");
            biomeSource = new BetaBiomeSource(0, DynamicRegistryManager.create().get(Registry.BIOME_KEY), BetaGeneratorSettings.createSettings());
        }
        
        return biomeSource;
    }
    

}
