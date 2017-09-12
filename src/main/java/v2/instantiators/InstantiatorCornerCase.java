package v2.instantiators;

import java.util.Set;

public interface InstantiatorCornerCase {
    public <T> Set<T> createCornerCasesForClass(Class<T> clazz);
}
