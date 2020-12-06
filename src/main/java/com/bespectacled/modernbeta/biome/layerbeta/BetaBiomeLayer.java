package com.bespectacled.modernbeta.biome.layerbeta;

public abstract class BetaBiomeLayer {
    private long worldSeed;
    private long chunkSeed;
    private long baseSeed;
    
    protected BetaBiomeLayer parent;
    
    public static BetaBiomeLayer[] build(long seed) {
        
        return null;
    }
    
    public BetaBiomeLayer(long seed) {
        baseSeed = seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
        baseSeed *= baseSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        baseSeed += seed;
    }
    
    public void initWorldSeed(long seed)
    {
        worldSeed = seed;
        if(parent != null)
        {
            parent.initWorldSeed(seed);
        }
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
        worldSeed *= worldSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        worldSeed += baseSeed;
    }

    public void initChunkSeed(long l, long l1)
    {
        chunkSeed = worldSeed;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += l;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += l1;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += l;
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += l1;
    }

    protected int nextInt(int bound)
    {
        int nextInt = (int)((chunkSeed >> 24) % (long)bound);
        if(nextInt < 0) {
            nextInt += bound;
        }
        
        chunkSeed *= chunkSeed * 0x5851f42d4c957f2dL + 0x14057b7ef767814fL;
        chunkSeed += worldSeed;
        return nextInt;
    }

    public abstract int[] sample(int i, int j, int k, int l);
}
