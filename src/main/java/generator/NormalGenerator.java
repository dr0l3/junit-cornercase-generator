package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.InstantiationStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class NormalGenerator<T> extends Generator<T> {
    private InstanceCreatorNormal normalCaseCreator;
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
            this.normalCaseCreator = InstanceCreatorNormal.defaultNormalCase(random);
        }

        return normalCaseCreator.createSingleInstanceOfClass(type, random);
    }


    public NormalGenerator<T> withNormalCaseCreator(InstanceCreatorNormal creator){
        this.normalCaseCreator = creator;
        return this;
    }

    public NormalGenerator<T> withClassInstStat(InstantiationStrategy<T> strat, Class<T> clazz) {
        this.normalCaseCreator = normalCaseCreator.withClassInstStrategy(strat,clazz);
        return this;
    }
}
