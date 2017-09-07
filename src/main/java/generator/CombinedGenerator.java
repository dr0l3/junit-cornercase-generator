package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.InstantiationStrategy;
import instantiator.PrimitiveInstantiator;

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
        this.cornerCaseCreator = InstanceCreatorCornerCase.defaultCornerCase();
        this.normalCaseCreator = InstanceCreatorNormal.defaultNormalCase();
    }

    public CombinedGenerator<T> withCornerCaseCreator(InstanceCreatorCornerCase creator){
        this.cornerCaseCreator = creator;
        return this;
    }

    public CombinedGenerator<T> withNormalCaseCreator(InstanceCreatorNormal creator){
        this.normalCaseCreator = creator;
        return this;
    }

    public <U> CombinedGenerator<T> withClassInstStat(InstantiationStrategy<U> strat, Class<U> clazz) {
        this.cornerCaseCreator = cornerCaseCreator.withClassInstStrategy(strat,clazz);
        this.normalCaseCreator = normalCaseCreator.withClassInstStrategy(strat,clazz);
        return this;
    }

    public CombinedGenerator<T> withPrimStar(PrimitiveInstantiator primStar){
        this.cornerCaseCreator = cornerCaseCreator.withDefaultPrimStrat(primStar);
        this.normalCaseCreator = normalCaseCreator.withDefaultPrimStrat(primStar);
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
        return normalCaseCreator.createSingleInstanceOfClass(type, random);
    }

    public InstanceCreatorCornerCase getCornerCaseCreator() {
        return cornerCaseCreator;
    }

    public void setCornerCaseCreator(InstanceCreatorCornerCase cornerCaseCreator) {
        this.cornerCaseCreator = cornerCaseCreator;
    }

    public void setNormalCaseCreator(InstanceCreatorNormal normalCaseCreator) {
        this.normalCaseCreator = normalCaseCreator;
    }
}
