package generator;


import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import instantiator.*;
import org.javatuples.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstanceCreatorCornerCase extends Creator{

    public static InstanceCreatorCornerCase defaultNormalCase(SourceOfRandomness randomness){
        InstanceCreatorCornerCase creator = new InstanceCreatorCornerCase();
        creator.setEmptyDefaults();
        creator.setDefaultPrimitiveInstantiator(new NormalCasePrimitiveInstantiator(randomness));
        creator.setAllowNull(false);
        return creator;
    }

    public static InstanceCreatorCornerCase defaultCornerCase(){
        InstanceCreatorCornerCase creator = new InstanceCreatorCornerCase();
        creator.setEmptyDefaults();
        creator.setDefaultPrimitiveInstantiator(new CornerCasePrimitiveInstantiator());
        creator.setAllowNull(true);
        return creator;
    }

    public static InstanceCreatorCornerCase nonNegativeCornerCase(){
        InstanceCreatorCornerCase creator = new InstanceCreatorCornerCase();
        creator.setEmptyDefaults();
        creator.setDefaultPrimitiveInstantiator(CornerCasePrimitiveInstantiator.nonNegative());
        creator.setAllowNull(true);
        return creator;
    }

    private InstanceCreatorCornerCase() {
        super();
    }

    public InstanceCreatorCornerCase(PrimitiveInstantiator defaultPrimitiveInstantiator) {
        setEmptyDefaults();
        this.defaultPrimitiveInstantiator = defaultPrimitiveInstantiator;
    }

    public <T,U> Set<T> createCornerCasesForClass(Class<T> clazz, Class<U> parent){
        if(visiting.contains(clazz)){
            return new HashSet<>();
        }
        visiting.add(clazz);
//        System.out.println("Creating corner clases for class: " + clazz.toString());
        try {
            boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
            if(!zeroArgConstructors){
                InstantiationStrategy<T> strategy = classInstMap.getOrDefault(clazz,new EmptySetStrategy(clazz));

                return strategy.createFrom();
            }

            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
            List<Field> filtered = fields.stream()
                    .filter(field -> !field.isAccessible())
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());


            Map<Field,Set<?>> valuesByField = filtered.stream()
                    .map(field -> Pair.with(field, new HashSet<>(createCornerCasesForField(field, clazz))))
                    .collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));


            int complexity = valuesByField.entrySet().stream()
                    .map(entry -> entry.getValue().size())
                    .reduce(0, Math::max);
            Set<T> res = new HashSet<>();
            for (int i = 0; i < complexity; i++) {
                T inst = clazz.newInstance();
                for (Map.Entry<Field, Set<?>> entry: valuesByField.entrySet()){

                    Field field = entry.getKey();
                    field.setAccessible(true);
                    List<?> values = Arrays.asList(entry.getValue().toArray());
                    Object value = values.get((i%values.size()));
                    field.set(inst,value);
                    field.setAccessible(false);
                }
                res.add(inst);
            }
            return res;
        } catch (Exception e){
            e.printStackTrace();
            return Collections.emptySet();
        }
    }


    public <T> Set<?> createCornerCasesForField(Field field, Class<T> clazz){
        Class<?> type = field.getType();
        field.getGenericType();
        if(type.isEnum()) {
            return new HashSet<>(Arrays.asList(type.getEnumConstants()));
        } else if(type.equals(Integer.TYPE)) {
            return handlePrimitiveInteger(field,clazz);
        } else if (type.equals(Integer.class)){
            Set<?> res = handlePrimitiveInteger(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Long.TYPE)) {
            return handlePrimitiveLong(field, clazz);
        } else if(type.equals(Long.class)) {
            Set<?> res = handlePrimitiveLong(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Double.TYPE)) {
            return handlePrimitiveDouble(field, clazz);
        } else if(type.equals(Double.class)) {
            Set<?> res = handlePrimitiveDouble(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Float.TYPE)) {
            return handlePrimitiveFloat(field, clazz);
        } else if(type.equals(Float.class)) {
            Set<?> res = handlePrimitiveFloat(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Boolean.TYPE)){
            return handlePrimitiveBool(field, clazz);
        } else if (type.equals(Boolean.class)){
            Set<?> res = handlePrimitiveBool(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Byte.TYPE)){
            return handlePrimitiveByte(field, clazz);
        } else if (type.equals(Byte.class)){
            Set<?> res = handlePrimitiveByte(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Short.TYPE)){
            return handlePrimitiveShort(field, clazz);
        } else if (type.equals(Short.class)){
            Set<?> res = handlePrimitiveShort(field,clazz);
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(type.equals(String.class)) {
            HashSet<String> res = new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), ""));
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(type.equals(List.class)){
            HashSet<List<?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new ArrayList<>());
            return res;
        } else if(type.equals(Set.class)){
            HashSet<Set<?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new HashSet<>());
            return res;
        } else if(type.equals(Map.class)){
            HashSet<Map<?,?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new HashMap<>());
            return res;
        }

        try {
            Set<?> cornerCasesForComplexType = createCornerCasesForClass(type, clazz);
            if(allowNullMap.getOrDefault(clazz, allowNull)){
                cornerCasesForComplexType.add(null);
            }
            return cornerCasesForComplexType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private <T> Set<?> handlePrimitiveShort(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getShortVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getShortVals();
    }

    private <T> Set<?> handlePrimitiveByte(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getByteVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getByteVals();
    }

    private <T> Set<?> handlePrimitiveBool(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getBoolVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getBoolVals();
    }

    private <T> Set<?> handlePrimitiveFloat(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getFloatVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getFloatVals();
    }

    private <T> Set<?> handlePrimitiveDouble(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getDoubleVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getDoubleVals();
    }

    private <T> Set<?> handlePrimitiveLong(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getLongVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator).getLongVals();
    }

    private <T> Set<?> handlePrimitiveInteger(Field field, Class<T> clazz) {
        String fieldName = field.getName();
        Pair<Class,String> com = Pair.with(clazz,fieldName);
        if(fieldInClassInstMap.containsKey(com)){
            return fieldInClassInstMap.get(com).createFrom();
        }
        if(primitiveInClassInstMap.containsKey(clazz)){
            return primitiveInClassInstMap.get(clazz).getIntVals();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveInstantiator)
                .getIntVals();
    }

    public <T> InstanceCreatorCornerCase withClassInstStrategy(InstantiationStrategy<T> strategy, Class<T> clazz){
        this.classInstMap.put(clazz,strategy);
        return this;
    }

    public <T> InstanceCreatorCornerCase withPrimStratInClass(Class<T> clazz, PrimitiveInstantiator primStrat){
        this.primitiveInClassInstMap.put(clazz,primStrat);
        return this;
    }

    public InstanceCreatorCornerCase withDefaultPrimStrat(PrimitiveInstantiator primStrat){
        setDefaultPrimitiveInstantiator(primStrat);
        return this;
    }

    public InstanceCreatorCornerCase withAllowNull(boolean allowNull){
        setAllowNull(allowNull);
        return this;
    }
}
