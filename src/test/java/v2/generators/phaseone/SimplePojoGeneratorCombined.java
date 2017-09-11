package v2.generators.phaseone;

import example.phaseOne.SimplePrimitivePojo;
import generator.CombinedGenerator;
import generator.NormalGenerator;

public class SimplePojoGeneratorCombined extends CombinedGenerator<SimplePrimitivePojo> {
    protected SimplePojoGeneratorCombined() {
        super(SimplePrimitivePojo.class);
    }
}
