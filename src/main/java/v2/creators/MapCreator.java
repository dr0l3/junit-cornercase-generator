package v2.creators;

import v2.Path;

import java.util.Map;
import java.util.Set;

public interface MapCreator {
    <T,U,V> Set<Map<T,U>> createMaps(Class<T> key, Class<U> value, Class<V> parent, Path path);
}
