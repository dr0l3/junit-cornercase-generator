package v2.creators;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleCollectionCreator implements SetCreator, ListCreator, MapCreator {
    @Override
    public <T> Set<Set<T>> createSets(Class<T> clazz) {
        Set<Set<T>> res = Sets.newHashSet();
        return res;
    }

    @Override
    public <T> Set<List<T>> createLists(Class<T> clazz) {
        Set<List<T>> res = Sets.newHashSet();
        return res;
    }

    @Override
    public <T, U> Set<Map<T, U>> createMaps(Class<T> key, Class<U> value) {
        Set<Map<T,U>> res = Sets.newHashSet();
        return res;
    }
}
