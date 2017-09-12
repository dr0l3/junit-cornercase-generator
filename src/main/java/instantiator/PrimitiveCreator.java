package instantiator;

import java.util.Set;
import java.util.function.Predicate;

public interface PrimitiveCreator {
//    PrimitiveCreator withConstraint(Predicate<? super Number> predicate);
    Set<Boolean> getBoolVals();
    Set<Byte> getByteVals();
    Set<Short> getShortVals();
    Set<Integer> getIntVals();
    Set<Long> getLongVals();
    Set<Float> getFloatVals();
    Set<Double> getDoubleVals();
}
