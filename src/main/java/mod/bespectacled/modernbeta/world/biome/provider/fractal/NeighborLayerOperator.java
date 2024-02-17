package mod.bespectacled.modernbeta.world.biome.provider.fractal;

@FunctionalInterface
public interface NeighborLayerOperator {
	BiomeInfo apply(BiomeInfo input, BiomeInfo... neighbors);
}
