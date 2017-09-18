package v2.util;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;
import com.pholser.junit.quickcheck.internal.generator.SimpleGenerationStatus;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javatuples.Pair;

import java.util.Random;

public class Util {
    public static Pair<SourceOfRandomness,GenerationStatus> randomAndStatus() {
        SourceOfRandomness randomness = new SourceOfRandomness(new Random());
        GeometricDistribution dist = new GeometricDistribution();
        GenerationStatus status = new SimpleGenerationStatus(dist,randomness,0);
        return Pair.with(randomness, status);
    }
}
