package v2;

import com.google.common.collect.Maps;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorSI;

import java.util.Map;

public class ClassCreatorMapSI {
    private Map<Class<?>,ClassCreatorSI<?>> map = Maps.newConcurrentMap();

    public <T> void put(Class<T> key, ClassCreatorSI<T> value){
        map.put(key,value);
    }

    public <T> ClassCreatorSI<T> get(Class<T> key){
        return (ClassCreatorSI<T>) map.get(key);
    }

    public <T> ClassCreatorSI<T> getOrDefault(Class<T> key, ClassCreatorSI<T> defaultCreator){
        return (ClassCreatorSI<T>) map.getOrDefault(key,defaultCreator);
    }
}
