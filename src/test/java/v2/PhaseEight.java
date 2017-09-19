package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseEight.Person;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.generators.phaseEight.PersonCombined;
import v2.generators.phaseEight.PersonCorner;
import v2.generators.phaseEight.PersonNormal;
import v2.generators.phasetwo.SimplePersonGeneratorCorner;
import v2.util.Util;

import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class PhaseEight {
    //Either other collection libraries or
    //Integration with specific collection libraries (integration would be annoying, need new one every time new library pops up...
    @Property
    public void cornerCase(@From(PersonCorner.class) Person person){
        System.out.println(person);
    }

    @Test
    public void cornerCasesAreExhaustive(){
        PersonCorner gen = new PersonCorner();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<Person> generated = List.range(0, gen.getComplexity())
                .map(irrelevant -> gen.generate(pair.getValue0(), pair.getValue1()));

        List<Integer[]> integerArrs = generated.map(Person::getIntegerArray);
        assertTrue("Contains null", integerArrs.contains(null));
        List<Integer> integers = integerArrs.filter(Objects::nonNull).flatMap(List::of);
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1,0,1);
        assertTrue("All int cornercases are represented", integers.containsAll(intCC));

        List<int[]> intArrs = generated.map(Person::getIntArray);
        assertTrue("Contains null", intArrs.contains(null));
        List<Integer> ints = integerArrs.filter(Objects::nonNull).flatMap(List::of);
        assertTrue("All int cornercases are represented", integers.containsAll(intCC));
    }

    @Property
    public void normalCase(@From(PersonNormal.class) Person person){
        System.out.println(person);
    }

    @Property
    public void combinedCase(@From(PersonCombined.class) Person person){
        System.out.println(person);
    }

    @Test
    public void combinedCasesAreExhaustive(){
        PersonCombined gen = new PersonCombined();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<Person> generated = List.range(0, 100)
                .map(irrelevant -> gen.generate(pair.getValue0(), pair.getValue1()));

        List<Integer[]> integerArrs = generated.map(Person::getIntegerArray);
        assertTrue("Contains null", integerArrs.contains(null));
        List<Integer> integers = integerArrs.filter(Objects::nonNull).flatMap(List::of);
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1,0,1);
        assertTrue("All int cornercases are represented", integers.containsAll(intCC));

        List<int[]> intArrs = generated.map(Person::getIntArray);
        assertTrue("Contains null", intArrs.contains(null));
        List<Integer> ints = integerArrs.filter(Objects::nonNull).flatMap(List::of);
        assertTrue("All int cornercases are represented", integers.containsAll(intCC));
    }
}
