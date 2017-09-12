package v2.instantiators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public interface InstantiatorNormal {
    <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness);
}
