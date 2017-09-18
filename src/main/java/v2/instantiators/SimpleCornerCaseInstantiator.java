package v2.instantiators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.*;
import v2.creators.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleCornerCaseInstantiator implements InstantiatorCornerCase,ListCreator,SetCreator,MapCreator{
    private ClassCreatorMap classInstMap = new ClassCreatorMap();
    private boolean allowNull = true;
    private Map<Class,Boolean> allowNullMap = Maps.newConcurrentMap();
    private FieldCreatorMap fieldInClassInstMap = new FieldCreatorMap();
    private Map<Class, PrimitiveCreator> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreator defaultPrimitiveCreator = new SimpleCCPrimCreator();



    @Override
    public <T> Set<T> createCornerCasesForClass(Class<T> clazz, Path path) {
        Set<T> res;
        if(classInstMap.containsKey(clazz)){
            res = classInstMap.get(clazz).get().createCornerCases();
            return res;
        }

        if(path.contains(clazz)){
            throw Utils.createRecursivePathException(clazz,path);
        }

        Path updated = path.append(clazz);


        if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
            return (Set<T>) SubTypeMap.getImplementations(clazz)
                    .map(subs -> subs.stream()
                            .flatMap(subtype -> createCornerCasesForClass(subtype, updated).stream())
                            .collect(Collectors.toSet()))
                    .getOrElseThrow(() -> Utils.createNoimplementationsForClassException(clazz, updated));

        boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
        if(!zeroArgConstructors){
            throw Utils.createUnknownClassException(clazz,updated);
        }

        Map<Field,Set<?>> valuesByField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAccessible())
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> Pair.with(field, new HashSet<>(createCornerCasesForField(field, clazz, field.getType(), updated))))
                .collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

        try {

            Optional<Integer> maybeComplexity = valuesByField.entrySet().stream()
                    .map(entry -> entry.getValue().size())
                    .max(Integer::compareTo);

            if(!maybeComplexity.isPresent()){
                return Sets.newHashSet();
            }

            res = new HashSet<>();
            for (int i = 0; i < maybeComplexity.get(); i++) {
                T inst = clazz.newInstance();

                for (Map.Entry<Field, Set<?>> entry: valuesByField.entrySet()){

                    Field field = entry.getKey();
                    field.setAccessible(true);
                    List<?> values = Arrays.asList(entry.getValue().toArray()); //Dont shuffle this list!
                    Object value = values.get((i%values.size()));
                    updateField(inst, field, value);
                }
                res.add(inst);
            }

            return res;
        } catch (Exception e){
            throw Utils.createExceptionForPath(clazz, updated, e);
        }
    }

    private <T> T updateField(T inst, Field field, Object value) throws IllegalAccessException {
        field.set(inst,value);
        field.setAccessible(false);
        return inst;
    }


    public <T,U> Set<U> createCornerCasesForField(Field field, Class<T> clazz, Class<U> fieldType, Path path){
        path = path.append(field);
        Set<U> res;
        if(fieldType.isEnum()) {
            res = new HashSet<>(Arrays.asList(fieldType.getEnumConstants()));
            return res;
        } else if(Utils.isPrimitive(fieldType)){
            res = handlePrimitive(field,clazz,fieldType);
            return res;
        } else if(fieldType.equals(String.class)) {
            res = (Set<U>) new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), ""));
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            return res;
        } else if(fieldType.equals(List.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (Set<U>) createLists(genericParam, clazz, path);
            return res;
        } else if(fieldType.equals(Set.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (Set<U>) createSets(genericParam, clazz, path);
            return res;
        } else if(fieldType.equals(Map.class)) {
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (Set<U>) createMaps(genericParamKey, genericParamValue, clazz, path);
            return res;
        }else if(fieldType.equals(io.vavr.collection.List.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            //generics/containers
            //if class has generic parameters and has a static "of" or "from" method that takes either
            // - a list
            // - an iterable
            return (Set<U>) createVavrList(genericParam,clazz,path);
        }else if(fieldType.equals(io.vavr.collection.Set.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            //generics/containers
            //if class has generic parameters and has a static "of" or "from" method that takes either
            // - a list
            // - an iterable
            return (Set<U>) createVavrSet(genericParam,clazz,path);
        }else if(fieldType.equals(io.vavr.collection.Map.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (Set<U>) createVavrMap(genericParamKey, genericParamValue, clazz, path);
            return res;
        } else {
            res = createCornerCasesForClass(fieldType, path);
            if(allowNullMap.getOrDefault(clazz, allowNull)){
                res.add(null);
            }
            return res;
        }
    }

    private <T,U> Set<T> handlePrimitiveSimple(Class<T> clazz, Class<U> parent){
        return primitiveInClassInstMap.getOrDefault(parent,defaultPrimitiveCreator).getValuesForType(clazz);
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

    @Override
    public <T,U> Set<List<T>> createLists(Class<T> clazz, Class<U> parent, Path path) {
        //cases: empty list, list with 1 element, list with 2 elements, list with duplicates, list with null element
        Set<List<T>> res = Sets.newHashSet();

        if(allowNullMap.getOrDefault(clazz, allowNull)){
//            List<T> withNull = Arrays.asList(null);
//            res.add(withNull);
            res.add(null);
        }


        Set<T> cornerCases = createCornerCasesForGenericValue(clazz, parent, path);
        T anElement = cornerCases.iterator().next();
        List<T> empty = Lists.newArrayList();
        List<T> singleElement = Arrays.asList(anElement);
        List<T> withDuplicate = Arrays.asList(anElement,anElement);
        List<T> cornerCasesAsList = new ArrayList<>(cornerCases);
        res.add(empty);
        res.add(singleElement);
        res.add(withDuplicate);
        res.add(cornerCasesAsList);

        return res;
    }

    @Override
    public <T, U,V> Set<Map<T, U> > createMaps(Class<T> key, Class<U> value, Class<V> parent, Path path) {
        Set<Map<T,U>> res = Sets.newHashSet();
        Random random = new Random();

        Set<T> keys = createCornerCasesForGenericValue(key, parent, path);
        Set<U> values = createCornerCasesForGenericValue(value, parent, path);

        int maxSize = Math.max(keys.size(), values.size());
        List<T> keyList = Lists.newArrayList(keys);
        List<U> valueList = Lists.newArrayList(values);
        List<T> keyListRandom = Lists.newArrayList(keys);
        List<U> valueListRandom = Lists.newArrayList(values);
        //seriously... what the fuck is this? Why is there not a zip function?
        for (int i = keys.size(); i < maxSize; i++) {
            keyList.add(keyListRandom.get(random.nextInt(keyListRandom.size()-1)));
        }

        for (int i = values.size(); i < maxSize; i++) {
            valueList.add(valueListRandom.get(random.nextInt(valueListRandom.size()-1)));
        }
        Map<T,U> empty = Maps.newHashMap();
        res.add(empty);
        Map<T,U> rest = Maps.newHashMap();
        for (int i = 0; i < maxSize; i++) {
            rest.put(keyList.get(i),valueList.get(i));
        }
        res.add(rest);
        if(allowNull){ // TODO: 13/09/2017 Verify that this is okay
            res.add(null);
        }


        //cases: empty map, from all sorts of keys to all sorts of values
        return res;
    }

    @Override
    public <T,U> Set<Set<T>> createSets(Class<T> clazz, Class<U> parent, Path path) {
        Set<Set<T>> res = Sets.newHashSet();

        if(allowNullMap.getOrDefault(clazz, allowNull)){
//            Set<T> withNull = new HashSet<>(null);
//            res.add(withNull);
            res.add(null);
        }

        Set<T> cornerCases = createCornerCasesForGenericValue(clazz, parent, path);
        T anElement = cornerCases.iterator().next();
        Set<T> empty = Sets.newHashSet();
        Set<T> singleElement = Sets.newHashSet(anElement);
        res.add(empty);
        res.add(singleElement);
        res.add(cornerCases);

        return res;
    }

    private <T,U> Set<io.vavr.collection.List<T>> createVavrList(Class<T> clazz, Class<U> parent, Path path){
        Set<io.vavr.collection.List<T>> res = Sets.newHashSet();
        if(allowNullMap.getOrDefault(clazz,allowNull)){
            res.add(null);
        }
        io.vavr.collection.List<T> cornerCases = io.vavr.collection.List.ofAll(createCornerCasesForGenericValue(clazz,parent,path));
        io.vavr.collection.List<T> empty = io.vavr.collection.List.empty();
        io.vavr.collection.List<T> single = io.vavr.collection.List.of(cornerCases.head());
        res.add(cornerCases);
        res.add(empty);
        res.add(single);
        return res;
    }

    private <T,U> Set<io.vavr.collection.Set<T>> createVavrSet(Class<T> clazz, Class<U> parent, Path path){
        Set<io.vavr.collection.Set<T>> res = Sets.newHashSet();
        if(allowNullMap.getOrDefault(clazz,allowNull)){
            res.add(null);
        }
        io.vavr.collection.Set<T> cornerCases = io.vavr.collection.TreeSet.ofAll(new ArrayList(createCornerCasesForGenericValue(clazz,parent,path)));
        io.vavr.collection.Set<T> empty = (io.vavr.collection.Set<T>) io.vavr.collection.List.empty().toSet();
        io.vavr.collection.Set<T> single = io.vavr.collection.List.of(cornerCases.head()).toSet();
        res.add(cornerCases);
        res.add(empty);
        res.add(single);
        return res;
    }

    private <T,U,V> Set<io.vavr.collection.Map<T,U>> createVavrMap(Class<T> key, Class<U> value, Class<V> parent, Path path) {
        Set<io.vavr.collection.Map<T,U>> res = Sets.newHashSet();
        Random random = new Random();

        Set<T> keys = createCornerCasesForGenericValue(key, parent, path);
        Set<U> values = createCornerCasesForGenericValue(value, parent, path);

        int maxSize = Math.max(keys.size(), values.size());
        List<T> keyList = Lists.newArrayList(keys);
        List<U> valueList = Lists.newArrayList(values);
        List<T> keyListRandom = Lists.newArrayList(keys);
        List<U> valueListRandom = Lists.newArrayList(values);
        //seriously... what the fuck is this? Why is there not a zip function?
        for (int i = keys.size(); i < maxSize; i++) {
            keyList.add(keyListRandom.get(random.nextInt(keyListRandom.size()-1)));
        }

        for (int i = values.size(); i < maxSize; i++) {
            valueList.add(valueListRandom.get(random.nextInt(valueListRandom.size()-1)));
        }
        io.vavr.collection.Map<T,U> empty = io.vavr.collection.HashMap.empty();
        res.add(empty);
        io.vavr.collection.Map<T,U> rest = io.vavr.collection.HashMap.empty();
        for (int i = 0; i < maxSize; i++) {
            rest.put(keyList.get(i),valueList.get(i));
        }
        res.add(rest);
        if(allowNull){ // TODO: 13/09/2017 Verify that this is okay
            res.add(null);
        }


        //cases: empty map, from all sorts of keys to all sorts of values
        return res;
    }

    private <T,U> Set<T> createCornerCasesForGenericValue(Class<T> clazz, Class<U> parent, Path path){
        // TODO: 14/09/2017 String? Nested generic value?
        if(Utils.isPrimitive(clazz)){
            return handlePrimitiveSimple(clazz,parent);
        } else {
            return createCornerCasesForClass(clazz, path);
        }
    }

    @Override
    public SimpleCornerCaseInstantiator withDefaultPrimitiveCreator(PrimitiveCreator creator) {
        this.defaultPrimitiveCreator = creator;
        return this;
    }

    @Override
    public InstantiatorCornerCase withNullable(boolean nullable) {
        this.allowNull = nullable;
        return this;
    }

    @Override
    public <U> InstantiatorCornerCase withPrimitiveCreatorForClass(Class<U> clazz, PrimitiveCreator creator) {
        this.primitiveInClassInstMap.put(clazz,creator);
        return this;
    }

    @Override
    public <U, V> InstantiatorCornerCase withCreatorForField(Class<U> parentClass, String name, Class<V> fieldClass, ClassCreator creator) {
        this.fieldInClassInstMap.put(Triplet.with(parentClass,name, fieldClass), creator);
        return this;
    }

    @Override
    public <U> InstantiatorCornerCase withCreatorForClass(Class<U> clazz, ClassCreator<U> creator) {
        this.classInstMap.put(clazz,creator);
        return this;
    }
}
