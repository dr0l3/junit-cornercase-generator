package v2.creators;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleClassCreator<T> implements ClassCreator<T> {
    private Set<T> cornerCases;
    private Supplier<T> normalCases;
    private List<Predicate<T>> preds = Lists.newArrayList();
    private Function<T,T> transformer;
    private Class<T> clazz;


    public SimpleClassCreator(Class<T> clazz) {
        this.clazz = clazz;
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
            Set<T> cases = cornerCases.stream().filter(Objects::nonNull).collect(Collectors.toSet());
            if(cases.size() > 1){
                Random r = new Random();
                int index = r.nextInt(cases.size()-1);
                return new ArrayList<>(cases).get(index); // TODO: 14/09/2017 THis is not pretty
            } else if(cases.size() == 1){
                return cases.iterator().next();
            } else {
                throw new RuntimeException("Trying to create instance of type "+ clazz +" with no candidates");
            }

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
