package v2.generators;

import v2.creators.PrimitiveCreator;

public interface PrimitiveCreatorPerClassConfigurator<T> {
    <U> T withPrimitiveCreatorForClass(Class<U> clazz, PrimitiveCreator creator);
}
