package v2;

import java.util.Set;

public interface ClassCreator<T> {
    Set<T> createCornerCases();
    T createInstance();
}
