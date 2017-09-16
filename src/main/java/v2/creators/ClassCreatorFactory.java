package v2.creators;

import java.util.Set;
import java.util.function.Supplier;

public class ClassCreatorFactory {
    public static <T> ClassCreator<T> fromSet(Set<T> source, Class<T> clazz){
        SimpleClassCreator<T> ret = new SimpleClassCreator<>(clazz);
        ret.setCornerCases(source);
        return ret;
    }

    public static <T> ClassCreatorSI<T> fromSupplier(Supplier<T> source, Class<T> clazz){
        return new SimpleClassCreatorSI<>(source, clazz);
    }
}
