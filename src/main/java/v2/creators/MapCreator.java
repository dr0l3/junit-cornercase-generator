package v2.creators;

import java.util.Map;
import java.util.Set;

public interface MapCreator {
    <T,U> Set<Map<T,U>> createMaps(Class<T> key, Class<U> value);
}
