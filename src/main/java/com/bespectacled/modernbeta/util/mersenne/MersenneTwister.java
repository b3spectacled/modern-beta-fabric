package com.bespectacled.modernbeta.util.mersenne;

/**
 * Implementation of Mersenne Twister MT19937.
 * 
 * Based on:
 * - http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/VERSIONS/C-LANG/980409/mt19937int.c
 * - http://www.math.sci.hiroshima-u.ac.jp/m-mat/MT/MT2002/CODES/mt19937ar.c
 * - http://www.java2s.com/Code/Java/Development-Class/AJavaimplementationoftheMT19937MersenneTwisterpseudorandomnumbergeneratoralgorithm.htm
 *
 */
public class MersenneTwister {
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = 0x9908b0df;
    private static final int UPPER_MASK = 0x80000000;
    private static final int LOWER_MASK = 0x7fffffff;
    private static final int DEFAULT_SEED = 4357;
    private static final int[] MAG_01 = { 0x0, MATRIX_A };
    
    private final int mt[];
    private int mti;
    
    public MersenneTwister() {
        this(DEFAULT_SEED);
    }
    
    public MersenneTwister(int seed) {
        this.mt = new int[N];
        this.mti = N + 1;
        
        this.init(seed);
    }
    
    public synchronized int genRandInt32() {
        int y;
        
        if (this.mti >= N) {
            int kk;
            
            // If init is uncalled (never happens)
            if (this.mti == N + 1)
                this.init(DEFAULT_SEED);
            
            for (kk = 0; kk < N - M; ++kk) {
                y = (this.mt[kk] & UPPER_MASK) | (this.mt[kk + 1] & LOWER_MASK);
                this.mt[kk] = this.mt[kk + M] ^ (y >>> 1) ^ MAG_01[y & 0x1];
            }
            
            for (; kk < N - 1; ++kk) {
                y = (this.mt[kk] & UPPER_MASK) | (this.mt[kk + 1] & LOWER_MASK);
                this.mt[kk] = this.mt[kk + (M - N)] ^ (y >>> 1) ^ MAG_01[y & 0x1];
            }
            
            y = (this.mt[N - 1] & UPPER_MASK) | (this.mt[0] & LOWER_MASK);
            this.mt[N - 1] = this.mt[M - 1] ^ (y >>> 1) ^ MAG_01[y & 0x1];
            
            this.mti = 0;
        }
        
        y = mt[mti++];
        
        // Tempering
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >>> 18);
        
        return y;
    }
    
    private synchronized void init(int seed) {
        this.mt[0] = seed & 0xFFFFFFFF;
        for (this.mti = 1; this.mti < N; ++this.mti) {
            this.mt[this.mti] = (1812433253 * (this.mt[this.mti-1] ^ (this.mt[this.mti-1] >>> 30)) + this.mti);
            this.mt[this.mti] &= 0xFFFFFFFF;
        }
    }
}
