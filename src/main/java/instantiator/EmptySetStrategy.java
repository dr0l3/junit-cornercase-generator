package instantiator;

import instantiator.InstantiationStrategy;

import java.util.HashSet;
import java.util.Set;

public class EmptySetStrategy<T> implements InstantiationStrategy<T> {
    private Class<T> clazz;

    public EmptySetStrategy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Set<T> createFrom() {
        return new HashSet<>();
    }
}
