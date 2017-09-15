package v2.creators;

import java.util.function.Function;
import java.util.function.Predicate;

public interface ClassCreatorSI<T> {
    T createInstance();
    ClassCreatorSI<T> withPredicates(Predicate<T> predicate);
    ClassCreatorSI<T> withTransformer(Function<T,T> transformer);
}
