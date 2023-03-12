package mod.bespectacled.modernbeta.util.mersenne;

import java.util.Random;

@SuppressWarnings("serial")
public class MTRandom extends Random {
    private static final double MAX_UINT_32 = 4294967296D;
    
    private final MersenneTwister mt;
    
    public MTRandom() {
        this.mt = new MersenneTwister();
    }
    
    public MTRandom(long seed) {
        this.mt = new MersenneTwister((int)seed);
    }
    
    @Override
    protected synchronized int next(int bits) {
        return this.mt.genRandInt32() >>> (32 - bits);
    }
    
    /*
     * Used by noise generators
     * Get double value by dividing by max unsigned int32 value.
     */
    @Override
    public synchronized double nextDouble() {
        return Integer.toUnsignedLong(this.mt.genRandInt32()) / MAX_UINT_32;
    }
    
    /*
     * Used by surface builder
     * Get float value by dividing by max unsigned int32 value.
     */
    @Override
    public synchronized float nextFloat() {
        return (float)(Integer.toUnsignedLong(this.mt.genRandInt32()) / MAX_UINT_32);
    }

    /*
     * Used when populating noise generator permutation array
     * See: __aeabi_uidivmod(unsigned numerator, unsigned denominator) for Arm equivalent
     */
    @Override
    public synchronized int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        
        return (int) (Integer.toUnsignedLong(this.mt.genRandInt32()) % bound);
    }
}   

