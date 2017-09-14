package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Map;

public interface MapCreatorSI {
    <T,U,V> Map<T,U> createMap(Class<T> keyClazz, Class<U> valueClazz, Class<V> parent, SourceOfRandomness randomness);
}
