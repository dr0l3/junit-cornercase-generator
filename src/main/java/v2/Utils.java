package v2;

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
}
