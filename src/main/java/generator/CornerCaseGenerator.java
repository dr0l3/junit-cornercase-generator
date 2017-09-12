package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.CornerCasePrimitiveCreator;

import java.util.ArrayList;
import java.util.List;

public abstract class CornerCaseGenerator<T> extends Generator<T> {
    private InstantiatorCornerCase cornerCaseCreator;
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;

    protected CornerCaseGenerator(Class<T> type) {
        super(type);
        this.type = type;
    }

    public CornerCaseGenerator<T> withCornerCaseCreator(InstantiatorCornerCase creator){
        this.cornerCaseCreator = creator;
        return this;
    }

    public void initializeOrNothing(){
        if(this.cornerCaseCreator == null){
            this.cornerCaseCreator = new InstantiatorCornerCase(new CornerCasePrimitiveCreator());
        }

        if(this.cornerCases == null){
            this.cornerCases = new ArrayList<>(cornerCaseCreator.createCornerCasesForClass(type, null));
            this.iterator = 0;
        }
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        initializeOrNothing();
        T res = cornerCases.get(iterator%cornerCases.size());
        this.iterator = this.iterator+1;
        return res;
    }

    public int numberOfCases(){
        initializeOrNothing();
        return this.cornerCases.size();
    }

    public InstantiatorCornerCase getCornerCaseCreator() {
        return cornerCaseCreator;
    }

    public void setCornerCaseCreator(InstantiatorCornerCase cornerCaseCreator) {
        this.cornerCaseCreator = cornerCaseCreator;
    }
}
