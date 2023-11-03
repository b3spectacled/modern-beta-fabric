package mod.bespectacled.modernbeta.client.resource;

import java.io.IOException;
import java.util.function.Consumer;

import mod.bespectacled.modernbeta.ModernBeta;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class ModernBetaColormapResource implements SimpleSynchronousResourceReloadListener {
    private final Identifier id;
    private final Consumer<int[]> consumer;
    
    public ModernBetaColormapResource(String path, Consumer<int[]> consumer) {
        this.id = ModernBeta.createId(path);
        this.consumer = consumer;
    }

    @Override
    public Identifier getFabricId() {
        return this.id;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void reload(ResourceManager resourceManager) {
        int[] map;
        
        try {
            map = RawTextureDataLoader.loadRawTextureData(resourceManager, this.id);
        } catch (IOException exception) {
            throw new IllegalStateException("[Modern Beta] Failed to load colormap texture!", exception);
        }
        
        this.consumer.accept(map);
    }
}
