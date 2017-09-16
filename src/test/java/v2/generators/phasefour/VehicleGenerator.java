package v2.generators.phasefour;

import com.google.common.collect.Sets;
import example.phasefour.Vehicle;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorSI;
import v2.creators.SimpleClassCreator;
import v2.generators.CombinedCaseGenerator;

public class VehicleGenerator extends CombinedCaseGenerator<Vehicle> {
    public VehicleGenerator() {
        super(Vehicle.class);
        SimpleClassCreator<Integer> wheels = new SimpleClassCreator<>(Integer.class);
        wheels.setCornerCases(Sets.newHashSet(2,4,6,20));
        withCreatorForField(Vehicle.class,"wheels", Integer.class, (ClassCreator)wheels);
        withCreatorForField(Vehicle.class,"wheels", Integer.class, (ClassCreatorSI)wheels);
        withNullable(false);
    }
}
