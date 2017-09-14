package v2.generators;

import v2.creators.ClassCreator;

public interface FieldCreatorConfigurator<T> {
    <U,V> T withCreatorForField(Class<U> parentClass, String name, Class<V> fieldClass, ClassCreator creator);
}
