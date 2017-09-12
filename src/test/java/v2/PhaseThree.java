package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseThree.PantsContent;
import org.junit.runner.RunWith;
import v2.generators.phasethree.SimplePantsContentGeneratorCombined;
import v2.generators.phasethree.SimplePantsContentGeneratorCorner;
import v2.generators.phasethree.SimplePantsContentGeneratorNormal;

@RunWith(JUnitQuickcheck.class)
public class PhaseThree {
    //collections, interfaces and abstract classes
    @Property
    public void pantsContentsCornerCasesNoConfig(@From(SimplePantsContentGeneratorCorner.class)PantsContent content){
        System.out.println(content.displayContents());
    }

    @Property
    public void pantsContentCombinedCasesNoConfig(@From(SimplePantsContentGeneratorCombined.class)PantsContent content){
        System.out.println(content.displayContents());
    }

    @Property
    public void pantsContentsNormalCasesNoConfig(@From(SimplePantsContentGeneratorNormal.class)PantsContent content){
        System.out.println(content);
    }
}
