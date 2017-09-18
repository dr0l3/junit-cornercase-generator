package example.phaseThree;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pants {
    private List<PantsContent> contents;
    private Set<Smartphone> phones;
    private Map<Float, Integer> randomNess;

    @Override
    public String toString() {
        return "Pants{" +
                "contents=" + contents +
                ", phones=" + phones +
                ", randomNess=" + randomNess +
                '}';
    }

    public List<PantsContent> getContents() {
        return contents;
    }

    public Set<Smartphone> getPhones() {
        return phones;
    }

    public Map<Float, Integer> getRandomNess() {
        return randomNess;
    }
}
