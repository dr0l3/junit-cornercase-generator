package v2.generators;

import v2.creators.PrimitiveCreatorSI;

public interface PrimitiveCreatorSIPerClassConfigurator<T> {
    <U> T withPrimitiveCreatorSIForClass(Class<U> clazz, PrimitiveCreatorSI creator);
}
