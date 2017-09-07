import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Mode;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.Orderline;
import example.Person;
import generators.PersonGenerator;
import generators.PlainOrderlineGenerator;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(JUnitQuickcheck.class)
public class TestTest {

//    @Property
//    public void cont(@From(PersonGenerator.class) Person p1, @From(PersonGenerator.class) Person p2){
//        assertTrue((p1.getAge() + p2.getAge()) >= 0);
//
//    }


    @Property
    public void cont(@From(PersonGenerator.class) Person p1) {
        System.out.println("In calss");
        System.out.println(p1);
        assertTrue((p1.getAge() + p1.getAge()) >= Integer.MIN_VALUE);
    }

    @Property(trials = 7, mode = Mode.EXHAUSTIVE)
    public void exhaust(@From(PersonGenerator.class) Person p1, @From(PersonGenerator.class) Person p2){
        System.out.println(String.format("%s %s", p1.toString(), p2.toString()));
        assertTrue((p1.getAge() + p2.getAge()) > -1);
    }

    @Property
    public void orderlines(@From(PlainOrderlineGenerator.class) Orderline o1){
        System.out.println(o1);
    }
}
