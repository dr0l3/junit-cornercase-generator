package v2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Utils {
    public static  <T> boolean isPrimitive(Class<T> clazz){
        if(clazz.isPrimitive() ||
                clazz.equals(Integer.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Double.class)){
            return true;
        } else
            return false;
    }

    public static <T> boolean testPredicates(List<Predicate<T>> preds, T candidate){
        return preds.stream().allMatch(p -> p.test(candidate));
    }

    public static <T> T getNextValue(List<Predicate<T>> preds, Function<T,T> transformer, Supplier<T> supplier, int MAX_ATTEMPTS){
        T candidate = getNextCandidate(supplier,transformer);
        boolean passes = Utils.testPredicates(preds,candidate);
        int attempts = 0;
        while(!passes){
            attempts++;
            if(attempts > MAX_ATTEMPTS){
                throw new RuntimeException("Unable to create instance of " + supplier.getClass() + " made "+ MAX_ATTEMPTS+" attempts with no result");
                // TODO: 15/09/2017 Fix this??
            }
            candidate = getNextCandidate(supplier,transformer);
            passes = Utils.testPredicates(preds,candidate);
        }
        return candidate;
    }

    private static  <T> T getNextCandidate(Supplier<T> supplier, Function<T,T> transformer){
        T candidate = supplier.get();
        if(transformer != null){
            candidate = transformer.apply(candidate);
        }
        return candidate;
    }
}
