package v2.generators.phasetwo;

import com.google.common.collect.Sets;
import example.phaseTwo.Person;
import v2.creators.ClassCreator;
import v2.creators.ClassCreatorFactory;
import v2.generators.CombinedCaseGenerator;
import v2.generators.CornerCaseGenerator;
import v2.generators.NormalCaseGenerator;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class SimplePersonGeneratorNormal extends NormalCaseGenerator<Person> {
    public SimplePersonGeneratorNormal() {
        super(Person.class);
        ClassCreator<Color> cc = ClassCreatorFactory.fromSet(Sets.newHashSet(Color.BLACK,Color.RED, Color.BLUE), Color.class);
        ClassCreator<LocalDate> ldc= ClassCreatorFactory.fromSet(Sets.newHashSet(LocalDate.now()), LocalDate.class);
        ClassCreator<Date> dc = ClassCreatorFactory.fromSet(Sets.newHashSet(Date.from(Instant.now())), Date.class);
        withCreatorForClass(Color.class,cc);
        withCreatorForClass(LocalDate.class,ldc);
        withCreatorForClass(Date.class, dc);
    }
}
