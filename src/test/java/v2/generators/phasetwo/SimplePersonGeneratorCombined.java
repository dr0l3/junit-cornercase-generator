package v2.generators.phasetwo;

import example.phaseTwo.Person;
import v2.generators.CombinedCaseGenerator;

public class SimplePersonGeneratorCombined extends CombinedCaseGenerator<Person> {
    public SimplePersonGeneratorCombined() {
        super(Person.class);
    }
}
