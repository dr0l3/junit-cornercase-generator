package v2.generators.phasefour;

import com.google.common.collect.Sets;
import example.phasefour.Vehicle;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorFactory;
import v2.creators.ClassCreatorSI;
import v2.generators.CombinedCaseGenerator;

import java.util.Random;

public class VehiclePredicateGenerator extends CombinedCaseGenerator<Vehicle> {
    public VehiclePredicateGenerator() {
        super(Vehicle.class);
        ClassCreatorSI<Integer> wheelGenerator = ClassCreatorFactory.fromSupplier(() -> new Random().nextInt(100), Integer.class)
                .withPredicates(i -> i % 2 == 0);
        ClassCreator<Integer> wheelCornerCases = ClassCreatorFactory.fromSet(Sets.newHashSet(0,100), Integer.class);
        withCreatorForField(Vehicle.class,"wheels",Integer.class, wheelGenerator);
        withCreatorForField(Vehicle.class,"wheels",Integer.class, wheelCornerCases);
    }
}
