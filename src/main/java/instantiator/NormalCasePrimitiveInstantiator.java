package instantiator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NormalCasePrimitiveInstantiator implements PrimitiveInstantiator {
    private SourceOfRandomness randomness;

    public NormalCasePrimitiveInstantiator(SourceOfRandomness randomness) {
        this.randomness = randomness;
    }

    @Override
    public Set<Boolean> getBoolVals() {
        return new HashSet<>(Arrays.asList(randomness.nextBoolean()));
    }

    @Override
    public Set<Byte> getByteVals() {
        return new HashSet<>(Arrays.asList(randomness.nextByte(Byte.MIN_VALUE, Byte.MAX_VALUE)));
    }

    @Override
    public Set<Short> getShortVals() {
        return new HashSet<>(Arrays.asList(randomness.nextShort(Short.MIN_VALUE, Short.MAX_VALUE)));
    }

    @Override
    public Set<Integer> getIntVals() {
        return new HashSet<>(Arrays.asList(randomness.nextInt()));
    }

    @Override
    public Set<Long> getLongVals() {
        return new HashSet<>(Arrays.asList(randomness.nextLong()));
    }

    @Override
    public Set<Float> getFloatVals() {
        return new HashSet<>(Arrays.asList(randomness.nextFloat()));
    }

    @Override
    public Set<Double> getDoubleVals() {
        return new HashSet<>(Arrays.asList(randomness.nextDouble()));
    }
}
