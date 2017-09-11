package v2;

import java.util.Set;

public interface InstantiatorNormal {
    public <T> T createInstance(Class<T> clazz);
}
