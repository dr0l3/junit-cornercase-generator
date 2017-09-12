package v2.generators.phasetwo;

import example.phaseTwo.Person;
import v2.generators.CombinedCaseGenerator;
import v2.generators.CornerCaseGenerator;

public class SimplePersonGeneratorCorner extends CornerCaseGenerator<Person> {
    public SimplePersonGeneratorCorner() {
        super(Person.class);
    }
}
