package v2.generators;

import v2.creators.ClassCreatorSI;

public interface ClassCreatorSIConfigurator<T> {
    <U> T withCreatorForClass(Class<U> clazz, ClassCreatorSI<U> creator);
}
