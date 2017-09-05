package generators;

import example.Person;
import generator.CornerCaseGenerator;
import generator.InstanceGenerator;
import instantiator.DefaultPrimitiveInstantiator;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;

public class PersonGenerator extends CornerCaseGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
        InstanceGenerator gen = new InstanceGenerator();
        gen.addClassInstStrategy(new ColorInstantiator(), Color.class);
        DefaultPrimitiveInstantiator p = new DefaultPrimitiveInstantiator();
        p.setIntVals(new HashSet<>(Arrays.asList(0,1,100)));
        gen.addPrimStratInClass(Person.class, p);
        setInstanceGenerator(gen);
    }
}
