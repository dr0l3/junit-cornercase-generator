package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseThree.*;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.generators.phasethree.*;
import v2.util.Util;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitQuickcheck.class)
public class PhaseThree {
    //collections, interfaces and abstract classes
    @Property
    public void pantsContentsCornerCasesNoConfig(@From(SimplePantsContentGeneratorCorner.class) PantsContent content) {
    }

    @Test
    public void allSubClassesUsedCorner() {
        SimplePantsContentGeneratorCorner gen = new SimplePantsContentGeneratorCorner();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<PantsContent> generated = List.range(0, 100).map(i -> gen.generate(pair.getValue0(), pair.getValue1()));
        assertTrue("Contains dime", generated.exists(c -> c.getClass() == Dime.class));
        assertTrue("Contains IPhone", generated.exists(c -> c.getClass() == IPhone.class));
        assertTrue("Contains Samsung", generated.exists(c -> c.getClass() == Samsung.class));
    }

    @Property
    public void pantsContentCombinedCasesNoConfig(@From(SimplePantsContentGeneratorCombined.class) PantsContent content) {
    }

    @Test
    public void allSubClassesUsedCombined() {
        SimplePantsContentGeneratorCombined gen = new SimplePantsContentGeneratorCombined();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<PantsContent> generated = List.range(0, 100).map(i -> gen.generate(pair.getValue0(), pair.getValue1()));
        assertTrue("Contains dime", generated.exists(c -> c.getClass() == Dime.class));
        assertTrue("Contains IPhone", generated.exists(c -> c.getClass() == IPhone.class));
        assertTrue("Contains Samsung", generated.exists(c -> c.getClass() == Samsung.class));
    }

    @Property()
    public void pantsContentsNormalCasesNoConfig(@From(SimplePantsContentGeneratorNormal.class) PantsContent content) {

    }

    @Property()
    public void pantsCornerCaseWithNoConfig(@From(SimplePantsGeneratorCorner.class) Pants pants) {
    }

    @Test
    public void testNullCollectionEmptyCollectionAndNonEmptyCollectionGenerated() {
        SimplePantsGeneratorCorner gen = new SimplePantsGeneratorCorner();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<Pants> generated = List.range(0, 100).map(i -> gen.generate(pair.getValue0(), pair.getValue1()));
        List<Try<java.util.List<PantsContent>>> tryPantsContent = generated.map(v -> Try.of(() -> new ArrayList<>(v.getContents())));
        assertTrue("Null collection", tryPantsContent.exists(t -> t.isFailure() && (t.getCause() instanceof NullPointerException)));
        assertTrue("Empty collection", tryPantsContent.exists(t -> t.isSuccess() && t.get().isEmpty()));
        assertTrue("Non-Empty collection", tryPantsContent.exists(t -> t.isSuccess() && !t.get().isEmpty()));

        List<Try<java.util.List<PantsContent>>> tryPantsPhones = generated.map(v -> Try.of(() -> new ArrayList<>(v.getPhones())));
        assertTrue("Null collection", tryPantsPhones.exists(t -> t.isFailure() && (t.getCause() instanceof NullPointerException)));
        assertTrue("Empty collection", tryPantsPhones.exists(t -> t.isSuccess() && t.get().isEmpty()));
        assertTrue("Non-Empty collection", tryPantsPhones.exists(t -> t.isSuccess() && !t.get().isEmpty()));

        List<Try<java.util.List<Float>>> tryPantsRandomness = generated.map(v -> Try.of(() -> new ArrayList<>(v.getRandomNess().keySet())));
        assertTrue("Null collection", tryPantsRandomness.exists(t -> t.isFailure() && (t.getCause() instanceof NullPointerException)));
        assertTrue("Empty collection", tryPantsRandomness.exists(t -> t.isSuccess() && t.get().isEmpty()));
        assertTrue("Non-Empty collection", tryPantsRandomness.exists(t -> t.isSuccess() && !t.get().isEmpty()));
    }

    @Property
    public void pantsNormalCaseWithNoConfig(@From(SimplePantsGeneratorNormal.class) Pants pants) {
    }

    @Property
    public void pantsCombinedCaseWithNoConfig(@From(SimplePantsGeneratorCombined.class) Pants pants) {
    }
}
