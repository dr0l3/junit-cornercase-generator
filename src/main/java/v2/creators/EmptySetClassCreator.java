package v2.creators;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class EmptySetClassCreator<T> implements ClassCreator<T> {
    @Override
    public Set<T> createCornerCases() {
        return Sets.newHashSet();
    }

    @Override
    public T createInstance() {
        return null; // TODO: 15/09/2017 THis is bad
    }

    @Override
    public ClassCreator<T> withPredicates(Predicate<T> predicate) {
        return this;
    }

    @Override
    public ClassCreator<T> withTransformer(Function<T, T> transformer) {
        return this;
    }
}
