package generators;

import instantiator.InstantiationStrategy;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ColorInstantiator implements InstantiationStrategy<Color> {
    @Override
    public Set<Color> createFrom() {
        Set<Color> candidates = new HashSet<>();
        candidates.add(Color.BLACK);
        candidates.add(Color.white);
        candidates.add(Color.RED);
        return candidates;
    }
}
