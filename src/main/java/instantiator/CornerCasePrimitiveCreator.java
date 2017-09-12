package instantiator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CornerCasePrimitiveCreator implements PrimitiveCreator {
    private Set<Boolean> boolVals = Constants.boolVals;
    private Set<Byte> byteVals = Constants.byteVals;
    private Set<Short> shortVals = Constants.shortVals;
    private Set<Integer> intVals = Constants.intVals;
    private Set<Long> longVals = Constants.longVals;
    private Set<Float> floatVals = Constants.floatVals;
    private Set<Double> doubleVals = Constants.doubleVals;
    private List<Predicate<? super Number>> constraints;

//    @Override
//    public PrimitiveCreator withConstraint(Predicate<? super Number> predicate) {
//        return null;
//    }

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

    public static PrimitiveCreator nonNegative(){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setByteVals(Constants.byteVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setShortVals(Constants.shortVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setLongVals(Constants.longVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setIntVals(Constants.intVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        inst.setDoubleVals(Constants.doubleVals.stream().filter(b -> b >= 0).collect(Collectors.toSet()));
        return inst;
    }

    public static PrimitiveCreator fromPredicate(Predicate<? super Number> predicate){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setByteVals(Constants.byteVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setShortVals(Constants.shortVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setLongVals(Constants.longVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setIntVals(Constants.intVals.stream().filter(predicate).collect(Collectors.toSet()));
        inst.setDoubleVals(Constants.doubleVals.stream().filter(predicate).collect(Collectors.toSet()));
        return inst;
    }

    public static PrimitiveCreator withBools(Collection<Boolean> booleans){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setBoolVals(new HashSet<>(booleans));
        return inst;
    }

    public static PrimitiveCreator withBytes(Collection<Byte> bytes){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setByteVals(new HashSet<>(bytes));
        return inst;
    }

    public static PrimitiveCreator withShorts(Collection<Short> shorts){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setShortVals(new HashSet<>(shorts));
        return inst;
    }

    public static PrimitiveCreator withInts(Collection<Integer> ints){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setIntVals(new HashSet<>(ints));
        return inst;
    }

    public static PrimitiveCreator withLongs(Collection<Long> longs){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setLongVals(new HashSet<>(longs));
        return inst;
    }

    public static PrimitiveCreator withFloats(Collection<Float> floats){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setFloatVals(new HashSet<>(floats));
        return inst;
    }

    public static PrimitiveCreator withDoubles(Collection<Double> doubles){
        CornerCasePrimitiveCreator inst = new CornerCasePrimitiveCreator();
        inst.setDoubleVals(new HashSet<>(doubles));
        return inst;
    }

    public PrimitiveCreator andBools(Collection<Boolean> bools){
        this.setBoolVals(new HashSet<>(bools));
        return this;
    }
    public PrimitiveCreator andBytes(Collection<Byte> bytes){
        this.setByteVals(new HashSet<>(bytes));
        return this;
    }

    public PrimitiveCreator andShorts(Collection<Short> shots){
        this.setShortVals(new HashSet<>(shots));
        return this;
    }

    public PrimitiveCreator andInst(Collection<Integer> ints){
        this.setIntVals(new HashSet<>(ints));
        return this;
    }

    public PrimitiveCreator andLongs(Collection<Long> longs){
        this.setLongVals(new HashSet<>(longs));
        return this;
    }

    public PrimitiveCreator andFloats(Collection<Float> floats){
        this.setFloatVals(new HashSet<>(floats));
        return this;
    }

    public PrimitiveCreator andDoubles(Collection<Double> doubles){
        this.setDoubleVals(new HashSet<>(doubles));
        return this;
    }
}
