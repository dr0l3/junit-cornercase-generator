package v2.generators.phaseone;

import example.phaseOne.SimplePojoString;
import example.phaseOne.SimplePrimitivePojo;
import generator.CornerCaseGenerator;

public class SimplePojoStringGeneratorCorner extends CornerCaseGenerator<SimplePojoString> {
    protected SimplePojoStringGeneratorCorner() {
        super(SimplePojoString.class);
    }
}
