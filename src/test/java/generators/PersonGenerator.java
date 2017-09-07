package generators;

import example.Person;
import generator.CombinedGenerator;
import generator.CornerCaseGenerator;
import generator.InstanceCreatorCornerCase;
import generator.InstanceCreatorNormal;
import instantiator.*;

import java.awt.*;
import java.util.Arrays;

public class PersonGenerator extends CombinedGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
        InstantiationStrategy<Color> colorsStrat = Instantiators.createInstantiator(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED));
        PrimitiveInstantiator nonNegInts = CornerCasePrimitiveInstantiator.withInts(Arrays.asList(0,1,100));
        InstanceCreatorCornerCase cornerGen = InstanceCreatorCornerCase.defaultCornerCase()
                .withClassInstStrategy(colorsStrat,Color.class)
                .withPrimStratInClass(Person.class,nonNegInts);

        InstanceCreatorNormal normalGen = InstanceCreatorNormal.defaultNormalCase()
                .withDefaultPrimStrat(NormalCasePrimitiveInstantiator.nonNeg());

        setCornerCaseCreator(cornerGen);
        setNormalCaseCreator(normalGen);
        withClassInstStat(colorsStrat,Color.class);


    }
}
