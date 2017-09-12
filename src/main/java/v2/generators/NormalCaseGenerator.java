package v2.generators;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.instantiators.InstantiatorNormal;
import v2.instantiators.SimpleCornerCaseInstantiator;
import v2.instantiators.SimpleNormalCaseInstantiator;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public abstract class NormalCaseGenerator<T> extends Generator<T> {
    private Class<T> type;
    private InstantiatorNormal instantiatorNormal;

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
        return instantiatorNormal.createInstance(type,random);
    }

    private void initializeOrNothing(){
        if(this.instantiatorNormal== null){
            this.instantiatorNormal = new SimpleNormalCaseInstantiator();
        }
    }
}
