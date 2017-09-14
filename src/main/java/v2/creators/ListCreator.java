package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.List;
import java.util.Set;

public interface ListCreator {
    <T,U> Set<List<T>> createLists(Class<T> clazz, Class<U> parent);
}