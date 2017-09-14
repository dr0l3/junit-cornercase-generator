package v2.generators;

import v2.creators.PrimitiveCreatorSI;

public interface PrimitiveCreatorSIConfigurator<T> {
    T withDefaultPrimitiveCreator(PrimitiveCreatorSI creator);
}
