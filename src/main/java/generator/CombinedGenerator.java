package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.CreationStrategy;
import instantiator.PrimitiveCreator;

import java.util.ArrayList;
import java.util.List;

public abstract class CombinedGenerator<T> extends Generator<T> {
    private InstantiatorCornerCase cornerCaseCreator;
    private InstantiatorNormal normalCaseCreator;
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;

    protected CombinedGenerator(Class<T> type) {
        super(type);
        this.type = type;
        this.cornerCaseCreator = InstantiatorCornerCase.defaultCornerCase();
        this.normalCaseCreator = InstantiatorNormal.defaultNormalCase();
    }

    public CombinedGenerator<T> withCornerCaseCreator(InstantiatorCornerCase creator){
        this.cornerCaseCreator = creator;
        return this;
    }

    public CombinedGenerator<T> withNormalCaseCreator(InstantiatorNormal creator){
        this.normalCaseCreator = creator;
        return this;
    }

    public <U> CombinedGenerator<T> withClassInstStat(CreationStrategy<U> strat, Class<U> clazz) {
        this.cornerCaseCreator = cornerCaseCreator.withClassInstStrategy(strat,clazz);
        this.normalCaseCreator = normalCaseCreator.withClassCreationStrategy(strat,clazz);
        return this;
    }

    public CombinedGenerator<T> withPrimStar(PrimitiveCreator primStar){
        this.cornerCaseCreator = cornerCaseCreator.withDefaultPrimStrat(primStar);
        this.normalCaseCreator = normalCaseCreator.withPrimitiveCreationStrategy(primStar);
        return this;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        if(this.cornerCaseCreator == null){
            this.cornerCaseCreator = InstantiatorCornerCase.defaultCornerCase();
        }
        if(this.normalCaseCreator == null){
            this.normalCaseCreator = InstantiatorNormal.defaultNormalCase(random);
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

    public InstantiatorCornerCase getCornerCaseCreator() {
        return cornerCaseCreator;
    }

    public void setCornerCaseCreator(InstantiatorCornerCase cornerCaseCreator) {
        this.cornerCaseCreator = cornerCaseCreator;
    }

    public void setNormalCaseCreator(InstantiatorNormal normalCaseCreator) {
        this.normalCaseCreator = normalCaseCreator;
    }
}
