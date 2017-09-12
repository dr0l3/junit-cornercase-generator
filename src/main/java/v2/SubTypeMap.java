package v2;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubTypeMap {
    private Map<Class<?>, Set<Class<?>>> map = Maps.newConcurrentMap();

    public <T> void put(Class<T> key, Set<Class<? extends T>> value){
        map.put(key,value);
    }

    public <T> Set<Class<T>> get(Class<T> key){
//        return map.get(key).stream().map(v -> Class<T>. v).collect(Collectors.toSet());
        return null;
    }

    public <T> boolean containsKey(Class<T> key){
        return map.containsKey(key);
    }
}
