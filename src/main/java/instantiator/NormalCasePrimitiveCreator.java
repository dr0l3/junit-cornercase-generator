package instantiator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.*;
import java.util.function.Predicate;

public class NormalCasePrimitiveCreator implements PrimitiveCreator {
    private SourceOfRandomness randomness;
    private Byte byte_min = Byte.MIN_VALUE;
    private Byte byte_max = Byte.MAX_VALUE;
    private Short short_min = Short.MIN_VALUE;
    private Short short_max = Short.MAX_VALUE;
    private Long long_min = Long.MIN_VALUE;
    private Long long_max = Long.MAX_VALUE;
    private Integer int_min = Integer.MIN_VALUE;
    private Integer int_max = Integer.MAX_VALUE;
    private Float float_min = Float.MIN_VALUE;
    private Float float_max = Float.MAX_VALUE;
    private Double double_min = Double.MIN_VALUE;
    private Double double_max = Double.MAX_VALUE;

    public NormalCasePrimitiveCreator(SourceOfRandomness randomness) {
        this.randomness = randomness;
    }

    public NormalCasePrimitiveCreator() {
    }

    public static NormalCasePrimitiveCreator nonNeg(){
        NormalCasePrimitiveCreator inst = new NormalCasePrimitiveCreator();
        inst.randomness = new SourceOfRandomness(new Random());
        inst.byte_min = 0;
        inst.double_min = 0d;
        inst.short_min = 0;
        inst.float_min = 0f;
        inst.long_min = 0L;
        inst.int_min = 0;
        return inst;
    }

    @Override
    public Set<Boolean> getBoolVals() {
        return new HashSet<>(Arrays.asList(randomness.nextBoolean()));
    }

    @Override
    public Set<Byte> getByteVals() {
        return new HashSet<>(Arrays.asList(randomness.nextByte(byte_min, byte_max)));
    }

    @Override
    public Set<Short> getShortVals() {
        return new HashSet<>(Arrays.asList(randomness.nextShort(short_min, short_max)));
    }

    @Override
    public Set<Integer> getIntVals() {
        return new HashSet<>(Arrays.asList(randomness.nextInt(int_min, int_max)));
    }

    @Override
    public Set<Long> getLongVals() {
        return new HashSet<>(Arrays.asList(randomness.nextLong(long_min, long_max)));
    }

    @Override
    public Set<Float> getFloatVals() {
        return new HashSet<>(Arrays.asList(randomness.nextFloat(float_min, float_max)));
    }

    @Override
    public Set<Double> getDoubleVals() {
        return new HashSet<>(Arrays.asList(randomness.nextDouble(double_min,double_max)));
    }
}
