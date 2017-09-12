package v2.creators;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionCreator {
    <T> Set<T> createSet(Class<T> clazz);
    <T> List<T> createList(Class<T> clazz);
    <T,U> Map<T,U> createMap(Class<T> keyClazz, Class<U> valueClazz);
}
