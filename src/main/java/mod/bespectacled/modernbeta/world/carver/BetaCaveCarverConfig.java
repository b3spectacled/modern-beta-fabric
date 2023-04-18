package mod.bespectacled.modernbeta.world.carver;

import java.util.Optional;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.CaveCarverConfig;
import net.minecraft.world.gen.heightprovider.HeightProvider;

public class BetaCaveCarverConfig extends CaveCarverConfig {
    public static final Codec<BetaCaveCarverConfig> CAVE_CODEC = RecordCodecBuilder.create(instance -> 
        fillConfigFields(instance).and(instance.group(
            Codec.BOOL.optionalFieldOf("use_fixed_caves").forGetter(config -> config.useFixedCaves),
            Codec.BOOL.optionalFieldOf("use_aquifers").forGetter(config -> config.useAquifers))
        ).apply(instance, BetaCaveCarverConfig::new));
    
    public final Optional<Boolean> useFixedCaves;
    public final Optional<Boolean> useAquifers;
    
    public BetaCaveCarverConfig(
        float probability,
        HeightProvider y,
        FloatProvider yScale,
        YOffset lavaLevel,
        CarverDebugConfig debugConfig,
        RegistryEntryList<Block> replaceable,
        FloatProvider horizontalRadiusMultiplier,
        FloatProvider verticalRadiusMultiplier,
        FloatProvider floorLevel,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers
    ) {
        super(
            probability,
            y,
            yScale,
            lavaLevel,
            debugConfig,
            replaceable,
            horizontalRadiusMultiplier,
            verticalRadiusMultiplier,
            floorLevel
        );
        
        this.useFixedCaves = useFixedCaves;
        this.useAquifers = useAquifers;
    }
    
    public BetaCaveCarverConfig(
        CarverConfig config,
        FloatProvider horizontalRadiusMultiplier,
        FloatProvider verticalRadiusMultiplier,
        FloatProvider floorLevel,
        Optional<Boolean> useFixedCaves,
        Optional<Boolean> useAquifers
    ) {
        this(
            config.probability,
            config.y,
            config.yScale,
            config.lavaLevel,
            config.debugConfig,
            config.replaceable,
            horizontalRadiusMultiplier,
            verticalRadiusMultiplier,
            floorLevel,
            useFixedCaves,
            useAquifers
        );
    }

    private static <P extends CaveCarverConfig> Products.P4<RecordCodecBuilder.Mu<P>, CarverConfig, FloatProvider, FloatProvider, FloatProvider> fillConfigFields(RecordCodecBuilder.Instance<P> instance) {
        return instance.group(
            CarverConfig.CONFIG_CODEC.forGetter(config -> config),
            FloatProvider.VALUE_CODEC.fieldOf("horizontal_radius_multiplier").forGetter(config -> config.horizontalRadiusMultiplier),
            FloatProvider.VALUE_CODEC.fieldOf("vertical_radius_multiplier").forGetter(config -> config.verticalRadiusMultiplier),
            FloatProvider.createValidatedCodec(-1.0f, 1.0f).fieldOf("floor_level").forGetter(config -> config.floorLevel)
        );
    }
}
