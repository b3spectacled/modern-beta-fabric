package com.bespectacled.modernbeta.util.function;

@FunctionalInterface
public interface TriConsumer<K, V, S> {
    void accept(K k, V v, S s);
}