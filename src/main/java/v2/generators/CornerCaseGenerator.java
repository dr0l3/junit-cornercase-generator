package v2.generators;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import v2.instantiators.InstantiatorCornerCase;
import v2.instantiators.SimpleCornerCaseInstantiator;

import java.util.ArrayList;
import java.util.List;

public abstract class CornerCaseGenerator<T> extends Generator<T> implements ComplexityAware {
    private List<T> cornerCases;
    private InstantiatorCornerCase instantiator;
    private int iterator;
    private Class type;

    public CornerCaseGenerator(Class<T> clazz){
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
        T res = cornerCases.get(iterator%cornerCases.size());
        this.iterator = this.iterator+1;
        return res;
    }

    @Override
    public int getComplexity() {
        initializeOrNothing();
        return cornerCases.size();
    }

    private void initializeOrNothing(){
        if(this.instantiator == null){
            this.instantiator = new SimpleCornerCaseInstantiator();
        }

        if(this.cornerCases == null){
            this.cornerCases = new ArrayList<>(instantiator.createCornerCasesForClass(type));
            this.iterator = 0;
        }
    }
}
