package v2.generators;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorSI;
import v2.creators.PrimitiveCreator;
import v2.creators.PrimitiveCreatorSI;
import v2.instantiators.InstantiatorCornerCase;
import v2.instantiators.InstantiatorNormal;
import v2.instantiators.SimpleCornerCaseInstantiator;
import v2.instantiators.SimpleNormalCaseInstantiator;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinedCaseGenerator<T> extends Generator<T>implements
        PrimitiveCreatorConfigurator<CombinedCaseGenerator<T>>,
        PrimitiveCreatorSIConfigurator<CombinedCaseGenerator<T>>,
        NullableConfigurator<CombinedCaseGenerator<T>>,
        PrimitiveCreatorPerClassConfigurator<CombinedCaseGenerator<T>>,
        PrimitiveCreatorSIPerClassConfigurator<CombinedCaseGenerator<T>>,
        FieldCreatorConfigurator<CombinedCaseGenerator<T>>,
        FieldCreatorSIConfigurator<CombinedCaseGenerator<T>>,
        ClassCreatorConfigurator<CombinedCaseGenerator<T>>,
        ClassCreatorSIConfigurator<CombinedCaseGenerator<T>>{
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;
    private InstantiatorNormal instantiatorNormal = new SimpleNormalCaseInstantiator();
    private InstantiatorCornerCase instantiatorCornerCase = new SimpleCornerCaseInstantiator();

    public CombinedCaseGenerator(Class<T> type){
        super(type);
        this.type = type;
    }
    /**
     * Generates a value, possibly influenced by a source of randomness and
     * metadata about the generation.
     *
     * @param random source of randomness to be used when generating the value
     * @param status an object that can be used to influence the generated
     *               value. For example, generating lists can use the {@link
     *               GenerationStatus#size() size} method to generate lists with a given
     *               number of elements.
     * @return the generated value
     */
    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        initializeOrNothing();
        T res;
        if(iterator < cornerCases.size())
            res = cornerCases.get(iterator);
        else
            res = instantiatorNormal.createInstance(type,random);

        this.iterator = this.iterator+1;
        return res;
    }

    private void initializeOrNothing(){
        if(this.cornerCases == null){
            this.cornerCases = new ArrayList<>(instantiatorCornerCase.createCornerCasesForClass(type));
            this.iterator = 0;
        }
    }

    @Override
    public CombinedCaseGenerator<T> withDefaultPrimitiveCreator(PrimitiveCreator creator) {
        this.instantiatorCornerCase = instantiatorCornerCase.withDefaultPrimitiveCreator(creator);
        return this;
    }

    @Override
    public CombinedCaseGenerator<T> withDefaultPrimitiveCreator(PrimitiveCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withDefaultPrimitiveCreator(creator);
        return this;
    }

    @Override
    public CombinedCaseGenerator<T> withNullable(boolean nullable) {
        this.instantiatorCornerCase = instantiatorCornerCase.withNullable(nullable);
        return this;
    }

    @Override
    public <U> CombinedCaseGenerator<T> withPrimitiveCreatorForClass(Class<U> clazz, PrimitiveCreator creator) {
        this.instantiatorCornerCase = instantiatorCornerCase.withPrimitiveCreatorForClass(clazz,creator);
        return this;
    }

    @Override
    public <U> CombinedCaseGenerator<T> withPrimitiveCreatorSIForClass(Class<U> clazz, PrimitiveCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withPrimitiveCreatorSIForClass(clazz, creator);
        return this;
    }

    @Override
    public <U, V> CombinedCaseGenerator<T> withCreatorForField(Class<U> parentClass, String fieldName, Class<V> fieldClass, ClassCreator creator) {
        this.instantiatorCornerCase = instantiatorCornerCase.withCreatorForField(parentClass,fieldName,fieldClass,creator);
        return this;
    }

    @Override
    public <U, V> CombinedCaseGenerator<T> withCreatorForField(Class<U> parentClass, String fieldName, Class<V> fieldClass, ClassCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withCreatorForField(parentClass,fieldName,fieldClass,creator);
        return this;
    }

    @Override
    public <U> CombinedCaseGenerator<T> withCreatorForClass(Class<U> clazz, ClassCreator<U> creator) {
        this.instantiatorCornerCase = instantiatorCornerCase.withCreatorForClass(clazz,creator);
        this.instantiatorNormal = instantiatorNormal.withCreatorForClass(clazz,creator);
        return this;
    }

    @Override
    public <U> CombinedCaseGenerator<T> withCreatorForClass(Class<U> clazz, ClassCreatorSI<U> creator) {
        this.instantiatorNormal = instantiatorNormal.withCreatorForClass(clazz,creator);
        return this;
    }
}
