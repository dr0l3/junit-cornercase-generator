package v2.generators;

import v2.creators.ClassCreatorSI;

public interface FieldCreatorSIConfigurator<T> {
    <U,V> T withCreatorForField(Class<U> parentClass, String name, Class<V> fieldClass, ClassCreatorSI creator);
}
