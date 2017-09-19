package v2.creators;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.Set;

public interface PrimitiveCreatorSI {
    Boolean nextBool(SourceOfRandomness randomness);
    Byte nextByte(SourceOfRandomness randomness);
    Short nextShort(SourceOfRandomness randomness);
    Integer nextInt(SourceOfRandomness randomness);
    Long nextLong(SourceOfRandomness randomness);
    Float nextFloat(SourceOfRandomness randomness);
    Double nextDouble(SourceOfRandomness randomness);
    <T> T getValueForType(Class<T> clazz, SourceOfRandomness randomness);
    <T> T getArrayValuesForType(Class<T> clazz, SourceOfRandomness randomness);
}
