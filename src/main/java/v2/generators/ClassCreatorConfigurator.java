package v2.generators;

import v2.creators.ClassCreator;

public interface ClassCreatorConfigurator<T> {
    <U> T withCreatorForClass(Class<U> clazz, ClassCreator<U> creator);
}
