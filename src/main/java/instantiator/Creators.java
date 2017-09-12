package instantiator;

import java.util.Collection;

public class Creators {
    public static <T> CreationStrategy<T> creatorFromValues(Collection<T> values){
        return new ClassValueCreator<>(values);
    }
}
