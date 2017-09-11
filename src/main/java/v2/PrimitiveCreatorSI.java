package v2;

import java.util.Set;

public interface PrimitiveCreatorSI extends PrimitiveCreator {
    Boolean nextBool();
    Byte nextByte();
    Short nextShort();
    Integer nextInt();
    Long nextLong();
    Float nextFloat();
    Double nextDouble();
}
