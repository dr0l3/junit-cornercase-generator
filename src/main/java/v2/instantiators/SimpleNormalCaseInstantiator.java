package v2.instantiators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.ClassCreatorMap;
import v2.ClassCreatorMapSI;
import v2.FieldCreatorMapSI;
import v2.Utils;
import v2.creators.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleNormalCaseInstantiator implements InstantiatorNormal {
    private Set<Class> visiting = Sets.newHashSet();
    private ClassCreatorMapSI classInstMap = new ClassCreatorMapSI();
    //seems there is no way to verify that the class creator has the correct type
    private FieldCreatorMapSI fieldInClassInstMap = new FieldCreatorMapSI();
    private Map<Class, PrimitiveCreatorSI> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreatorSI defaultPrimitiveCreator = new SimpleNormalPrimCreator(); // FIXME: 12/09/2017 INitialization

    private <T,U> U handlePrimitive(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness) {
        String fieldName = field.getName();
        Triplet<Class<T>, String, Class<U>> com = Triplet.with(clazz, fieldName,fieldType);
        if (fieldInClassInstMap.containsKey(com)) {
            return fieldInClassInstMap.get(com).createInstance();
        }
        if (primitiveInClassInstMap.containsKey(clazz)) {
            return primitiveInClassInstMap.get(clazz).getValueForType(fieldType, randomness);
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveCreator).getValueForType(fieldType, randomness);
    }


    @Override
    public <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness) {
        if(visiting.contains(clazz)){
            return null; // TODO: 12/09/2017 What to do here?
        }
        visiting.add(clazz);
        try {
            boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
            if(!zeroArgConstructors){
                ClassCreatorSI<T> strategy = classInstMap.getOrDefault(clazz, new NullClassCreator<>()); // TODO: 12/09/2017 What to do here?

                return strategy.createInstance();
            }

            List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
            List<Field> filtered = fields.stream()
                    .filter(field -> !field.isAccessible())
                    .filter(field -> !Modifier.isFinal(field.getModifiers()))
                    .filter(field -> !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());


            Map<Field,?> valuesByField = filtered.stream()
                    .map(field -> Pair.with(field, createCornerCasesForField(field, clazz, field.getType(), randomness)))
                    .collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

            T inst = clazz.newInstance();
            for (Map.Entry<Field, ?> entry: valuesByField.entrySet()){

                Field field = entry.getKey();
                field.setAccessible(true);
                Object value = entry.getValue();
                field.set(inst,value);
                field.setAccessible(false);
            }
            return inst;
        } catch (Exception e){
            e.printStackTrace();
            return null; // TODO: 12/09/2017 What to do here??
        }
    }

    public <T,U> U createCornerCasesForField(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness){
        if(fieldType.isEnum()) {
            List<U> possibles = Arrays.asList(fieldType.getEnumConstants());
            int index = randomness.nextInt(0,possibles.size()-1);
            return possibles.get(index);
        } else if(Utils.isPrimitive(fieldType)){
            return handlePrimitive(field,clazz,fieldType, randomness);
        } else if(fieldType.equals(String.class)) {
            return (U) UUID.randomUUID().toString();
        } else if(fieldType.equals(List.class)){
            return fieldType.cast(Lists.newArrayList()); // TODO: 12/09/2017 What to do ehre?
        } else if(fieldType.equals(Set.class)){
            return fieldType.cast(Sets.newHashSet()); // TODO: 12/09/2017 What to do here?
        } else if(fieldType.equals(Map.class)){
            return fieldType.cast(Maps.newHashMap()); // TODO: 12/09/2017 What to do here?
        }

        try {
            return createInstance(fieldType,randomness);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // TODO: 12/09/2017 What to do here??
    }
}
