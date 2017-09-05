package generator;


import instantiator.*;
import org.javatuples.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InstanceGenerator {
    private Map<Class, InstantiationStrategy> classInstMap;
    private Map<Pair<Class, Class>,InstantiationStrategy> classInClassInstMap;
    private Map<Pair<Class, String>,InstantiationStrategy> fieldInClassInstMap;
    private Map<Class, PrimitiveInstantiator> primitiveInClassInstMap;
    private Map<Class, Boolean> allowNullMap;
    private Set<Class> visited;

    public InstanceGenerator() {
        classInstMap = new HashMap<>();
        classInClassInstMap = new HashMap<>();
        fieldInClassInstMap = new HashMap<>();
        primitiveInClassInstMap = new HashMap<>();
        allowNullMap = new HashMap<>();
        visited = new HashSet<>();
    }


    public <T,U> Set<T> createCornerCasesForClass(Class<T> clazz, Class<U> parent){
        if(visited.contains(clazz)){
            return new HashSet<>();
        }
        System.out.println("Creating corner clases for class: " + clazz.toString());
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
            visited.add(clazz);
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
            Set<?> res = handlePrimitiveInteger(field,clazz);
            return res;
        } else if (type.equals(Integer.class)){
            Set<?> res = handlePrimitiveInteger(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Long.TYPE)) {
            return handlePrimitiveLong(field, clazz);
        } else if(type.equals(Long.class)) {
            Set<?> res = handlePrimitiveLong(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Double.TYPE)) {
            return handlePrimitiveDouble(field, clazz);
        } else if(type.equals(Double.class)) {
            Set<?> res = handlePrimitiveDouble(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if(type.equals(Float.TYPE)) {
            return handlePrimitiveFloat(field, clazz);
        } else if(type.equals(Float.class)) {
            Set<?> res = handlePrimitiveFloat(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Boolean.TYPE)){
            return handlePrimitiveBool(field, clazz);
        } else if (type.equals(Boolean.class)){
            Set<?> res = handlePrimitiveBool(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Byte.TYPE)){
            return handlePrimitiveByte(field, clazz);
        } else if (type.equals(Byte.class)){
            Set<?> res = handlePrimitiveByte(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if (type.equals(Short.TYPE)){
            return handlePrimitiveShort(field, clazz);
        } else if (type.equals(Short.class)){
            Set<?> res = handlePrimitiveShort(field,clazz);
            if(allowNullMap.getOrDefault(clazz,true)){
                res.add(null);
            }
            return res;
        } else if(type.equals(String.class)) {
            return new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), "", null));
        } else if(type.equals(List.class)){
            HashSet<List<?>> res = new HashSet<>();
            res.add(null);
            res.add(new ArrayList<>());
            return res;
        } else if(type.equals(Set.class)){
            HashSet<Set<?>> res = new HashSet<>();
            res.add(null);
            res.add(new HashSet<>());
            return res;
        } else if(type.equals(Map.class)){
            HashSet<Map<?,?>> res = new HashSet<>();
            res.add(null);
            res.add(new HashMap<>());
            return res;
        }

        try {
            Set<?> cornerCasesForComplexType = createCornerCasesForClass(type, clazz);
            cornerCasesForComplexType.add(null);
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getShortVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getByteVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getBoolVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getFloatVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getDoubleVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getLongVals();
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

        return primitiveInClassInstMap.getOrDefault(clazz, new DefaultPrimitiveInstantiator()).getIntVals();
    }

    public <T> void addClassInstStrategy(InstantiationStrategy<T> strategy, Class<T> clazz){
        this.classInstMap.put(clazz,strategy);
    }

    public Map<Class, InstantiationStrategy> getClassInstMap() {
        return classInstMap;
    }

    public void setClassInstMap(Map<Class, InstantiationStrategy> classInstMap) {
        this.classInstMap = classInstMap;
    }

    public Map<Pair<Class, Class>, InstantiationStrategy> getClassInClassInstMap() {
        return classInClassInstMap;
    }

    public void setClassInClassInstMap(Map<Pair<Class, Class>, InstantiationStrategy> classInClassInstMap) {
        this.classInClassInstMap = classInClassInstMap;
    }

    public Map<Pair<Class, String>, InstantiationStrategy> getFieldInClassInstMap() {
        return fieldInClassInstMap;
    }

    public void setFieldInClassInstMap(Map<Pair<Class, String>, InstantiationStrategy> fieldInClassInstMap) {
        this.fieldInClassInstMap = fieldInClassInstMap;
    }
}
