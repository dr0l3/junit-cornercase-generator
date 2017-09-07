package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.InstantiationStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinedGenerator<T> extends Generator<T> {
    private InstanceCreatorCornerCase cornerCaseCreator;
    private InstanceCreatorNormal normalCaseCreator;
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;

    protected CombinedGenerator(Class<T> type) {
        super(type);
        this.type = type;
    }

    public CombinedGenerator<T> withCornerCaseCreator(InstanceCreatorCornerCase creator){
        this.cornerCaseCreator = creator;
        return this;
    }

    public CombinedGenerator<T> withNormalCaseCreator(InstanceCreatorNormal creator){
        this.normalCaseCreator = creator;
        return this;
    }

    public CombinedGenerator<T> withClassInstStat(InstantiationStrategy<T> strat, Class<T> clazz) {
        this.cornerCaseCreator = cornerCaseCreator.withClassInstStrategy(strat,clazz);
        this.normalCaseCreator = normalCaseCreator.withClassInstStrategy(strat,clazz);
        return this;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        if(this.cornerCaseCreator == null){
            this.cornerCaseCreator = InstanceCreatorCornerCase.defaultCornerCase();
        }
        if(this.normalCaseCreator == null){
            this.normalCaseCreator = InstanceCreatorNormal.defaultNormalCase(random);
        }

        if(this.cornerCases == null){
            this.cornerCases = new ArrayList<>(cornerCaseCreator.createCornerCasesForClass(type, null));
            this.iterator = 0;
        }


        if(iterator < cornerCases.size()){
            T res = cornerCases.get(iterator);
            this.iterator = this.iterator+1;
            return res;
        }
        return normalCaseCreator.createSingleInstanceOfClass(type);
    }

    public InstanceCreatorCornerCase getCornerCaseCreator() {
        return cornerCaseCreator;
    }

    public void setCornerCaseCreator(InstanceCreatorCornerCase cornerCaseCreator) {
        this.cornerCaseCreator = cornerCaseCreator;
    }
}
