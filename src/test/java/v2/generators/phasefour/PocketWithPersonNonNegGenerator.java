package v2.generators.phasefour;

import com.pholser.junit.quickcheck.generator.Gen;
import example.phasefour.Person;
import example.phasefour.Pocket;
import v2.creators.SimpleCCPrimCreator;
import v2.creators.SimpleNormalPrimCreator;
import v2.generators.CombinedCaseGenerator;

public class PocketWithPersonNonNegGenerator extends CombinedCaseGenerator<Pocket> {
    public PocketWithPersonNonNegGenerator() {
        super(Pocket.class);
        withPrimitiveCreatorForClass(Person.class, SimpleCCPrimCreator.nonNegative());
        withPrimitiveCreatorSIForClass(Person.class, SimpleNormalPrimCreator.nonNeg());
        withDefaultPrimitiveCreator(new SimpleNormalPrimCreator());
        withNullable(false);
    }
}
