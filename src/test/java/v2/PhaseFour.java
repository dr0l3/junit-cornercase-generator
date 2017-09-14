package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phasefour.Person;
import example.phasefour.Pocket;
import example.phasefour.Vehicle;
import org.junit.runner.RunWith;
import v2.generators.phasefour.PersonWithPositiveAgeGenerator;
import v2.generators.phasefour.PersonWithPositiveAgeGeneratorNullable;
import v2.generators.phasefour.PocketWithPersonNonNegGenerator;
import v2.generators.phasefour.VehicleGenerator;

import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnitQuickcheck.class)
public class PhaseFour {
    //constraints and configuration
    /**
     * Configuration
     *  - General Primitive Strategy
     *  - Primitive Strategy per class
     *  - Field Strategy
     * Constraints
     *  - yes...
     */

    @Property
    public void personWithNonnegativeAge(@From(PersonWithPositiveAgeGenerator.class) Person person){
        assertThat("nonnegative age",person.getAge() > -1);
        assertThat("nonnegative age",person.getAgeInMonths() > -1);
    }

    @Property
    public void personWithNonnegativeAgeWithNulls(@From(PersonWithPositiveAgeGeneratorNullable.class) Person person){
        assertThat("nonnegative age",person.getAge() > -1);
        if(person.getAgeInMonths() != null){
            assertThat("nonnegative age",person.getAgeInMonths() > -1);
        }
    }

    @Property
    public void pocketWithPrimitiveStrategyPerClass(@From(PocketWithPersonNonNegGenerator.class) Pocket pocket){
        System.out.println(pocket);
        assertThat("nonnegative age",pocket.getOwner().getAge() > -1);
        assertThat("nonnegative age",pocket.getOwner().getAgeInMonths() > -1);
    }

    @Property
    public void vehicleWithEventNumberofWheels(@From(VehicleGenerator.class)Vehicle vehicle){
        System.out.println(vehicle);
        assertThat("nonnegative age",vehicle.getWheels() % 2 ==  0);
    }
}
