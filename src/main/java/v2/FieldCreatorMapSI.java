package v2;

import com.google.common.collect.Maps;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorSI;

import java.util.Map;

public class FieldCreatorMapSI {
    private Map<Triplet<Class<?>,String,Class<?>>, ClassCreatorSI<?>> creatorMap = Maps.newConcurrentMap();

    public <T,U> void put(Triplet<Class<T>, String, Class<U>> key, ClassCreatorSI<U> value){
        creatorMap.put(Triplet.with(key.getValue0(), key.getValue1(), key.getValue2()),value);
//        creatorMap.put(key,value); <-- does not compile
    }

    public <T,U> ClassCreatorSI<U> get(Triplet<Class<T>, String, Class<U>> key){
        return (ClassCreatorSI<U>) creatorMap.get(key);
    }

    public <T,U> ClassCreatorSI<U> getOrDefault(Triplet<Class<T>, String, Class<U>> key, ClassCreatorSI<U> defaultValue){
        return (ClassCreatorSI<U>) creatorMap.getOrDefault(key, defaultValue);
    }

    public <T,U> boolean containsKey(Triplet<Class<T>, String, Class<U>> key){
        return creatorMap.containsKey(key);
    }
}
