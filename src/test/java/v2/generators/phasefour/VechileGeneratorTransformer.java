package v2.generators.phasefour;

import com.google.common.collect.Sets;
import example.phasefour.Vehicle;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorFactory;
import v2.creators.ClassCreatorSI;
import v2.generators.CombinedCaseGenerator;

import java.util.Random;

public class VechileGeneratorTransformer extends CombinedCaseGenerator<Vehicle> {
    public VechileGeneratorTransformer() {
        super(Vehicle.class);
        ClassCreatorSI<Integer> wheelGenerator = ClassCreatorFactory.fromSupplier(() -> new Random().nextInt(100))
                .withTransformer(i -> i*2);
        ClassCreator<Integer> wheelCornerCases = ClassCreatorFactory.fromSet(Sets.newHashSet(0,100));
        withCreatorForField(Vehicle.class,"wheels",Integer.class, wheelGenerator);
        withCreatorForField(Vehicle.class,"wheels",Integer.class, wheelCornerCases);
    }
}
