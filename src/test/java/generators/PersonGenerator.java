package generators;

import example.Person;
import generator.CornerCaseGenerator;
import generator.InstanceCreatorCornerCase;
import instantiator.CornerCasePrimitiveInstantiator;
import instantiator.InstantiationStrategy;
import instantiator.Instantiators;
import instantiator.PrimitiveInstantiator;

import java.awt.*;
import java.util.Arrays;

public class PersonGenerator extends CornerCaseGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
        InstantiationStrategy<Color> colorsStrat = Instantiators.createInstantiator(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED));
        PrimitiveInstantiator nonNegInts = CornerCasePrimitiveInstantiator.withInts(Arrays.asList(0,1,100));
        InstanceCreatorCornerCase gen = InstanceCreatorCornerCase.defaultCornerCase()
                .withClassInstStrategy(colorsStrat,Color.class)
                .withPrimStratInClass(Person.class,nonNegInts);

        setCornerCaseCreator(gen);
    }
}
