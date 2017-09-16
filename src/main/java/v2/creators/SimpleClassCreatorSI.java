package v2.creators;

import com.google.common.collect.Lists;
import v2.Utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SimpleClassCreatorSI<T> implements ClassCreatorSI<T> {
    private Supplier<T> supplier;
    private List<Predicate<T>> preds = Lists.newArrayList();
    private Function<T,T> transformer;
    private int MAX_ATTEMPTS = 1000;
    private Class<T> clazz;

    public SimpleClassCreatorSI(Supplier<T> supplier, Class<T> clazz) {
        this.supplier = supplier;
        this.clazz = clazz;
    }

    @Override
    public T createInstance() {
        return Utils.getNextValue(preds,transformer,supplier,MAX_ATTEMPTS, clazz);
    }

    @Override
    public ClassCreatorSI<T> withPredicates(Predicate<T> predicate) {
        this.preds.add(predicate);
        return this;
    }

    @Override
    public ClassCreatorSI<T> withTransformer(Function<T, T> transformer) {
        this.transformer = transformer;
        return this;
    }
}
