package generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;
import java.util.List;

public abstract class CornerCaseGenerator<T> extends Generator<T> {
    private InstanceGenerator instanceGenerator;
    private List<T> cornerCases;
    private int iterator;
    private Class<T> type;

    protected CornerCaseGenerator(Class<T> type) {
        super(type);
        this.type = type;
    }

    @Override
    public T generate(SourceOfRandomness random, GenerationStatus status) {
        if(this.instanceGenerator == null){
            this.instanceGenerator = new InstanceGenerator();
        }
        if(this.cornerCases == null){
            this.cornerCases = new ArrayList<>(instanceGenerator.createCornerCasesForClass(type, null));
            System.out.println("Cornercases for stuff");
            cornerCases.forEach(System.out::println);
            this.iterator = 0;
        }


        if(iterator < cornerCases.size()){
            T res = cornerCases.get(iterator);
            this.iterator = this.iterator+1;
            return res;
        }
        return null;
    }

    public InstanceGenerator getInstanceGenerator() {
        return instanceGenerator;
    }

    public void setInstanceGenerator(InstanceGenerator instanceGenerator) {
        this.instanceGenerator = instanceGenerator;
    }
}
