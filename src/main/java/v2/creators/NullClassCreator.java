package v2.creators;

public class NullClassCreator<T> implements ClassCreatorSI<T>{

    @Override
    public T createInstance() {
        return null;
    }
}
