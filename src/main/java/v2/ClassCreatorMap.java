package v2;

import com.google.common.collect.Maps;
import v2.creators.ClassCreator;

import java.util.Map;

public class ClassCreatorMap {
    private Map<Class<?>,ClassCreator<?>> map = Maps.newConcurrentMap();

    public <T> void put(Class<T> key, ClassCreator<T> value){
        map.put(key,value);
    }

    public <T> ClassCreator<T> get(Class<T> key){
        return (ClassCreator<T>) map.get(key);
    }

    public <T> ClassCreator<T> getOrDefault(Class<T> key, ClassCreator<T> defaultCreator){
        return (ClassCreator<T>) map.getOrDefault(key,defaultCreator);
    }
}
