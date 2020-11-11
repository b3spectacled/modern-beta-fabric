package com.bespectacled.modernbeta.colormap;

import net.fabricmc.api.Environment;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import java.io.IOException;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class WaterColormapResourceSupplier extends SinglePreparationResourceReloadListener<int[]> {
    private static final Identifier WATER_COLORMAP_LOC;
    
    @Override
    protected int[] prepare(ResourceManager resourceManager, Profiler profiler) {
        try {
            return RawTextureDataLoader.loadRawTextureData(resourceManager, WaterColormapResourceSupplier.WATER_COLORMAP_LOC);
        }
        catch (IOException iOException) {
            throw new IllegalStateException("Failed to load water color texture", iOException);
        }
    }
    
    @Override
    protected void apply(int[] arr, ResourceManager resourceManager, Profiler profiler) {
        WaterColors.setColorMap(arr);
    }
    
    static {
        WATER_COLORMAP_LOC = new Identifier(ModernBeta.ID, "textures/colormap/watercolor.png");
    }
    
    
}
