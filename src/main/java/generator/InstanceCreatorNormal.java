package generator;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.EmptySetStrategy;
import instantiator.InstantiationStrategy;
import instantiator.NormalCasePrimitiveInstantiator;
import instantiator.PrimitiveInstantiator;
import org.javatuples.Pair;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstanceCreatorNormal extends Creator {
    private Random random;
    private SourceOfRandomness sourceOfRandomness;


    public static InstanceCreatorNormal defaultNormalCase(SourceOfRandomness randomness){
        InstanceCreatorNormal creator = new InstanceCreatorNormal();
        creator.setSourceOfRandomness(randomness);
        creator.setEmptyDefaults();
        creator.setDefaultPrimitiveInstantiator(new NormalCasePrimitiveInstantiator(randomness));
        creator.setAllowNull(false);
        return creator;
    }

    public static InstanceCreatorNormal defaultNormalCase(){
        InstanceCreatorNormal creator = new InstanceCreatorNormal();
        SourceOfRandomness r = new SourceOfRandomness(new Random());
        creator.setSourceOfRandomness(r);
        creator.setEmptyDefaults();
        creator.setDefaultPrimitiveInstantiator(new NormalCasePrimitiveInstantiator(r));
        creator.setAllowNull(false);
        return creator;
    }

    public <T> Object createSingleInstanceOfField(Field field, Class<T> clazz, SourceOfRandomness randomness){
        this.sourceOfRandomness = randomness;
        Class<?> type = field.getType();
        field.getGenericType();
        if(type.isEnum()) {
            int index = getEffectiveRandom().nextInt(0,type.getEnumConstants().length-1);
            return type.getEnumConstants()[index];
        } else if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return handlePrimitiveInt(field,clazz);
        } else if(type.equals(Long.TYPE) || type.equals(Long.class)) {
            return handlePrimitiveLong(field, clazz);
        } else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
            return handlePrimitiveDouble(field, clazz);
        } else if(type.equals(Float.TYPE) || type.equals(Float.class)) {
            return handlePrimitiveFloat(field, clazz);
        } else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)){
            return handlePrimitiveBool(field, clazz);
        } else if (type.equals(Byte.TYPE) || type.equals(Byte.class)){
            return handlePrimitiveByte(field, clazz);
        } else if (type.equals(Short.TYPE) || type.equals(Short.class)){
            return handlePrimitiveShort(field, clazz);
        } else if(type.equals(String.class)) {
            return java.util.UUID.randomUUID().toString();
        } else if(type.equals(List.class)){
            return new ArrayList<>(); // TODO: 07/09/2017 Fix this
        } else if(type.equals(Set.class)){
            return new HashSet<>(); // TODO: 07/09/2017 Fix this
        } else if(type.equals(Map.class)){
            return new HashMap<>(); // TODO: 07/09/2017 Fix this
        }

        try {
            return createSingleInstanceOfClass(type, randomness);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("cant find instance for field" + field.toString());
        return null;
    }

    public <T> T createSingleInstanceOfClass(Class<T>  clazz, SourceOfRandomness randomness){
        if(visiting.contains(clazz)){
            return null; // TODO: 07/09/2017 What to do here?
        }
        visiting.add(clazz);
        try {
            boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
            if(!zeroArgConstructors){
                InstantiationStrategy<T> strategy = classInstMap.getOrDefault(clazz,new EmptySetStrategy<>(clazz));

                List<T> possibles = new ArrayList<>(strategy.createFrom());
                Collections.shuffle(possibles);
                visiting.remove(clazz);
                return possibles.get(0); // TODO: 07/09/2017 No possibles?
            }

            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
            List<Field> filtered = fields.stream()
                    .filter(field -> !field.isAccessible())
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());


            Map<Field,?> valuesByField = filtered.stream()
                    .map(field -> Pair.with(field, createSingleInstanceOfField(field, clazz,randomness)))
                    .collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

            T inst = clazz.newInstance();
            for (Map.Entry<Field, ?> entry: valuesByField.entrySet()){

                Field field = entry.getKey();
                field.setAccessible(true);
                field.set(inst,entry.getValue());
                field.setAccessible(false);
            }

            visiting.remove(clazz);
            return inst;
        } catch (Exception e){
            e.printStackTrace();
            visiting.remove(clazz);
            return null; // TODO: 07/09/2017 WHat to do here?
        }
    }

    private <T> Object handlePrimitiveByte(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getByteVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getByteVals().iterator().next();
    }

    private <T> Object handlePrimitiveBool(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getBoolVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getBoolVals().iterator().next();
    }

    private <T> Object handlePrimitiveShort(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getShortVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getShortVals().iterator().next();
    }

    private <T> Object handlePrimitiveLong(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getLongVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getLongVals().iterator().next();
    }

    private <T> Object handlePrimitiveFloat(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getFloatVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getFloatVals().iterator().next();
    }

    private <T> Object handlePrimitiveDouble(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getDoubleVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getDoubleVals().iterator().next();
    }
    private <T> Object handlePrimitiveInt(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            List<?> options = new ArrayList<>(fieldInClassInstMap.get(com).createFrom());
            Collections.shuffle(options);
            return options.get(0);
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            List<?> options = new ArrayList<>(primitiveInClassInstMap.get(clazz).getIntVals());
            Collections.shuffle(options);
            return options.get(0);
        }

        return defaultPrimitiveInstantiator.getIntVals().iterator().next();
    }

    public <T> InstanceCreatorNormal withClassInstStrategy(InstantiationStrategy<T> strategy, Class<T> clazz){
        this.classInstMap.put(clazz,strategy);
        return this;
    }

    public <T> InstanceCreatorNormal withPrimStratInClass(Class<T> clazz, PrimitiveInstantiator primStrat){
        this.primitiveInClassInstMap.put(clazz,primStrat);
        return this;
    }

    public InstanceCreatorNormal withDefaultPrimStrat(PrimitiveInstantiator primStrat){
        setDefaultPrimitiveInstantiator(primStrat);
        return this;
    }

    private SourceOfRandomness getEffectiveRandom(){
        if(this.sourceOfRandomness != null){
            return this.sourceOfRandomness;
        } else {
            this.sourceOfRandomness = new SourceOfRandomness(new Random());
            return sourceOfRandomness;
        }
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public SourceOfRandomness getSourceOfRandomness() {
        return sourceOfRandomness;
    }

    public void setSourceOfRandomness(SourceOfRandomness sourceOfRandomness) {
        this.sourceOfRandomness = sourceOfRandomness;
    }

}
