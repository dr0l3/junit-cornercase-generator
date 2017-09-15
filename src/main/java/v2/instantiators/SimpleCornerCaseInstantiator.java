package v2.instantiators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.atlassian.fugue.Either;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import v2.ClassCreatorMap;
import v2.FieldCreatorMap;
import v2.SubTypeMap;
import v2.Utils;
import v2.creators.*;
import v2.generators.PrimitiveCreatorConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleCornerCaseInstantiator implements InstantiatorCornerCase,ListCreator,SetCreator,MapCreator{
    private Set<Class> visiting = Sets.newHashSet();
    private ClassCreatorMap classInstMap = new ClassCreatorMap();
    private boolean allowNull = true;
    private Map<Class,Boolean> allowNullMap = Maps.newConcurrentMap();
    //seems there is no way to verify that the class creator has the correct type
    private FieldCreatorMap fieldInClassInstMap = new FieldCreatorMap();
    private Map<Class, PrimitiveCreator> primitiveInClassInstMap = Maps.newConcurrentMap();
    private PrimitiveCreator defaultPrimitiveCreator = new SimpleCCPrimCreator();
    private Stack<Either<Class,Field>> path = new Stack<>();

    @Override
    public <T> Set<T> createCornerCasesForClass(Class<T> clazz) {

        if(visiting.contains(clazz)){
            System.out.println(path);
            throw new RuntimeException("Recursive definition! class " + clazz + " has already been visited"); // TODO: 15/09/2017 Better
        }
        path.push(Either.left(clazz));
        visiting.add(clazz);
        System.out.println("Creating corner clases for class: " + clazz.toString());

        if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
            Set<Class<? extends T>> subtypes;
            if(SubTypeMap.containsKey(clazz)){
                subtypes = SubTypeMap.get(clazz);
            } else {
                subtypes = SubTypeMap.reflections.getSubTypesOf(clazz);
                SubTypeMap.put(clazz,subtypes);
            }
            visiting.remove(clazz); // FIXME: 15/09/2017 Is this even corect?
            return subtypes.stream().flatMap(subtype -> createCornerCasesForClass(subtype).stream()).collect(Collectors.toSet());
        }
        try {
            boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
            if(!zeroArgConstructors){
                if(classInstMap.containsKey(clazz)){
                    return classInstMap.get(clazz).createCornerCases();
                } else {
                    System.out.println(path);
                    throw new RuntimeException("Unable to instantiate class of type " + clazz); // TODO: 15/09/2017 Better
                }
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
                    Collections.shuffle(values);
                    Object value = values.get((i%values.size()));
                    field.set(inst,value);
                    field.setAccessible(false);
                }
                res.add(inst);
            }
            visiting.remove(clazz);
            return res;
        } catch (Exception e){
//            e.printStackTrace();
            visiting.remove(clazz);
            System.out.println(path);
            throw new RuntimeException("Exception doing creation of class " + clazz + ". error reported was " + e.getMessage()); // TODO: 15/09/2017 Better
        }
    }


    public <T,U> Set<U> createCornerCasesForField(Field field, Class<T> clazz, Class<U> fieldType){
        path.push(Either.right(field));
        if(fieldType.isEnum()) {
            path.pop();
            return new HashSet<>(Arrays.asList(fieldType.getEnumConstants()));
        } else if(Utils.isPrimitive(fieldType)){
            path.pop();
            return handlePrimitive(field,clazz,fieldType);
        } else if(fieldType.equals(String.class)) {
            HashSet<String> res = new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), ""));
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            path.pop();
            return (Set<U>) res;
        } else if(fieldType.equals(List.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            path.pop();
            return (Set<U>) createLists(genericParam, clazz);
        } else if(fieldType.equals(Set.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            path.pop();
            return (Set<U>) createSets(genericParam, clazz);
        } else if(fieldType.equals(Map.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            path.pop();
            return (Set<U>) createMaps(genericParamKey,genericParamValue, clazz);
        }

        try {
            Set<U> cornerCasesForComplexType = createCornerCasesForClass(fieldType);
            if(allowNullMap.getOrDefault(clazz, allowNull)){
                cornerCasesForComplexType.add(null);
            }
            path.pop();
            return cornerCasesForComplexType;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        System.out.println(path);
        throw new RuntimeException("Unable to instantiate field " + field.getName() + " in " + clazz + ". Dont know how to deal with type " + fieldType); // TODO: 15/09/2017 Better
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
    public <T,U> Set<List<T>> createLists(Class<T> clazz, Class<U> parent) {
        //cases: empty list, list with 1 element, list with 2 elements, list with duplicates, list with null element
        Set<List<T>> res = Sets.newHashSet();

        if(allowNullMap.getOrDefault(clazz, allowNull)){
//            List<T> withNull = Arrays.asList(null);
//            res.add(withNull);
            res.add(null);
        }


        Set<T> cornerCases = createCornerCasesForGenericValue(clazz, parent);
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
    public <T, U,V> Set<Map<T, U> > createMaps(Class<T> key, Class<U> value, Class<V> parent) {
        Set<Map<T,U>> res = Sets.newHashSet();
        Random random = new Random();

        Set<T> keys = createCornerCasesForGenericValue(key,parent);
        Set<U> values = createCornerCasesForGenericValue(value,parent);

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
    public <T,U> Set<Set<T>> createSets(Class<T> clazz, Class<U> parent) {
        Set<Set<T>> res = Sets.newHashSet();

        if(allowNullMap.getOrDefault(clazz, allowNull)){
//            Set<T> withNull = new HashSet<>(null);
//            res.add(withNull);
            res.add(null);
        }

        Set<T> cornerCases = createCornerCasesForGenericValue(clazz, parent);
        T anElement = cornerCases.iterator().next();
        Set<T> empty = Sets.newHashSet();
        Set<T> singleElement = Sets.newHashSet(anElement);
        res.add(empty);
        res.add(singleElement);
        res.add(cornerCases);

        return res;
    }

    private <T,U> Set<T> createCornerCasesForGenericValue(Class<T> clazz, Class<U> parent){
        // TODO: 14/09/2017 String?
        if(Utils.isPrimitive(clazz)){
            return handlePrimitiveSimple(clazz,parent);
        } else {
            return createCornerCasesForClass(clazz);
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
}
