package v2.generators.phaseone;

import example.phaseOne.SimplePrimitivePojo;
import generator.CornerCaseGenerator;
import generator.NormalGenerator;

public class SimplePojoGeneratorCorner extends CornerCaseGenerator<SimplePrimitivePojo> {
    protected SimplePojoGeneratorCorner() {
        super(SimplePrimitivePojo.class);
    }
}
