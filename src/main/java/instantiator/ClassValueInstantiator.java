package instantiator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassValueInstantiator<T> implements InstantiationStrategy<T> {
    private Collection<T> values;

    public ClassValueInstantiator(Collection<T> values) {
        this.values = values;
    }

    @Override
    public Set<T> createFrom() {
        return new HashSet<>(values);
    }
}
