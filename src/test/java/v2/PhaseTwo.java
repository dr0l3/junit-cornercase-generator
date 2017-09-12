package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseOne.SimplePojoString;
import example.phaseOne.SimplePrimitivePojo;
import example.phaseTwo.Person;
import org.junit.runner.RunWith;
import v2.generators.phaseone.*;
import v2.generators.phasetwo.SimplePersonGeneratorCombined;
import v2.generators.phasetwo.SimplePersonGeneratorCorner;
import v2.generators.phasetwo.SimplePersonGeneratorNormal;

@RunWith(JUnitQuickcheck.class)
public class PhaseTwo {
    //simple pojo with strings and primitive wrappers, all the types of generators
    @Property
    public void canGeneratePojoWithNoConfigCorner(@From(SimplePersonGeneratorCorner.class) Person pojo){

    }

    @Property
    public void canGeneratePojoWithNoConfigNormal(@From(SimplePersonGeneratorNormal.class) Person pojo){

    }

    @Property
    public void canGeneratePojoWithNoConfigCombined(@From(SimplePersonGeneratorCombined.class) Person pojo){

    }
}
