package generators;

import example.Person;
import generator.CombinedGenerator;
import generator.InstantiatorCornerCase;
import generator.InstantiatorNormal;
import instantiator.*;

import java.awt.*;
import java.util.Arrays;

public class PersonGenerator extends CombinedGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
        CreationStrategy<Color> colorCreator = Creators.creatorFromValues(Arrays.asList(Color.BLACK, Color.WHITE, Color.RED));
        PrimitiveCreator nonNegInts = CornerCasePrimitiveCreator.withInts(Arrays.asList(0,1,100));
        InstantiatorCornerCase cornerGen = InstantiatorCornerCase.defaultCornerCase()
                .withClassInstStrategy(colorCreator,Color.class)
                .withPrimStratInClass(Person.class,nonNegInts);

        InstantiatorNormal normalGen = InstantiatorNormal.defaultNormalCase()
                .withPrimitiveCreationStrategy(nonNegInts);

        setCornerCaseCreator(cornerGen);
        setNormalCaseCreator(normalGen);
        withClassInstStat(colorCreator,Color.class);
    }
}
