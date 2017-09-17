package example;

import java.util.List;

public class PersonTwo {
    private final List<Integer> ints;

    public PersonTwo(List<Integer> ints) {
        this.ints = ints;
    }

    public List<Integer> getInts() {
        return ints;
    }

    @Override
    public String toString() {
        return "PersonTwo{" +
                "ints=" + ints +
                '}';
    }
}
