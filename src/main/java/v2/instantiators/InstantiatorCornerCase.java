package v2.instantiators;

import v2.Path;
import v2.generators.*;

import java.util.Set;

public interface InstantiatorCornerCase extends PrimitiveCreatorConfigurator<InstantiatorCornerCase>,
        NullableConfigurator<InstantiatorCornerCase>,
        PrimitiveCreatorPerClassConfigurator<InstantiatorCornerCase>,
        FieldCreatorConfigurator<InstantiatorCornerCase>,
        ClassCreatorConfigurator<InstantiatorCornerCase>{
    <T> Set<T> createCornerCasesForClass(Class<T> clazz, Path path);
}
