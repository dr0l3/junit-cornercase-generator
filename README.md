# junit-cornercase-generator


## Example

Suppose we have a simple Person POJO with the following fields

````java
public class Person {
    private String name;
    private int age;
}
````

Lets supposed we have some function that created these POJO's from json another function that validates that those POJO's make sense.
Testing this validation function quickly becomes cumbersome.
It would be neat to be able to express this as: "just try all combinations of person and verify that the results make sense".

That's where this library comes in!
 
Test.java
````java
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.Person;
import generators.PersonGenerator;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class MyTests {

    @Property
    public void myProperty(@From(PersonGenerator.class) Person p1) {
        if(isValidPerson(p1)){
            assertTrue(p1.getAge() >= 0);
            assertTrue(p1.getName().length() > 1);
        }
    }
}
````
PersonGenerator.java
````java
import example.Person;
import generator.CombinedGenerator;

public class PersonGenerator extends CombinedGenerator<Person> {
    public PersonGenerator(){
        super(Person.class);
    }
}
````

Of course a trivial example. Come up with a non-trivial example.
TODO: come up with a non-trivial example
ideas:
- Clothes swapper, verify that everyone has the same amount of clothes as they started out with, verify that noone has the same pants as they started out with.
- Wallet swapper, verify that everyone gets a new wallet, but that they have the same amount of money.
- Just dont crash: complex object with many nested items. does the algorithm crash?
    - Orderline, has amount and item. item has description as string. what happens with empty string?


````java
public class Person {
    private String name;
    private int age;
    private Jacket jacket;
}
````

Where jacket is another POJO

````java
public class Jacket {
    private Integer buttons;
    private Color color;
}
````

PersonGenerator.java
````java
import example.Person;
import generator.CombinedGenerator;
import generator.CornerCaseGenerator;
import generator.InstantiatorCornerCase;
import generator.InstantiatorNormal;
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
````

## Concepts

- Generator
    - Chooses mode of generation (corner case, normal, combined)
    - Holds information on instantiation
- Instantiator
    - types
        - Corner case instantiator
        - Normal instantiator
        - Combined instantiator
    - delegates to strategies
    - Instantiates a field 
- ValueCreator
    - types
        - PrimitiveCreator (also creates classes for wrappers for primitives)
        - StringCreator
        - CollectionCreator
        - ClassCreator, is really a generator
    - creates a concrete value of a specific type