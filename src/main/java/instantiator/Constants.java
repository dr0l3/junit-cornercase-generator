package instantiator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static Set<Boolean> boolVals = new HashSet<>(Arrays.asList(true,false));
    public static Set<Byte> byteVals = new HashSet<>(Arrays.asList(
            Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.parseByte("0"), Byte.valueOf("-1"), Byte.valueOf("1")));
    public static Set<Short> shortVals =
            new HashSet<>(Arrays.asList(Short.MIN_VALUE, Short.MAX_VALUE, (short) 0,(short) 1, (short) -1));
    public static Set<Integer> intVals = new HashSet<>(Arrays.asList(Integer.MIN_VALUE, Integer.MAX_VALUE, 0,1,-1));
    public static Set<Long> longVals = new HashSet<>(Arrays.asList(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L));
    public static Set<Float> floatVals = new HashSet<>(Arrays.asList(Float.MAX_VALUE, Float.MIN_VALUE, 0F,1F, -1F));
    public static Set<Double> doubleVals = new HashSet<>(Arrays.asList(Double.MIN_VALUE, Double.MAX_VALUE, 0D, 1D, -1D));
}
