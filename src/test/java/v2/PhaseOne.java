package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseOne.SimplePrimitivePojo;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.generators.phaseone.SimplePojoGeneratorCombined;
import v2.generators.phaseone.SimplePojoGeneratorCorner;
import v2.generators.phaseone.SimplePojoGeneratorNormal;

@RunWith(JUnitQuickcheck.class)
public class PhaseOne {
    //Simple pojos, but combined, corner case only and normal case only
    @Property
    public void canGeneratePojoWithNoConfigCorner(@From(SimplePojoGeneratorCorner.class) SimplePrimitivePojo pojo){

    }

    @Property
    public void canGeneratePojoWithNoConfigNormal(@From(SimplePojoGeneratorNormal.class) SimplePrimitivePojo pojo){

    }

    @Property
    public void canGeneratePojoWithNoConfigCombined(@From(SimplePojoGeneratorCombined.class) SimplePrimitivePojo pojo){

    }
}
