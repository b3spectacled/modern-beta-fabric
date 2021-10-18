package com.bespectacled.modernbeta.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.minecraft.world.chunk.ChunkStatus;

public class SimpleGenProfiler {
    private final int totalIterations;
    private final Map<ChunkStatus, AtomicInteger> steps;
    
    public SimpleGenProfiler(int totalIterations) {
        this.totalIterations = totalIterations;
        this.steps = new HashMap<>();
    }
    
    public void addStep(ChunkStatus chunkStatus) {
        this.steps.put(chunkStatus, new AtomicInteger());
    }
    
    public GenProfilerStep createStep(ChunkStatus chunkStatus) {
        int iterations = this.iterate(chunkStatus);
        
        if (iterations > this.totalIterations) {
            return new GenProfilerStep(chunkStatus) {
                @Override
                public void start() {}
                
                @Override
                public void stop() {}
            };
        }
        
        return new GenProfilerStep(chunkStatus);
    }
    
    private int iterate(ChunkStatus chunkStatus) {
        return this.steps.get(chunkStatus).incrementAndGet();
    }
    
    public static class GenProfilerStep {
        private long timeNano;
        private long elapsedTimeNano;
        
        private ChunkStatus chunkStatus;
        
        public GenProfilerStep(ChunkStatus chunkStatus) {
            this.timeNano = 0;
            this.elapsedTimeNano = 0;
            this.chunkStatus = chunkStatus;
        }
        
        public void start() {
            this.timeNano = System.nanoTime();
        }
        
        public void stop() {
            this.elapsedTimeNano = System.nanoTime() - this.timeNano;
            
            double elapsedTimeSeconds = (double)this.elapsedTimeNano / 1000000000D;
            ModernBeta.log(Level.INFO, this.chunkStatus.getId() + ", time elapsed: " + elapsedTimeSeconds);
        }
    }
}
