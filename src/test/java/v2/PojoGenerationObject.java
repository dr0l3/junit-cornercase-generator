package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseTwo.Person;
import example.phaseTwo.Socks;
import example.phaseTwo.Underpants;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.generators.phasetwo.SimplePersonGeneratorCombined;
import v2.generators.phasetwo.SimplePersonGeneratorCorner;
import v2.generators.phasetwo.SimplePersonGeneratorNormal;
import v2.util.Util;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitQuickcheck.class)
public class PojoGenerationObject {
    //simple pojo with strings and primitive wrappers, all the types of generators
    @Property
    public void canGeneratePojoWithNoConfigCorner(@From(SimplePersonGeneratorCorner.class) Person pojo) {

    }

    @Test
    public void allCornerCasesHitCorner() {
        SimplePersonGeneratorCorner gen = new SimplePersonGeneratorCorner();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<Person> generated = List.range(0, 100)
                .map(irrelevant -> gen.generate(pair.getValue0(), pair.getValue1()));
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 0, 1);

        List<Integer> ages = generated.map(Person::getAge);
        List<Color> colorCC = List.of(Color.BLACK, Color.RED, Color.BLUE);
        List<Color> colors = generated
                .map(v -> Try.of(() -> v.getSocks().stream().map(Socks::getColor)))
                .filter(Try::isSuccess)
                .map(Try::get)
                .flatMap(List::ofAll);
        List<Try<java.util.List<Socks>>> attSocks = generated.map(v -> Try.of(() -> new ArrayList<>(v.getSocks())));
        List<LocalDate> localDates = attSocks
                .filter(Try::isSuccess)
                .map(Try::get)
                .flatMap(List::ofAll)
                .map(Socks::getLastWashed);
        List<LocalDate> localDateCC = List.of(LocalDate.now());
        List<Try<java.util.List<Underpants>>> attUnderpants = generated.map(v -> Try.of(() -> new ArrayList<>(v.getUnderpants())));
        assertTrue("All int cases are present", ages.containsAll(intCC));
        assertTrue("Underpants was null", attUnderpants.exists(Try::isFailure));
        assertTrue("Socks was null", attSocks.exists(Try::isFailure));
        assertTrue("All localdates", localDates.containsAll(localDateCC));
        assertTrue("All colors", colors.containsAll(colorCC));
    }


    @Property
    public void canGeneratePojoWithNoConfigNormal(@From(SimplePersonGeneratorNormal.class) Person pojo) {

    }

    @Property
    public void canGeneratePojoWithNoConfigCombined(@From(SimplePersonGeneratorCombined.class) Person pojo) {

    }

    @Test
    public void allCornerCasesHitCombined() {
        SimplePersonGeneratorCombined gen = new SimplePersonGeneratorCombined();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<Person> generated = List.range(0, 100)
                .map(irrelevant -> gen.generate(pair.getValue0(), pair.getValue1()));
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 0, 1);

        List<Integer> ages = generated.map(Person::getAge);
        List<Color> colorCC = List.of(Color.BLACK, Color.RED, Color.BLUE);
        List<Color> colors = generated
                .map(v -> Try.of(() -> v.getSocks().stream().map(Socks::getColor)))
                .filter(Try::isSuccess)
                .map(Try::get)
                .flatMap(List::ofAll);
        List<Try<java.util.List<Socks>>> attSocks = generated.map(v -> Try.of(() -> new ArrayList<>(v.getSocks())));
        List<LocalDate> localDates = attSocks
                .filter(Try::isSuccess)
                .map(Try::get)
                .flatMap(List::ofAll)
                .map(Socks::getLastWashed);
        List<LocalDate> localDateCC = List.of(LocalDate.now());
        List<Try<java.util.List<Underpants>>> attUnderpants = generated.map(v -> Try.of(() -> new ArrayList<>(v.getUnderpants())));
        assertTrue("All int cases are present", ages.containsAll(intCC));
        assertTrue("Underpants was null", attUnderpants.exists(Try::isFailure));
        assertTrue("Socks was null", attSocks.exists(Try::isFailure));
        assertTrue("All localdates", localDates.containsAll(localDateCC));
        assertTrue("All colors", colors.containsAll(colorCC));
    }
}
