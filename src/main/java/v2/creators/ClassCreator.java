package v2.creators;

import java.util.Set;

public interface ClassCreator<T> {
    Set<T> createCornerCases();
}
