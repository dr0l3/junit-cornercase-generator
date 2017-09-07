package instantiator;

import java.util.Collection;

public class Instantiators {
    public static <T> InstantiationStrategy<T> createInstantiator(Collection<T> values){
        return new ClassValueInstantiator<>(values);
    }
}
