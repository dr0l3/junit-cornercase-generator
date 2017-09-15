package v2.creators;

import java.util.function.Function;
import java.util.function.Predicate;

public class NullClassCreator<T> implements ClassCreatorSI<T>{

    @Override
    public T createInstance() {
        return null;
    }

    @Override
    public ClassCreatorSI<T> withPredicates(Predicate<T> predicate) {
        return this;
    }

    @Override
    public ClassCreatorSI<T> withTransformer(Function<T, T> transformer) {
        return this;
    }
}
