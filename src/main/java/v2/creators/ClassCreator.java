package v2.creators;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ClassCreator<T> extends ClassCreatorSI<T>{
    Set<T> createCornerCases();
    ClassCreator<T> withPredicates(Predicate<T> predicate); //overrides method in ClassCreatorSI as the little mark says
    ClassCreator<T> withTransformer(Function<T,T> transformer);
}
