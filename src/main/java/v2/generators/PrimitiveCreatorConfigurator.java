package v2.generators;

import v2.creators.PrimitiveCreator;

public interface PrimitiveCreatorConfigurator<T> {
    T withDefaultPrimitiveCreator(PrimitiveCreator creator);
}
