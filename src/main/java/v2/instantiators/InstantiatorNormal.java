package v2.instantiators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.generators.FieldCreatorSIConfigurator;
import v2.generators.PrimitiveCreatorSIConfigurator;
import v2.generators.PrimitiveCreatorSIPerClassConfigurator;

public interface InstantiatorNormal extends PrimitiveCreatorSIConfigurator<InstantiatorNormal>,
        PrimitiveCreatorSIPerClassConfigurator<InstantiatorNormal>,
        FieldCreatorSIConfigurator<InstantiatorNormal> {
    <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness);
}
