package v2.creators;

import java.util.Set;

public interface PrimitiveCreator {
    Set<Boolean> getBools();
    Set<Byte> getBytes();
    Set<Short> getShorts();
    Set<Integer> getInts();
    Set<Long> getLongs();
    Set<Float> getFloats();
    Set<Double> getDoubles();
    <T> Set<T> getValuesForType(Class<T> clazz);
}
