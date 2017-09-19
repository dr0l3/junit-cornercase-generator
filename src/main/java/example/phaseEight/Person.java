package example.phaseEight;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.util.Arrays;

public class Person {
    private Integer[] integerArray;
    private int[] intArray;
    private List<Integer> ints;
    private Set<Float> floats;
    private Map<Integer,Integer> intMap;


    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public List<Integer> getInts() {
        return ints;
    }

    public Set<Float> getFloats() {
        return floats;
    }

    public Map<Integer, Integer> getIntMap() {
        return intMap;
    }

    @Override
    public String toString() {
        return "Person{" +
                "intArray=" + Arrays.toString(intArray) +
                ", integerArray=" + Arrays.toString(integerArray) +
                ", ints=" + ints +
                ", floats=" + floats +
                ", intMap=" + intMap +
                '}';
    }
}
