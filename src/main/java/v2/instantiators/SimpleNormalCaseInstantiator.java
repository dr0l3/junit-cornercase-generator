package v2.instantiators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import io.vavr.control.Either;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.*;
import v2.creators.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleNormalCaseInstantiator implements InstantiatorNormal, ListCreatorSI, MapCreatorSI, SetCreatorSI {
    private ClassCreatorMapSI classInstMap = new ClassCreatorMapSI();
    //seems there is no way to verify that the class creator has the correct type
    private FieldCreatorMapSI fieldInClassInstMap = new FieldCreatorMapSI();
    private Map<Class, PrimitiveCreatorSI> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreatorSI defaultPrimitiveCreator = new SimpleNormalPrimCreator();

    private <T, U> U handlePrimitive(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness) {
        String fieldName = field.getName();
        Triplet<Class<T>, String, Class<U>> com = Triplet.with(clazz, fieldName, fieldType);
        if (fieldInClassInstMap.containsKey(com)) {
            return fieldInClassInstMap.get(com).createInstance();
        }

        return primitiveInClassInstMap.getOrDefault(clazz, defaultPrimitiveCreator).getValueForType(fieldType, randomness);
    }


    @Override
    public <T> T createInstance(Class<T> clazz, SourceOfRandomness randomness, Path path) {
        T res;
        if (classInstMap.containsKey(clazz)) {
            res = classInstMap.get(clazz).createInstance();
            return res;
        }

        if (path.contains(clazz)) {
            throw Utils.createRecursivePathException(clazz, path);
        }

        Path updatedPath = path.append(clazz);

        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            return SubTypeMap.getImplementations(clazz)
                    .map(subs -> {
                        int index = randomness.nextInt(0, subs.size() - 1);
                        Class<? extends T> next = new ArrayList<>(subs).get(index);
                        return createInstance(next, randomness, updatedPath);})
                    .getOrElseThrow(() -> Utils.createNoimplementationsForClassException(clazz,updatedPath));
        }

        if (Utils.isPrimitive(clazz)) {
            return defaultPrimitiveCreator.getValueForType(clazz, randomness);
        }

        boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
        if (!zeroArgConstructors) {
            throw Utils.createUnknownClassException(clazz, updatedPath);
        }

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        List<Field> filtered = fields.stream()
                .filter(field -> !field.isAccessible())
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());


        Map<Field, ?> valuesByField = filtered.stream()
                .map(field -> Pair.with(field, createNormalCaseForField(field, clazz, field.getType(), randomness, updatedPath)))
                .collect(Collectors.toMap(Pair::getValue0,Pair::getValue1));

        try {

            T inst = clazz.newInstance();
            for (Map.Entry<Field, ?> entry : valuesByField.entrySet()) {

                Field field = entry.getKey();
                field.setAccessible(true);
                Object value = entry.getValue();
                field.set(inst, value);
                field.setAccessible(false);
            }

            return inst;
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.createExceptionForPath(clazz, path, e);
        }
    }

    public <T, U> U createNormalCaseForField(Field field, Class<T> clazz, Class<U> fieldType, SourceOfRandomness randomness, Path path) {
        U res;
        path = path.append(field);
        if (fieldType.isEnum()) {
            List<U> possibles = Arrays.asList(fieldType.getEnumConstants());
            int index = randomness.nextInt(0, possibles.size() - 1);
            return possibles.get(index);
        } else if (Utils.isPrimitive(fieldType)) {
            return handlePrimitive(field, clazz, fieldType, randomness);
        } else if (fieldType.equals(String.class)) {
            return (U) UUID.randomUUID().toString();
        } else if (fieldType.equals(List.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createList(genericParamValue, clazz, randomness, path);
            return res;
        } else if (fieldType.equals(Set.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createSet(genericParamValue, clazz, randomness, path);
            return res;
        } else if (fieldType.equals(Map.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (U) createMap(genericParamKey, genericParamValue, clazz, randomness, path);
            return res;
        } else if (fieldType.equals(io.vavr.collection.List.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createVavrList(genericParamKey, clazz, randomness, path);
            return res;
        } else if (fieldType.equals(io.vavr.collection.Set.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (U) createVavrSet(genericParamKey, clazz, randomness, path);
            return res;
        } else if (fieldType.equals(io.vavr.collection.Map.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (U) createVavrMap(genericParamKey, genericParamValue, clazz, randomness, path);
            return res;
        }else if(fieldType.isArray()){
            Class<?> genericParam = fieldType.getComponentType();
            if(genericParam.isPrimitive()){
                res = (U) createPrimitiveArray(genericParam,clazz,randomness, path);
            } else {
                res = (U) createNonPrimitiveArray(genericParam, clazz, randomness, path);
            }

            return res;
        }

        res = createInstance(fieldType, randomness, path);
        return res;
    }

    private<T,U> T createPrimitiveArray(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path){
        return (T) primitiveInClassInstMap.getOrDefault(clazz,defaultPrimitiveCreator).getArrayValuesForType(clazz,randomness);
    }

    private <T,U> T[] createNonPrimitiveArray(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path){
        int size = randomness.nextInt(0,50); // TODO: 19/09/2017 Configuration
        T[] many = (T[]) Array.newInstance(clazz,size);
        for (int i = 0; i < size; i++) {
            T value = createInstance(clazz,randomness,path);
            Arrays.fill(many,i, i+1, value);
        }
        return many;
    }

    @Override
    public <T, U> List<T> createList(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        List<T> res = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            res.add(createInstance(clazz, randomness, path));
        }
        return res;
    }

    public <T, U> io.vavr.collection.List<T> createVavrList(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        io.vavr.collection.List<T> res = io.vavr.collection.List.empty();
        for (int i = 0; i < size; i++) {
            res = res.prepend(createInstance(clazz, randomness, path));
        }
        return res;
    }

    @Override
    public <T, U, V> Map<T, U> createMap(Class<T> keyClazz, Class<U> valueClazz, Class<V> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        Map<T, U> res = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            T key = createInstance(keyClazz, randomness, path);
            U value = createInstance(valueClazz, randomness, path);
            res.put(key, value);
        }
        return res;
    }

    public <T, U, V> io.vavr.collection.Map<T, U> createVavrMap(Class<T> keyClazz, Class<U> valueClazz, Class<V> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        io.vavr.collection.Map<T, U> res = io.vavr.collection.HashMap.empty();
        for (int i = 0; i < size; i++) {
            T key = createInstance(keyClazz, randomness, path);
            U value = createInstance(valueClazz, randomness, path);
            res = res.put(key, value);
        }
        return res;
    }

    @Override
    public <T, U> Set<T> createSet(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        Set<T> res = Sets.newHashSet();
        for (int i = 0; i < size; i++) {
            res.add(createInstance(clazz, randomness, path));
        }
        return res;
    }

    public <T, U> io.vavr.collection.Set<T> createVavrSet(Class<T> clazz, Class<U> parent, SourceOfRandomness randomness, Path path) {
        int size = randomness.nextInt(0, 50); // TODO: 14/09/2017 Configuration
        io.vavr.collection.Set<T> res = io.vavr.collection.HashSet.empty();
        for (int i = 0; i < size; i++) {
            res = res.add(createInstance(clazz, randomness, path));
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
