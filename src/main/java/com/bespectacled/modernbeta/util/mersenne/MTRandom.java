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
    
    @Override
    public synchronized double nextDouble() {
        return this.mt.genRandDouble();
    }
}   
