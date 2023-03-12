package mod.bespectacled.modernbeta.world.gen.sampler;

import mod.bespectacled.modernbeta.util.noise.NoiseRules;

public class OreVeinRules {
    private static final double COPPER_MIN = -1.0;
    private static final double COPPER_MAX = 0.0;
    
    private static final double IRON_MIN = 0.0;
    private static final double IRON_MAX = 1.0;
    
    public static final NoiseRules<OreVeinType> DEFAULT_VEIN_RULES;
    public static final NoiseRules<OreVeinType> BETA_VEIN_RULES;
    public static final NoiseRules<OreVeinType> SKYLANDS_VEIN_RULES;
    public static final NoiseRules<OreVeinType> OLD_VEIN_RULES;
    
    static {
        DEFAULT_VEIN_RULES = new NoiseRules.Builder<OreVeinType>()
            .add(COPPER_MIN, COPPER_MAX, OreVeinType.COPPER_UPPER)
            .add(IRON_MIN, IRON_MAX, OreVeinType.IRON_LOWER)
            .build();
        
        BETA_VEIN_RULES = new NoiseRules.Builder<OreVeinType>()
            .add(COPPER_MIN, COPPER_MAX, OreVeinType.COPPER_UPPER)
            .add(IRON_MIN, IRON_MAX, OreVeinType.IRON_LOWER)
            .build();
        
        SKYLANDS_VEIN_RULES = new NoiseRules.Builder<OreVeinType>()
            .add(COPPER_MIN, COPPER_MAX, OreVeinType.COPPER_UPPER)
            .add(IRON_MIN, IRON_MAX, OreVeinType.IRON_UPPER)
            .build();
        
        OLD_VEIN_RULES = new NoiseRules.Builder<OreVeinType>()
            .add(COPPER_MIN, COPPER_MAX, OreVeinType.COPPER_UPPER)
            .add(IRON_MIN, IRON_MAX, OreVeinType.IRON_LOWER)
            .build();
    }
}
