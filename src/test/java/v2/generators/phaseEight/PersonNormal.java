package v2.generators.phaseEight;

import example.phaseEight.Person;
import v2.generators.CornerCaseGenerator;
import v2.generators.NormalCaseGenerator;

public class PersonNormal extends NormalCaseGenerator<Person> {

    public PersonNormal() {
        super(Person.class);
    }
}
