package instantiator;

import java.util.Set;

public interface InstantiationStrategy<T> {
    Set<T> createFrom();
}
