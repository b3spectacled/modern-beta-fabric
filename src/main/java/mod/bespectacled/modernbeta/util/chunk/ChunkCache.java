package mod.bespectacled.modernbeta.util.chunk;

import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;

/*
 * Generic threadsafe(???) cache for anything that outputs T given pair of integer chunk coordinates.
 * 
 */
public class ChunkCache<T> {
    public static final int DEFAULT_SIZE = 512;
    public static final boolean DEFAULT_EVICT = true;
    
    @SuppressWarnings("unused")
    private final String name;
    private final int capacity;
    private final boolean evictOldChunks;
    
    private final BiFunction<Integer, Integer, T> chunkFunc;
    private final Long2ObjectLinkedOpenHashMap<T> chunkMap;
    
    private final StampedLock lock;

    public ChunkCache(String name, int capacity, boolean evictOldChunks, BiFunction<Integer, Integer, T> chunkFunc) {
        this.name = name;
        this.capacity = capacity;
        this.evictOldChunks = evictOldChunks;
        
        this.chunkFunc = chunkFunc;
        this.chunkMap = new Long2ObjectLinkedOpenHashMap<>(capacity);
        
        this.lock = new StampedLock();
    }
    
    public ChunkCache(String name, int capacity, BiFunction<Integer, Integer, T> chunkFunc) {
        this(name, capacity, DEFAULT_EVICT, chunkFunc);
    }
    
    public ChunkCache(String name, BiFunction<Integer, Integer, T> chunkFunc) {
        this(name, DEFAULT_SIZE, DEFAULT_EVICT, chunkFunc);
    }
    
    public void clear() {
        long stamp = this.lock.writeLock();
        try {
            this.chunkMap.clear();
            this.chunkMap.trim();
        } finally {
            this.lock.unlock(stamp);
        }
    }
    
    public T get(int chunkX, int chunkZ) {
        T chunk;
        
        long key = ChunkPos.toLong(chunkX, chunkZ);
        long stamp = this.lock.readLock();
        
        try {
            while ((chunk = this.chunkMap.get(key)) == null) {
                // Attempt to upgrade read lock to write lock w/o blocking
                long writeStamp = this.lock.tryConvertToWriteLock(stamp);
                
                // Write lock, if:
                // => lock upgrade succeeds w/o blocking
                // => blocked write is acquired anyway (see below)
                if (writeStamp != 0) {
                    stamp = writeStamp;
                    chunk = this.createChunk(key, chunkX, chunkZ);
                    
                    break;
                }
                
                // Lock upgrade failed so use blocking write lock.
                this.lock.unlockRead(stamp);
                stamp = this.lock.writeLock();
            }
        } finally {
            this.lock.unlock(stamp);
        }
        
        return chunk;
    }
    
    private T createChunk(long key, int chunkX, int chunkZ) {
        // Ensure cache size remains below capacity
        if (this.evictOldChunks && this.chunkMap.size() >= this.capacity) {
            this.chunkMap.removeFirst();
        }
        
        T chunk = this.chunkFunc.apply(chunkX, chunkZ);
        this.chunkMap.put(key, chunk);
        
        return chunk;
    }
}
