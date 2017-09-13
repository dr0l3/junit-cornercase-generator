package v2.creators;

import java.util.Set;

public interface SetCreator {
    <T> Set<Set<T>> createSets(Class<T> clazz);
}
