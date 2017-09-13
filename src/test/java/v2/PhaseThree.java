package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseThree.Pants;
import example.phaseThree.PantsContent;
import org.junit.runner.RunWith;
import v2.generators.phasethree.SimplePantsContentGeneratorCombined;
import v2.generators.phasethree.SimplePantsContentGeneratorCorner;
import v2.generators.phasethree.SimplePantsContentGeneratorNormal;
import v2.generators.phasethree.SimplePantsGeneratorCorner;

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

    @Property()
    public void pantsContentsNormalCasesNoConfig(@From(SimplePantsContentGeneratorNormal.class)PantsContent content){
        System.out.println(content);
        System.out.println("-------------------------------------------------------------------");
    }

    @Property
    public void pantsCornerCaseWithNoConfig(@From(SimplePantsGeneratorCorner.class)Pants pants){
        System.out.println(pants);
    }
}
