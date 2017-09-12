package v2.generators.phasetwo;

import example.phaseTwo.Person;
import v2.generators.CombinedCaseGenerator;
import v2.generators.CornerCaseGenerator;
import v2.generators.NormalCaseGenerator;

public class SimplePersonGeneratorNormal extends NormalCaseGenerator<Person> {
    public SimplePersonGeneratorNormal() {
        super(Person.class);
    }
}
