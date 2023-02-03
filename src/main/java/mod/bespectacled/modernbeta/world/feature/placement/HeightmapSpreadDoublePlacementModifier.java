package mod.bespectacled.modernbeta.world.feature.placement;

import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.FeaturePlacementContext;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class HeightmapSpreadDoublePlacementModifier extends PlacementModifier {
    public static final Codec<HeightmapSpreadDoublePlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Heightmap.Type.CODEC.fieldOf("heightmap").forGetter(arg -> arg.heightmap)
        ).apply(instance, HeightmapSpreadDoublePlacementModifier::of));
    
    private final Heightmap.Type heightmap;
    
    private HeightmapSpreadDoublePlacementModifier(Heightmap.Type heightmap) {
        this.heightmap = heightmap;
    }
    
    public static HeightmapSpreadDoublePlacementModifier of(Heightmap.Type heightmap) {
        return new HeightmapSpreadDoublePlacementModifier(heightmap);
    }
    
    @Override
    public Stream<BlockPos> getPositions(FeaturePlacementContext context, Random random, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        
        int y = context.getTopY(this.heightmap, x, z);
        if (y == context.getBottomY()) {
            return Stream.of(new BlockPos[0]);
        }
        
        return Stream.of(new BlockPos(x, context.getBottomY() + random.nextInt((y - context.getBottomY()) * 2), z));
    }

    @Override
    public PlacementModifierType<?> getType() {
        return ModernBetaPlacementTypes.HEIGHTMAP_SPREAD_DOUBLE;
    }

}
