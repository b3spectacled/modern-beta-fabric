package mod.bespectacled.modernbeta.mixin;

import java.util.List;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;

@Mixin(ChunkGenerator.class)
public interface MixinChunkGenerator {
    @Accessor
    public Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> getIndexedFeaturesListSupplier();
}
