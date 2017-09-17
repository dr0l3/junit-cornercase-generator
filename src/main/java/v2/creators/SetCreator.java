package v2.creators;

import v2.Path;

import java.util.Set;

public interface SetCreator {
    <T,U> Set<Set<T>> createSets(Class<T> clazz, Class<U> parent, Path path);
}
