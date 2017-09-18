package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseEight.Person;
import org.junit.runner.RunWith;
import v2.generators.phaseEight.PersonCornerCase;

@RunWith(JUnitQuickcheck.class)
public class PhaseEight {
    //Either other collection libraries or
    //Integration with specific collection libraries (integration would be annoying, need new one every time new library pops up...
    @Property
    public void something(@From(PersonCornerCase.class) Person person){

    }
}
