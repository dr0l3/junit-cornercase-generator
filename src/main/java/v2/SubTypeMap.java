package v2;

import com.google.common.collect.Maps;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SubTypeMap {
    private static Map<Class<?>, Set<?>> map = Maps.newConcurrentMap();
    private static Reflections reflections = new Reflections();//new ConfigurationBuilder()
//            .setUrls(ClasspathHelper.forJavaClassPath())
//            .setScanners(new SubTypesScanner())); // TODO: 13/09/2017 Move and configuration
    // FIXME: 13/09/2017 Only create reflections boject if we need to!

    public static <T> void put(Class<T> key, Set<Class<? extends T>> value){
        map.put(key,value);
    }

    public static <T> Set<Class<? extends T>> get(Class<T> key){
        return (Set<Class<? extends T>>) map.get(key);
    }

    public static <T> Optional<Set<Class<? extends T>>> getImplementations(Class<T> baseClass){
        if(map.containsKey(baseClass)){
            return Optional.of((Set<Class<? extends T>>) map.get(baseClass));
        } else {
            Set<Class<? extends T>> subtypes = SubTypeMap.reflections.getSubTypesOf(baseClass);
            if(subtypes.size() > 0){
                SubTypeMap.put(baseClass,subtypes);
                return Optional.of(subtypes);
            } else {
                return Optional.empty();
            }
        }
    }

    public static <T> boolean containsKey(Class<T> key){
        return map.containsKey(key);
    }
}
