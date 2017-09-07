package instantiator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CornerCasePrimitiveInstantiator implements PrimitiveInstantiator {
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

    private void setBoolVals(Set<Boolean> boolVals) {
        this.boolVals = boolVals;
    }

    public Set<Byte> getByteVals() {
        return byteVals;
    }

    private void setByteVals(Set<Byte> byteVals) {
        this.byteVals = byteVals;
    }

    public Set<Short> getShortVals() {
        return shortVals;
    }

    private void setShortVals(Set<Short> shortVals) {
        this.shortVals = shortVals;
    }

    public Set<Integer> getIntVals() {
        return intVals;
    }

    private void setIntVals(Set<Integer> intVals) {
        this.intVals = intVals;
    }

    public Set<Long> getLongVals() {
        return longVals;
    }

    private void setLongVals(Set<Long> longVals) {
        this.longVals = longVals;
    }

    public Set<Float> getFloatVals() {
        return floatVals;
    }

    private void setFloatVals(Set<Float> floatVals) {
        this.floatVals = floatVals;
    }

    public Set<Double> getDoubleVals() {
        return doubleVals;
    }

    private void setDoubleVals(Set<Double> doubleVals) {
        this.doubleVals = doubleVals;
    }

    public static PrimitiveInstantiator nonNegative(){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setByteVals(Constants.byteVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setShortVals(Constants.shortVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setLongVals(Constants.longVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setIntVals(Constants.intVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setDoubleVals(Constants.doubleVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        return inst;
    }

    public static PrimitiveInstantiator fromPredicate(Predicate<? super Number> predicate){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setByteVals(Constants.byteVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setShortVals(Constants.shortVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setLongVals(Constants.longVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setIntVals(Constants.intVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setDoubleVals(Constants.doubleVals.stream().filter(predicate).collect(Collectors.toSet()));
        return inst;
    }

    public static PrimitiveInstantiator withBools(Collection<Boolean> booleans){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setBoolVals(new HashSet<>(booleans));
        return inst;
    }

    public static PrimitiveInstantiator withBytes(Collection<Byte> bytes){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setByteVals(new HashSet<>(bytes));
        return inst;
    }

    public static PrimitiveInstantiator withShorts(Collection<Short> shorts){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setShortVals(new HashSet<>(shorts));
        return inst;
    }

    public static PrimitiveInstantiator withInts(Collection<Integer> ints){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setIntVals(new HashSet<>(ints));
        return inst;
    }

    public static PrimitiveInstantiator withLongs(Collection<Long> longs){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setLongVals(new HashSet<>(longs));
        return inst;
    }

    public static PrimitiveInstantiator withFloats(Collection<Float> floats){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setFloatVals(new HashSet<>(floats));
        return inst;
    }

    public static PrimitiveInstantiator withDoubles(Collection<Double> doubles){
        CornerCasePrimitiveInstantiator inst = new CornerCasePrimitiveInstantiator();
        inst.setDoubleVals(new HashSet<>(doubles));
        return inst;
    }

    public PrimitiveInstantiator andBools(Collection<Boolean> bools){
        this.setBoolVals(new HashSet<>(bools));
        return this;
    }
    public PrimitiveInstantiator andBytes(Collection<Byte> bytes){
        this.setByteVals(new HashSet<>(bytes));
        return this;
    }

    public PrimitiveInstantiator andShorts(Collection<Short> shots){
        this.setShortVals(new HashSet<>(shots));
        return this;
    }

    public PrimitiveInstantiator andInst(Collection<Integer> ints){
        this.setIntVals(new HashSet<>(ints));
        return this;
    }

    public PrimitiveInstantiator andLongs(Collection<Long> longs){
        this.setLongVals(new HashSet<>(longs));
        return this;
    }

    public PrimitiveInstantiator andFloats(Collection<Float> floats){
        this.setFloatVals(new HashSet<>(floats));
        return this;
    }

    public PrimitiveInstantiator andDoubles(Collection<Double> doubles){
        this.setDoubleVals(new HashSet<>(doubles));
        return this;
    }
}
