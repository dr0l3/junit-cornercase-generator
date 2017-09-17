package v2.generators;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.Path;
import v2.creators.ClassCreatorSI;
import v2.creators.PrimitiveCreatorSI;
import v2.instantiators.InstantiatorNormal;
import v2.instantiators.SimpleNormalCaseInstantiator;

public abstract class NormalCaseGenerator<T> extends Generator<T> implements
        PrimitiveCreatorSIConfigurator<NormalCaseGenerator<T>>,
        PrimitiveCreatorSIPerClassConfigurator<NormalCaseGenerator<T>>,
        FieldCreatorSIConfigurator<NormalCaseGenerator<T>>,
        ClassCreatorSIConfigurator<NormalCaseGenerator<T>>
{
    private Class<T> type;
    private InstantiatorNormal instantiatorNormal = new SimpleNormalCaseInstantiator();

    public NormalCaseGenerator(Class<T> clazz){
        super(clazz);
        this.type = clazz;
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
        return instantiatorNormal.createInstance(type,random, Path.empty());
    }

    private void initializeOrNothing(){
        if(this.instantiatorNormal== null){
            this.instantiatorNormal = new SimpleNormalCaseInstantiator();
        }
    }

    @Override
    public NormalCaseGenerator<T> withDefaultPrimitiveCreator(PrimitiveCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withDefaultPrimitiveCreator(creator);
        return this;
    }

    @Override
    public <U> NormalCaseGenerator<T> withPrimitiveCreatorSIForClass(Class<U> clazz, PrimitiveCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withPrimitiveCreatorSIForClass(clazz,creator);
        return this;
    }

    @Override
    public <U, V> NormalCaseGenerator<T> withCreatorForField(Class<U> parentClass, String name, Class<V> fieldClass, ClassCreatorSI creator) {
        this.instantiatorNormal = instantiatorNormal.withCreatorForField(parentClass,name,fieldClass,creator);
        return this;
    }

    @Override
    public <U> NormalCaseGenerator<T> withCreatorForClass(Class<U> clazz, ClassCreatorSI<U> creator) {
        this.instantiatorNormal = instantiatorNormal.withCreatorForClass(clazz,creator);
        return this;
    }
}
