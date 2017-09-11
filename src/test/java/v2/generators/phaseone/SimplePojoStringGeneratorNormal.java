package v2.generators.phaseone;

import example.phaseOne.SimplePojoString;
import example.phaseOne.SimplePrimitivePojo;
import generator.NormalGenerator;

public class SimplePojoStringGeneratorNormal extends NormalGenerator<SimplePojoString> {
    protected SimplePojoStringGeneratorNormal() {
        super(SimplePojoString.class);
    }
}
