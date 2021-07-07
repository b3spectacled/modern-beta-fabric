package com.bespectacled.modernbeta.util.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public abstract class ObjectPool<T> {
    private final ConcurrentLinkedQueue<T> pool;
    private final int initialCapacity;
    private final Predicate<T> validator;
    
    public ObjectPool(int initialCapacity, Predicate<T> validator) {
        this.pool = new ConcurrentLinkedQueue<T>();
        this.initialCapacity = initialCapacity;
        this.validator = validator;
    }
    
    protected abstract T createObj();
    
    public T borrowObj() {
        // Initialize pool on first use.
        if (this.pool.isEmpty())
            this.initPool();
        
        T obj = this.pool.poll();
        
        if (obj == null)
            obj = this.createObj();
        
        return obj;
    }
    
    public void returnObj(T obj) {
        if (obj == null || !validator.test(obj))
            throw new IllegalArgumentException("[Modern Beta] Returned object of invalid type!");
        
        this.pool.add(obj);
    }
    
    private void initPool() {
        for (int i = 0; i < this.initialCapacity; ++i) {
            this.pool.add(this.createObj());
        }
    }
}