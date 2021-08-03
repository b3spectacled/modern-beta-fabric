package com.bespectacled.modernbeta.util.mersenne;

import java.util.Random;

@SuppressWarnings("serial")
public class MTRandom extends Random {
    private final MersenneTwister mt;
    
    public MTRandom(long seed) {
        this.mt = new MersenneTwister((int)seed);
    }
    
    @Override
    protected synchronized int next(int bits) {
        return this.mt.genRandInt32() >>> (32 - bits);
    }
    
    /*
     * Used by noise generators
     */
    @Override
    public synchronized double nextDouble() {
        return this.mt.genRandDouble();
    }

    /*
     * Used when populating noise generator permutation array
     * See: __aeabi_uidivmod(unsigned numerator, unsigned denominator) for PE equivalent
     */
    @Override
    public synchronized int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");
        
        return (int) (Integer.toUnsignedLong(this.mt.genRandInt32()) % bound);
    }
}   

