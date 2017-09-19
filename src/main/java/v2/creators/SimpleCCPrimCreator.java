package v2.creators;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleCCPrimCreator implements PrimitiveCreator {
    public Set<Boolean> boolVals = new HashSet<>(Arrays.asList(true,false));
    public Set<Byte> byteVals = new HashSet<>(Arrays.asList(
            Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.parseByte("0"), Byte.valueOf("-1"), Byte.valueOf("1")));
    public Set<Short> shortVals =
            new HashSet<>(Arrays.asList(Short.MIN_VALUE, Short.MAX_VALUE, (short) 0,(short) 1, (short) -1));
    public Set<Integer> intVals = new HashSet<>(Arrays.asList(Integer.MIN_VALUE, Integer.MAX_VALUE, 0,1,-1));
    public Set<Long> longVals = new HashSet<>(Arrays.asList(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L));
    public Set<Float> floatVals = new HashSet<>(Arrays.asList(Float.MAX_VALUE, Float.MIN_VALUE, 0F,1F, -1F));
    public Set<Double> doubleVals = new HashSet<>(Arrays.asList(Double.MIN_VALUE, Double.MAX_VALUE, 0D, 1D, -1D));

    @Override
    public Set<Boolean> getBools() {
        return boolVals;
    }

    @Override
    public Set<Byte> getBytes() {
        return byteVals;
    }

    @Override
    public Set<Short> getShorts() {
        return shortVals;
    }

    @Override
    public Set<Integer> getInts() {
        return intVals;
    }

    @Override
    public Set<Long> getLongs() {
        return longVals;
    }

    @Override
    public Set<Float> getFloats() {
        return floatVals;
    }

    @Override
    public Set<Double> getDoubles() {
        return doubleVals;
    }

    @Override
    public <T> Set<T> getValuesForType(Class<T> fieldType) {
        if(fieldType.equals(Integer.TYPE) || fieldType.equals(Integer.class)) {
            return (Set<T>) intVals;
        } else if(fieldType.equals(Long.TYPE) || fieldType.equals(Long.class)) {
            return (Set<T>) longVals;
        } else if(fieldType.equals(Double.TYPE) || fieldType.equals(Double.class)) {
            return (Set<T>) doubleVals;
        } else if(fieldType.equals(Float.TYPE) || fieldType.equals(Float.class)) {
            return (Set<T>) floatVals;
        } else if (fieldType.equals(Boolean.TYPE) || fieldType.equals(Boolean.class)){
            return (Set<T>) boolVals;
        } else if (fieldType.equals(Byte.TYPE) || fieldType.equals(Byte.class)){
            return (Set<T>) byteVals;
        } else if (fieldType.equals(Short.TYPE) || fieldType.equals(Short.class)) {
            return (Set<T>) shortVals;
        } else {
            throw new RuntimeException("Invalid type " + fieldType + " is not a primitive, and should not be instantiated from a primitive creator strategy");
        }
    }

    public <T> Set<T> getArrayValuesForType(Class<T> clazz){
        if(clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
            int[] nulls = null;
            int[] empty  = new int[0];
            int[] single = new int[]{1};
            int[] many = new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0,1,-1};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if(clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
            long[] nulls = null;
            long[] empty  = new long[0];
            long[] single = new long[]{1};
            long[] many = new long[]{Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if(clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
            double[] nulls = null;
            double[] empty  = new double[0];
            double[] single = new double[]{1};
            double[] many = new double[]{Double.MIN_VALUE, Double.MAX_VALUE, 0D, 1D, -1D};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if(clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
            float[] nulls = null;
            float[] empty  = new float[0];
            float[] single = new float[]{1};
            float[] many = new float[]{Float.MAX_VALUE, Float.MIN_VALUE, 0F,1F, -1F};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if (clazz.equals(Boolean.TYPE) || clazz.equals(Boolean.class)){
            boolean[] nulls = null;
            boolean[] empty  = new boolean[0];
            boolean[] single = new boolean[]{false};
            boolean[] many = new boolean[]{true,false};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)){
            byte[] nulls = null;
            byte[] empty  = new byte[0];
            byte[] single = new byte[]{1};
            byte[] many = new byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.parseByte("0"), Byte.valueOf("-1"), Byte.valueOf("1")};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
            short[] nulls = null;
            short[] empty  = new short[0];
            short[] single = new short[]{1};
            short[] many = new short[]{Short.MIN_VALUE, Short.MAX_VALUE, (short) 0,(short) 1, (short) -1};

            return (Set<T>) Sets.newHashSet(nulls,empty,single,many);
        } else {
            throw new RuntimeException("Invalid type " + clazz + " is not a primitive, and should not be instantiated from a primitive creator strategy");
        }
    }

    public static SimpleCCPrimCreator nonNegative(){
        SimpleCCPrimCreator inst = new SimpleCCPrimCreator();
        inst.byteVals = (inst.byteVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.shortVals = (inst.shortVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.longVals = (inst.longVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.intVals = (inst.intVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.doubleVals = (inst.doubleVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        return inst;
    }
    public static SimpleCCPrimCreator positive(){
        SimpleCCPrimCreator inst = new SimpleCCPrimCreator();
        inst.byteVals = (inst.byteVals.stream().filter(b -> b > 0).collect(Collectors.toSet()));
        inst.shortVals = (inst.shortVals.stream().filter(b -> b > 0).collect(Collectors.toSet()));
        inst.longVals = (inst.longVals.stream().filter(b -> b > 0).collect(Collectors.toSet()));
        inst.intVals = (inst.intVals.stream().filter(b -> b > 0).collect(Collectors.toSet()));
        inst.doubleVals = (inst.doubleVals.stream().filter(b -> b > 0).collect(Collectors.toSet()));
        return inst;
    }


    public static SimpleCCPrimCreator fromPredicate(Predicate<? super Number> predicate){
        SimpleCCPrimCreator inst = new SimpleCCPrimCreator();
        inst.byteVals = (inst.byteVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.shortVals = (inst.shortVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.longVals = (inst.longVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.intVals = (inst.intVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.doubleVals = (inst.doubleVals.stream().filter(predicate).collect(Collectors.toSet()));
        return inst;
    }
}
