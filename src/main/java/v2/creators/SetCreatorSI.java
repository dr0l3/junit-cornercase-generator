package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Set;

public interface SetCreatorSI {
    <T> Set<T> createSet(Class<T> clazz, SourceOfRandomness randomness);
}
