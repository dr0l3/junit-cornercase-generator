package v2.instantiators;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.ClassCreatorMap;
import v2.FieldCreatorMap;
import v2.Utils;
import v2.creators.ClassCreator;
import v2.creators.EmptySetClassCreator;
import v2.creators.PrimitiveCreator;
import v2.creators.SimpleCCPrimCreator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleCornerCaseInstantiator implements InstantiatorCornerCase {
    private Set<Class> visiting = Sets.newHashSet();
    private ClassCreatorMap classInstMap = new ClassCreatorMap();
    private boolean allowNull = true;
    private Map<Class,Boolean> allowNullMap = Maps.newConcurrentMap();
    //seems there is no way to verify that the class creator has the correct type
    private FieldCreatorMap fieldInClassInstMap = new FieldCreatorMap();
    private Map<Class, PrimitiveCreator> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreator defaultPrimitiveCreator = new SimpleCCPrimCreator();

    @Override
    public <T> Set<T> createCornerCasesForClass(Class<T> clazz) {
        if(visiting.contains(clazz)){
            return new HashSet<>();
        }
        visiting.add(clazz);
        System.out.println("Creating corner clases for class: " + clazz.toString());
        try {
            boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
            if(!zeroArgConstructors){
                ClassCreator<T> strategy = classInstMap.getOrDefault(clazz, new EmptySetClassCreator<>());

                return strategy.createCornerCases();
            }

            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
            List<Field> filtered = fields.stream()
                    .filter(field -> !field.isAccessible())
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());


            Map<Field,Set<?>> valuesByField = filtered.stream()
                    .map(field -> Pair.with(field, new HashSet<>(createCornerCasesForField(field, clazz, field.getType()))))
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



    public <T,U> Set<U> createCornerCasesForField(Field field, Class<T> clazz, Class<U> fieldType){
        if(fieldType.isEnum()) {
            return new HashSet<>(Arrays.asList(fieldType.getEnumConstants()));
        } else if(Utils.isPrimitive(fieldType)){
            return handlePrimitive(field,clazz,fieldType);
        } else if(fieldType.equals(String.class)) {
            HashSet<String> res = new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), ""));
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return (Set<U>) res;
        } else if(fieldType.equals(List.class)){
            HashSet<List<?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new ArrayList<>());
            return (Set<U>) res;
        } else if(fieldType.equals(Set.class)){
            HashSet<Set<?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new HashSet<>());
            return (Set<U>) res;
        } else if(fieldType.equals(Map.class)){
            HashSet<Map<?,?>> res = new HashSet<>();
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            res.add(new HashMap<>());
            return (Set<U>) res;
        }

        try {
            Set<U> cornerCasesForComplexType = createCornerCasesForClass(fieldType);
            if(allowNullMap.getOrDefault(clazz, allowNull)){
                cornerCasesForComplexType.add(null);
            }
            return cornerCasesForComplexType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptySet();
    }

    private <T,U> Set<U> handlePrimitive(Field field, Class<T> clazz, Class<U> fieldType) {
        String fieldName = field.getName();
        Triplet<Class<T>, String, Class<U>> triplet = Triplet.with(clazz,fieldName,fieldType);
        if (fieldInClassInstMap.containsKey(triplet)) {
            return fieldInClassInstMap.get(triplet).createCornerCases();
        }
        if (primitiveInClassInstMap.containsKey(clazz)) {
            return primitiveInClassInstMap.get(clazz).getValuesForType(fieldType);
        }

        Set<U> nonNullSet = primitiveInClassInstMap
                .getOrDefault(clazz, defaultPrimitiveCreator)
                .getValuesForType(fieldType);
        if (!fieldType.isPrimitive() && allowNullMap.getOrDefault(clazz, allowNull)) {

            Set<U> withNullSet = Sets.newHashSet(nonNullSet);
            withNullSet.add(null);
            return withNullSet;
        } else
            return nonNullSet;
    }
}
