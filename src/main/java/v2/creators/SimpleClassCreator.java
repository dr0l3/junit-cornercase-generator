package v2.creators;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleClassCreator<T> implements ClassCreator<T> {
    private Set<T> cornerCases;
    private Supplier<T> normalCases;
    private List<Predicate<T>> preds = Lists.newArrayList();
    private Function<T,T> transformer;

    public SimpleClassCreator() {
    }

    public void setCornerCases(Set<T> cornerCases) {
        this.cornerCases = cornerCases;
    }

    public void setNormalCases(Supplier<T> normalCases) {
        this.normalCases = normalCases;
    }

    @Override
    public Set<T> createCornerCases() {
        Set<T> candidates = cornerCases;
        if(transformer != null){
            candidates = candidates.stream()
                    .map(v -> transformer.apply(v))
                    .collect(Collectors.toSet());
        }
        if(!preds.isEmpty()){
            candidates = candidates.stream()
                    .filter(preds.stream()
                            .reduce(t -> true, Predicate::and))
                    .collect(Collectors.toSet());
        }
        return candidates;
    }

    @Override
    public T createInstance() {
        if(normalCases == null){
            Random r = new Random();
            int index = r.nextInt(cornerCases.size()-1);
            return new ArrayList<>(cornerCases).get(index); // TODO: 14/09/2017 THis is not pretty
        } else {
            return normalCases.get();
        }
    }

    @Override
    public ClassCreator<T> withPredicates(Predicate<T> predicate) {
        this.preds.add(predicate);
        return this;
    }

    @Override
    public ClassCreator<T> withTransformer(Function<T, T> transformer) {
        this.transformer = transformer;
        return this;
    }

}
