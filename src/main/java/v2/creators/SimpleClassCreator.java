package v2.creators;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class SimpleClassCreator<T> implements ClassCreator<T>, ClassCreatorSI<T> {
    private Set<T> cornerCases;

    @Override
    public Set<T> createCornerCases() {
        return cornerCases;
    }

    public SimpleClassCreator() {
    }

    public void setCornerCases(Set<T> cornerCases) {
        this.cornerCases = cornerCases;
    }

    @Override
    public T createInstance() {
        Random r = new Random();
        int index = r.nextInt(cornerCases.size()-1);
        return new ArrayList<>(cornerCases).get(index); // TODO: 14/09/2017 THis is not pretty
    }
}
