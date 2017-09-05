package instantiator;

import java.util.Set;

public interface PrimitiveInstantiator {
    Set<Boolean> getBoolVals();
    Set<Byte> getByteVals();
    Set<Short> getShortVals();
    Set<Integer> getIntVals();
    Set<Long> getLongVals();
    Set<Float> getFloatVals();
    Set<Double> getDoubleVals();
}
