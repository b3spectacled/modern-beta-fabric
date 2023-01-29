package mod.bespectacled.modernbeta.util.function;

import java.util.Objects;

public interface TriPredicate<S1, S2, S3> {
    boolean test(S1 p1, S2 p2, S3 p3);

    default TriPredicate<S1, S2, S3> and(TriPredicate<? super S1, ? super S2, ? super S3> other) {
        Objects.requireNonNull(other);
        return (S1 p1, S2 p2, S3 p3) -> test(p1, p2, p3) && other.test(p1, p2, p3);
    }

    default TriPredicate<S1, S2, S3> negate() {
        return (S1 p1, S2 p2, S3 p3) -> !test(p1, p2, p3);
    }

    default TriPredicate<S1, S2, S3> or(TriPredicate<? super S1, ? super S2, ? super S3> other) {
        Objects.requireNonNull(other);
        return (S1 p1, S2 p2, S3 p3) -> test(p1, p2, p3) || other.test(p1, p2, p3);
    }
}
