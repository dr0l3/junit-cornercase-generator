package generators;

import example.Person;
import generator.CombinedGenerator;

public class PlainPersonGenerator extends CombinedGenerator<Person> {
    public PlainPersonGenerator() {
        super(Person.class);
    }
}
