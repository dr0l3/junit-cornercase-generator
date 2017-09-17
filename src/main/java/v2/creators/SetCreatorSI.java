package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.Path;

import java.util.Set;

public interface SetCreatorSI {
    <T,U> Set<T> createSet(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path);
}
