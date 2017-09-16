package v2.instantiators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.generators.*;

public interface InstantiatorNormal extends PrimitiveCreatorSIConfigurator<InstantiatorNormal>,
        PrimitiveCreatorSIPerClassConfigurator<InstantiatorNormal>,
        FieldCreatorSIConfigurator<InstantiatorNormal>,
        ClassCreatorSIConfigurator<InstantiatorNormal> {
    <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness);
}
