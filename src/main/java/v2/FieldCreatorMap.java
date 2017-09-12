package v2;

import com.google.common.collect.Maps;
import org.javatuples.Triplet;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorSI;

import java.util.Map;

public class FieldCreatorMap {
    private Map<Triplet<Class<?>,String,Class<?>>, ClassCreator<?>> creatorMap = Maps.newConcurrentMap();

    public <T,U> void put(Triplet<Class<T>, String, Class<U>> key, ClassCreator<U> value){
        creatorMap.put(Triplet.with(key.getValue0(), key.getValue1(), key.getValue2()),value);
//        creatorMap.put(key,value); <-- does not compile
    }

    public <T,U> ClassCreator<U> get(Triplet<Class<T>, String, Class<U>> key){
        return (ClassCreator<U>) creatorMap.get(key);
    }

    public <T,U> ClassCreator<U> getOrDefault(Triplet<Class<T>, String, Class<U>> key, ClassCreator<U> defaultValue){
        return (ClassCreator<U>) creatorMap.getOrDefault(key, defaultValue);
    }

    public <T,U> boolean containsKey(Triplet<Class<T>, String, Class<U>> key){
        return creatorMap.containsKey(key);
    }
}
