package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class SimpleNormalPrimCreator implements PrimitiveCreatorSI {
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

    public SimpleNormalPrimCreator(SourceOfRandomness randomness) {
    }

    public SimpleNormalPrimCreator() {
    }

    public static SimpleNormalPrimCreator nonNeg(){
        SimpleNormalPrimCreator inst = new SimpleNormalPrimCreator();
        inst.byte_min = 0;
        inst.double_min = 0d;
        inst.short_min = 0;
        inst.float_min = 0f;
        inst.long_min = 0L;
        inst.int_min = 0;
        return inst;
    }

    @Override
    public Boolean nextBool(SourceOfRandomness randomness) {
        return randomness.nextBoolean();
    }

    @Override
    public Byte nextByte(SourceOfRandomness randomness) {
        return randomness.nextByte(byte_min, byte_max);
    }

    @Override
    public Short nextShort(SourceOfRandomness randomness) {
        return randomness.nextShort(short_min,short_max);
    }

    @Override
    public Integer nextInt(SourceOfRandomness randomness) {
        return randomness.nextInt(int_min,int_max);
    }

    @Override
    public Long nextLong(SourceOfRandomness randomness) {
        return randomness.nextLong(long_min, long_max);
    }

    @Override
    public Float nextFloat(SourceOfRandomness randomness) {
        return randomness.nextFloat(float_min, float_max);
    }

    @Override
    public Double nextDouble(SourceOfRandomness randomness) {
        return randomness.nextDouble(double_min,double_max);
    }

    @Override
    public <T> T getValueForType(Class<T> clazz, SourceOfRandomness randomness) {
        if(clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
            return (T) nextInt(randomness);
        } else if(clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
            return (T) nextLong(randomness);
        } else if(clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
            return (T) nextDouble(randomness);
        } else if(clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
            return (T) nextFloat(randomness);
        } else if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)){
            return (T) nextBool(randomness);
        } else if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)){
            return (T) nextByte(randomness);
        } else if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
            return (T) nextShort(randomness);
        } else {
            throw new RuntimeException("Invalid type " + clazz + " is not a primitive, and should not be instantiated from a primitive creator strategy");
        }
    }
}
