package v2;

import com.google.common.collect.Maps;
import io.vavr.control.Option;
import v2.creators.ClassCreator;

import java.util.Map;

public class ClassCreatorMap {
    private Map<Class<?>,ClassCreator<?>> map = Maps.newConcurrentMap();

    public <T> void put(Class<T> key, ClassCreator<T> value){
        map.put(key,value);
    }

    public <T> Option<ClassCreator<T>> get(Class<T> key){
        if(map.containsKey(key)){
            return Option.of((ClassCreator<T>) map.get(key));
        } else {
            return Option.none();
        }
    }

    public <T> ClassCreator<T> getOrDefault(Class<T> key, ClassCreator<T> defaultCreator){
        return (ClassCreator<T>) map.getOrDefault(key,defaultCreator);
    }

    public <T> boolean containsKey(Class<T> key){
        return map.containsKey(key);
    }
}
