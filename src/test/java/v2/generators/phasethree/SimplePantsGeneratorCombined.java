package v2.generators.phasethree;

import example.phaseThree.Pants;
import v2.generators.CombinedCaseGenerator;
import v2.generators.CornerCaseGenerator;
import v2.generators.NormalCaseGenerator;

public class SimplePantsGeneratorCombined extends CombinedCaseGenerator<Pants> {
    public SimplePantsGeneratorCombined() {
        super(Pants.class);
    }
}
