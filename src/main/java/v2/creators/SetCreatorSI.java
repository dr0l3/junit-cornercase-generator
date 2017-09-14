package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Set;

public interface SetCreatorSI {
    <T,U> Set<T> createSet(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness);
}
