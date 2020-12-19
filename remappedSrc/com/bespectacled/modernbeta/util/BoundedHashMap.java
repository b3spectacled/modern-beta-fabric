package com.bespectacled.modernbeta.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class BoundedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int bound;
    
    public BoundedHashMap(int bound) {
        this.bound = bound;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > bound;
    }
}
