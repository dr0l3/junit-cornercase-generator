package v2;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import example.phaseFive.Cat;
import example.phaseTwo.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import v2.exceptions.NoImplementationException;
import v2.exceptions.RecursivePathException;
import v2.exceptions.UnknownClassException;
import v2.generators.phasefive.CatCombined;
import v2.generators.phasefive.PersonCombined;
import v2.generators.phasefive.RecursivePersonCombined;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitQuickcheck.class)
public class PhaseFive {
    //Error handling and propagation of errors

    /**
     * Possible problems
     *  - No instantiator for class
     *  - Recursion
     *  - Look through instantiators
     */

    //notes -> prefer crashing over "sensible" defaults.
    //Generally you wont be inspecting all the output (you should't need to at least).
    //It is thus important that if the specification is either incomplete or ambiguous that it is explicitly reported.
    @Test
    public void noInstantiator(){
        PersonCombined generator = new PersonCombined();
        SourceOfRandomness randomness = new SourceOfRandomness(new Random());
        GeometricDistribution dist = new GeometricDistribution();
        GenerationStatus status = new SimpleGenerationStatus(dist,randomness,0);

        //No generator for Color
        try{
            Person p = generator.generate(randomness,status);
        } catch (Exception e){
            assertThat("Exception type is UnknownClassException", e instanceof UnknownClassException);
        }
    }

    @Test
    public void recursiveHierarchy(){
        RecursivePersonCombined generator = new RecursivePersonCombined();
        SourceOfRandomness randomness = new SourceOfRandomness(new Random());
        GeometricDistribution dist = new GeometricDistribution();
        GenerationStatus status = new SimpleGenerationStatus(dist,randomness,0);

        //recursive definition
        try{
            example.phaseFive.Person p = generator.generate(randomness,status);
        } catch (Exception e){
            assertThat("Exception type is RecursivePathException", e instanceof RecursivePathException);
        }
    }

    @Test
    public void noImplementations(){
        CatCombined generator = new CatCombined();
        SourceOfRandomness randomness = new SourceOfRandomness(new Random());
        GeometricDistribution dist = new GeometricDistribution();
        GenerationStatus status = new SimpleGenerationStatus(dist,randomness,0);

        //no implementations
        try{
            Cat c = generator.generate(randomness,status);
        } catch (Exception e){
            assertThat("Exception type is NoImplementationException", e instanceof NoImplementationException);
        }
    }

}
