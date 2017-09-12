package instantiator;

import java.util.HashSet;
import java.util.Set;

public class EmptySetCreator<T> implements CreationStrategy<T> {
    private Class<T> clazz;

    public EmptySetCreator(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Set<T> createFrom() {
        return new HashSet<>();
    }
}
