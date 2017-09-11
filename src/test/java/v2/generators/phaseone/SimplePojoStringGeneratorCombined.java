package v2.generators.phaseone;

import example.phaseOne.SimplePojoString;
import example.phaseOne.SimplePrimitivePojo;
import generator.CombinedGenerator;

public class SimplePojoStringGeneratorCombined extends CombinedGenerator<SimplePojoString> {
    protected SimplePojoStringGeneratorCombined() {
        super(SimplePojoString.class);
    }
}
