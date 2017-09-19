package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phasefour.Person;
import example.phasefour.Pocket;
import example.phasefour.Vehicle;
import org.junit.runner.RunWith;
import v2.generators.phasefour.*;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitQuickcheck.class)
public class GeneratorConfiguration {
    //constraints and configuration
    /**
     * Configuration of instantiators
     *  - General Primitive Strategy
     *  - Primitive Strategy per class
     *  - Field Strategy
     * Configuration of Creators
     *  - Constraints
     *  - Transformers
     */

    //general primitive strategy
    @Property
    public void personWithNonnegativeAge(@From(PersonWithPositiveAgeGenerator.class) Person person){
        assertThat("nonnegative age",person.getAge() > -1);
        assertThat("nonnegative age",person.getAgeInMonths() > -1);
    }

    //general primitive strategy
    @Property
    public void personWithNonnegativeAgeWithNulls(@From(PersonWithPositiveAgeGeneratorNullable.class) Person person){
        assertThat("nonnegative age",person.getAge() > -1);
        if(person.getAgeInMonths() != null){
            assertThat("nonnegative age",person.getAgeInMonths() > -1);
        }
    }

    //primitive strategy for specific class
    @Property
    public void pocketWithPrimitiveStrategyPerClass(@From(PocketWithPersonNonNegGenerator.class) Pocket pocket){
        System.out.println(pocket);
        assertThat("nonnegative age",pocket.getOwner().getAge() > -1);
        assertThat("nonnegative age",pocket.getOwner().getAgeInMonths() > -1);
    }

    //field configuration
    @Property
    public void vehicleWithEvenNumberOfWheels(@From(VehicleGenerator.class) Vehicle vehicle){
        assertThat("even number of wheels",vehicle.getWheels() % 2 ==  0);
    }



    //predicates
    @Property
    public void vehicleWithPredicateNumberOfWheels(@From(VehiclePredicateGenerator.class) Vehicle vehicle){
        assertThat("event number of wheels",vehicle.getWheels() % 2 == 0);
    }

    @Property
    public void vehicleWithTransformerNumberOfWheels(@From(VechileGeneratorTransformer.class) Vehicle vehicle){
        assertThat("Even number of wheels", vehicle.getWheels() % 2 == 0);
    }

}