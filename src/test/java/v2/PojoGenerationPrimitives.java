package v2;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseOne.SimplePrimitivePojo;
import io.vavr.collection.List;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.generators.phaseone.SimplePojoGeneratorCombined;
import v2.generators.phaseone.SimplePojoGeneratorCorner;
import v2.generators.phaseone.SimplePojoGeneratorNormal;
import v2.util.Util;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitQuickcheck.class)
public class PojoGenerationPrimitives {
    //Simple pojos, but combined, corner case only and normal case only
    @Property
    public void canGeneratePojoWithNoConfigCorner(@From(SimplePojoGeneratorCorner.class) SimplePrimitivePojo pojo){

    }

    @Test
    public void allCornerCasesAreCreated(){
        SimplePojoGeneratorCorner gen = new SimplePojoGeneratorCorner();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<SimplePrimitivePojo> generated = List.range(0,100)
                .map(irrelevant -> gen.generate(pair.getValue0(),pair.getValue1()));
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1,0,1);
        List<Byte> byteCC = List.of(Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.parseByte("0"), Byte.valueOf("-1"), Byte.valueOf("1"));
        List<Short> shortCC = List.of(Short.MIN_VALUE, Short.MAX_VALUE, (short) 0,(short) 1, (short) -1);
        List<Long> longCC = List.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L);
        List<Float> floatCC = List.of(Float.MAX_VALUE, Float.MIN_VALUE, 0F,1F, -1F);
        List<Double> doubleCC = List.of(Double.MIN_VALUE, Double.MAX_VALUE, 0D, 1D, -1D);
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getA).containsAll(intCC));
        assertTrue("All byte cases are present", generated.map(SimplePrimitivePojo::getB).containsAll(byteCC));
        assertTrue("All long cases are present", generated.map(SimplePrimitivePojo::getC).containsAll(longCC));
        assertTrue("All double cases are present", generated.map(SimplePrimitivePojo::getD).containsAll(doubleCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getE).containsAll(shortCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getF).containsAll(floatCC));
    }

    @Property
    public void canGeneratePojoWithNoConfigNormal(@From(SimplePojoGeneratorNormal.class) SimplePrimitivePojo pojo){

    }

    @Property
    public void canGeneratePojoWithNoConfigCombined(@From(SimplePojoGeneratorCombined.class) SimplePrimitivePojo pojo){

    }

    @Test
    public void allCornerCasesAreCreatedCombined(){
        SimplePojoGeneratorCombined gen = new SimplePojoGeneratorCombined();
        Pair<SourceOfRandomness, GenerationStatus> pair = Util.randomAndStatus();
        List<SimplePrimitivePojo> generated = List.range(0,100)
                .map(irrelevant -> gen.generate(pair.getValue0(),pair.getValue1()));
        List<Integer> intCC = List.of(Integer.MIN_VALUE, Integer.MAX_VALUE, -1,0,1);
        List<Byte> byteCC = List.of(Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.parseByte("0"), Byte.valueOf("-1"), Byte.valueOf("1"));
        List<Short> shortCC = List.of(Short.MIN_VALUE, Short.MAX_VALUE, (short) 0,(short) 1, (short) -1);
        List<Long> longCC = List.of(Long.MAX_VALUE, Long.MIN_VALUE, 0L, 1L, -1L);
        List<Float> floatCC = List.of(Float.MAX_VALUE, Float.MIN_VALUE, 0F,1F, -1F);
        List<Double> doubleCC = List.of(Double.MIN_VALUE, Double.MAX_VALUE, 0D, 1D, -1D);
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getA).containsAll(intCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getB).containsAll(byteCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getC).containsAll(longCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getD).containsAll(doubleCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getE).containsAll(shortCC));
        assertTrue("All int cases are present", generated.map(SimplePrimitivePojo::getF).containsAll(floatCC));
    }
}
