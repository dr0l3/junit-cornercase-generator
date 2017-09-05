package instantiator;

import java.util.Set;

public class DefaultPrimitiveInstantiator implements PrimitiveInstantiator {
    private Set<Boolean> boolVals = Constants.boolVals;
    private Set<Byte> byteVals = Constants.byteVals;
    private Set<Short> shortVals = Constants.shortVals;
    private Set<Integer> intVals = Constants.intVals;
    private Set<Long> longVals = Constants.longVals;
    private Set<Float> floatVals = Constants.floatVals;
    private Set<Double> doubleVals = Constants.doubleVals;

    public Set<Boolean> getBoolVals() {
        return boolVals;
    }

    public void setBoolVals(Set<Boolean> boolVals) {
        this.boolVals = boolVals;
    }

    public Set<Byte> getByteVals() {
        return byteVals;
    }

    public void setByteVals(Set<Byte> byteVals) {
        this.byteVals = byteVals;
    }

    public Set<Short> getShortVals() {
        return shortVals;
    }

    public void setShortVals(Set<Short> shortVals) {
        this.shortVals = shortVals;
    }

    public Set<Integer> getIntVals() {
        return intVals;
    }

    public void setIntVals(Set<Integer> intVals) {
        this.intVals = intVals;
    }

    public Set<Long> getLongVals() {
        return longVals;
    }

    public void setLongVals(Set<Long> longVals) {
        this.longVals = longVals;
    }

    public Set<Float> getFloatVals() {
        return floatVals;
    }

    public void setFloatVals(Set<Float> floatVals) {
        this.floatVals = floatVals;
    }

    public Set<Double> getDoubleVals() {
        return doubleVals;
    }

    public void setDoubleVals(Set<Double> doubleVals) {
        this.doubleVals = doubleVals;
    }
}
