package instantiator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassValueCreator<T> implements CreationStrategy<T> {
    private Collection<T> values;

    public ClassValueCreator(Collection<T> values) {
        this.values = values;
    }

    @Override
    public Set<T> createFrom() {
        return new HashSet<>(values);
    }
}
