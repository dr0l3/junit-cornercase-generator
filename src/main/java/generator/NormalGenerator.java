package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.CreationStrategy;

import java.util.List;

public abstract class NormalGenerator<T> extends Generator<T> {
    private InstantiatorNormal normalCaseCreator;
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;

    protected NormalGenerator(Class<T> type) {
        super(type);
        this.type = type;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        if(this.normalCaseCreator == null){
            this.normalCaseCreator = InstantiatorNormal.defaultNormalCase(random);
        }

        return normalCaseCreator.createSingleInstanceOfClass(type, random);
    }


    public NormalGenerator<T> withNormalCaseCreator(InstantiatorNormal creator){
        this.normalCaseCreator = creator;
        return this;
    }

    public NormalGenerator<T> withClassInstStat(CreationStrategy<T> strat, Class<T> clazz) {
        this.normalCaseCreator = normalCaseCreator.withClassCreationStrategy(strat,clazz);
        return this;
    }
}
