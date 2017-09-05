import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.Person;
import generators.PersonGenerator;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(JUnitQuickcheck.class)
public class TestTest {

//    @Property
//    public void cont(@From(PersonGenerator.class) Person p1, @From(PersonGenerator.class) Person p2){
//        assertTrue((p1.getAge() + p2.getAge()) >= 0);
//
//    }

        @Property
    public void cont(@From(PersonGenerator.class) Person p1){
        assertTrue((p1.getAge() + p1.getAge()) >= 0);

    }
}
