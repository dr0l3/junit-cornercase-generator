package instantiator;

import java.util.Set;

public interface CreationStrategy<T> {
    Set<T> createFrom();
}
