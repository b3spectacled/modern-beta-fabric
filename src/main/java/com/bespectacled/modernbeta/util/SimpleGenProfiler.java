package com.bespectacled.modernbeta.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.google.common.util.concurrent.AtomicDouble;

import net.minecraft.world.chunk.ChunkStatus;

public class SimpleGenProfiler {
    private final int totalIterations;
    private final Map<ChunkStatus, AtomicInteger> steps;
    private final Map<ChunkStatus, AtomicDouble> sum;
    
    public SimpleGenProfiler(int totalIterations) {
        this.totalIterations = totalIterations;
        this.steps = new HashMap<>();
        this.sum = new HashMap<>();
    }
    
    public void addStep(ChunkStatus chunkStatus) {
        this.steps.put(chunkStatus, new AtomicInteger());
        this.sum.put(chunkStatus, new AtomicDouble());
    }
    
    public GenProfilerStep createStep(ChunkStatus chunkStatus) {
        int iterations = this.iterate(chunkStatus);
        
        if (iterations > this.totalIterations) {
            return new GenProfilerStep(chunkStatus, this.sum.get(chunkStatus)) {
                @Override
                public void start() {}
                
                @Override
                public void stop() {}
            };
        }
        
        return new GenProfilerStep(chunkStatus, this.sum.get(chunkStatus));
    }
    
    private int iterate(ChunkStatus chunkStatus) {
        return this.steps.get(chunkStatus).incrementAndGet();
    }
    
    public static class GenProfilerStep {
        private long timeNano;
        private long elapsedTimeNano;
        
        private final ChunkStatus chunkStatus;
        private final AtomicDouble sum;
        
        public GenProfilerStep(ChunkStatus chunkStatus, AtomicDouble sum) {
            this.timeNano = 0;
            this.elapsedTimeNano = 0;
            
            this.chunkStatus = chunkStatus;
            this.sum = sum;
        }
        
        public void start() {
            this.timeNano = System.nanoTime();
        }
        
        public void stop() {
            this.elapsedTimeNano = System.nanoTime() - this.timeNano;
            
            double elapsedTimeSeconds = (double)this.elapsedTimeNano / 1000000000D;
            sum.addAndGet(elapsedTimeSeconds);

            ModernBeta.log(Level.INFO, this.chunkStatus.getId() + ", time elapsed: " + elapsedTimeSeconds + ", sum: " + sum.get());
        }
    }
}
