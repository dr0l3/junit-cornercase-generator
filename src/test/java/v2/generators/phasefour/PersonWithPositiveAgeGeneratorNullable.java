package v2.generators.phasefour;

import example.phasefour.Person;
import v2.creators.SimpleCCPrimCreator;
import v2.creators.SimpleNormalPrimCreator;
import v2.generators.CombinedCaseGenerator;

public class PersonWithPositiveAgeGeneratorNullable extends CombinedCaseGenerator<Person> {
    public PersonWithPositiveAgeGeneratorNullable() {
        super(Person.class);
        withDefaultPrimitiveCreator(SimpleCCPrimCreator.nonNegative());
        withDefaultPrimitiveCreator(SimpleNormalPrimCreator.nonNeg());
    }
}
