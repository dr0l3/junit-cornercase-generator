package v2.instantiators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import io.atlassian.fugue.Either;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.*;
import v2.creators.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleNormalCaseInstantiator implements InstantiatorNormal, ListCreatorSI, MapCreatorSI, SetCreatorSI {
    private Set<Class> visiting = Sets.newHashSet();
    private ClassCreatorMapSI classInstMap = new ClassCreatorMapSI();
    //seems there is no way to verify that the class creator has the correct type
    private FieldCreatorMapSI fieldInClassInstMap = new FieldCreatorMapSI();
    private Map<Class, PrimitiveCreatorSI> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreatorSI defaultPrimitiveCreator = new SimpleNormalPrimCreator();
    private Stack<Either<Class, Field>> path = new Stack<>();

    private <T, U> U handlePrimitive(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness) {
        String fieldName = field.getName();
        Triplet<Class<T>, String, Class<U>> com = Triplet.with(clazz, fieldName, fieldType);
        if (fieldInClassInstMap.containsKey(com)) {
            return fieldInClassInstMap.get(com).createInstance();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveCreator).getValueForType(fieldType, randomness);
    }


    @Override
    public <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness) {
        T res;
        if (classInstMap.containsKey(clazz)) {
            res = classInstMap.get(clazz).createInstance();
            return res;
        }

        if (visiting.contains(clazz)) {
            throw Utils.createRecursivePathException(clazz, path);
        }
        path.push(Either.left(clazz));
        visiting.add(clazz);

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return SubTypeMap.getImplementations(clazz)
                    .map(subs -> {
                        int index = randomness.nextInt(0, subs.size() - 1);
                        Class<? extends T> next = new ArrayList<>(subs).get(index);

                        T normalCase = createInstance(next, randomness);
                        visiting.remove(clazz);
                        path.pop();
                        return normalCase;})
                    .orElseThrow(() -> Utils.createNoimplementationsForClassException(clazz,path));
        }

        if (Utils.isPrimitive(clazz)) {
            visiting.remove(clazz);
            path.pop();
            return defaultPrimitiveCreator.getValueForType(clazz, randomness);
        }

        boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
        if (!zeroArgConstructors) {
            throw Utils.createUnknownClassException(clazz, path);
        }

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        List<Field> filtered = fields.stream()
                .filter(field -> !field.isAccessible())
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());


        Map<Field, ?> valuesByField = filtered.stream()
                .map(field -> Pair.with(field, createNormalCaseForField(field, clazz, field.getType(), randomness)))
                .collect(
                        Collectors.toMap(
                                Pair::getValue0,
                                Pair::getValue1));

        try {

            T inst = clazz.newInstance();
            for (Map.Entry<Field, ?> entry : valuesByField.entrySet()) {

                Field field = entry.getKey();
                field.setAccessible(true);
                Object value = entry.getValue();
                field.set(inst, value);
                field.setAccessible(false);
            }
            visiting.remove(clazz);
            path.pop();
            return inst;
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.createExceptionForPath(clazz, path, e);
        }
    }

    public <T, U> U createNormalCaseForField(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness) {
        U res;
        path.push(Either.right(field));
        if (fieldType.isEnum()) {
            List<U> possibles = Arrays.asList(fieldType.getEnumConstants());
            int index = randomness.nextInt(0, possibles.size() - 1);
            path.pop();
            return possibles.get(index);
        } else if (Utils.isPrimitive(fieldType)) {
            path.pop();
            return handlePrimitive(field, clazz, fieldType, randomness);
        } else if (fieldType.equals(String.class)) {
            path.pop();
            return (U) UUID.randomUUID().toString();
        } else if (fieldType.equals(List.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createList(genericParamValue, clazz, randomness);
            path.pop();
            return res;
        } else if (fieldType.equals(Set.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createSet(genericParamValue, clazz, randomness);
            path.pop();
            return res;
        } else if (fieldType.equals(Map.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (U) createMap(genericParamKey, genericParamValue, clazz, randomness);
            path.pop();
            return res;
        }

        res = createInstance(fieldType, randomness);
        path.pop();
        return res;
    }

    @Override
    public <T, U> List<T> createList(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        List<T> res = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            res.add(createInstance(clazz, randomness));
        }
        return res;
    }

    @Override
    public <T, U, V> Map<T, U> createMap(Class<T> keyClazz, Class<U> valueClazz, Class<V> parent, SourceOfRandomness randomness) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        Map<T, U> res = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            T key = createInstance(keyClazz, randomness);
            U value = createInstance(valueClazz, randomness);
            res.put(key, value);
        }
        return res;
    }

    @Override
    public <T, U> Set<T> createSet(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        Set<T> res = Sets.newHashSet();
        for (int i = 0; i < size; i++) {
            res.add(createInstance(clazz, randomness));
        }
        return res;
    }

    @Override
    public InstantiatorNormal withDefaultPrimitiveCreator(PrimitiveCreatorSI creator) {
        this.defaultPrimitiveCreator = creator;
        return this;
    }

    @Override
    public <U> InstantiatorNormal withPrimitiveCreatorSIForClass(Class<U> clazz, PrimitiveCreatorSI creator) {
        this.primitiveInClassInstMap.put(clazz, creator);
        return this;
    }

    @Override
    public <U, V> InstantiatorNormal withCreatorForField(Class<U> parentClass, String name, Class<V> fieldClass, ClassCreatorSI creator) {
        this.fieldInClassInstMap.put(Triplet.with(parentClass, name, fieldClass), creator);
        return this;
    }

    @Override
    public <U> InstantiatorNormal withCreatorForClass(Class<U> clazz, ClassCreatorSI<U> creator) {
        this.classInstMap.put(clazz, creator);
        return this;
    }
}
