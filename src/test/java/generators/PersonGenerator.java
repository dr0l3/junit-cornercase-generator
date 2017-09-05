package generators;

import example.Person;
import generator.CornerCaseGenerator;
import generator.InstanceGenerator;

import java.awt.*;

public class PersonGenerator extends CornerCaseGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
        InstanceGenerator gen = new InstanceGenerator();
        gen.addClassInstStrategy(new ColorInstantiator(), Color.class);
        setInstanceGenerator(gen);
    }
}
