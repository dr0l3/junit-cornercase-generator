package v2;

import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class SubTypeMap {
    private static Map<Class<?>, Set<?>> map = Maps.newConcurrentMap();
    public static Reflections reflections = new Reflections(); // TODO: 13/09/2017 Move and configuration
    // FIXME: 13/09/2017 Only create reflections boject if we need to!

    public static <T> void put(Class<T> key, Set<Class<? extends T>> value){
        map.put(key,value);
    }

    public static <T> Set<Class<? extends T>> get(Class<T> key){
        return (Set<Class<? extends T>>) map.get(key);
    }

    public static <T> boolean containsKey(Class<T> key){
        return map.containsKey(key);
    }
}
