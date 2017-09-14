package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.List;

public interface ListCreatorSI {
    <T,U> List<T> createList(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness);
}
