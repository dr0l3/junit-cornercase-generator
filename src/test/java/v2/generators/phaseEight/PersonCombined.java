package v2.generators.phaseEight;

import example.phaseEight.Person;
import v2.generators.CombinedCaseGenerator;
import v2.generators.CornerCaseGenerator;

public class PersonCombined extends CombinedCaseGenerator<Person> {

    public PersonCombined() {
        super(Person.class);
    }
}
