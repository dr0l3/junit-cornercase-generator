package v2.creators;

import java.util.Set;
import java.util.function.Supplier;

public class ClassCreatorFactory {
    public static <T> ClassCreator<T> fromSet(Set<T> source){
        SimpleClassCreator<T> ret = new SimpleClassCreator<>();
        ret.setCornerCases(source);
        return ret;
    }

    public static <T> ClassCreatorSI<T> fromSupplier(Supplier<T> source){
        return new SimpleClassCreatorSI<>(source);
    }
}
