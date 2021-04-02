package com.bespectacled.modernbeta.util;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<A,B,C,D,R> {
    R apply(A a, B b, C c, D d);
    
    default <V> QuadFunction<A, B, C, D, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d) -> after.apply(apply(a, b, c, d));
    }
}
