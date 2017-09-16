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
        Set<T> res;
        if(classInstMap.containsKey(clazz)){
            res = classInstMap.get(clazz).createCornerCases();
            return res;
        }

        if(visiting.contains(clazz)){
            throw Utils.createRecursivePathException(clazz,path);
        }

        path.push(Either.left(clazz));
        visiting.add(clazz);


        if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
            return SubTypeMap.getImplementations(clazz)
                    .map(subs -> {
                        Set<T> cornerCases = subs.stream()
                                .flatMap(subtype -> createCornerCasesForClass(subtype).stream())
                                .collect(Collectors.toSet());
                        visiting.remove(clazz);
                        path.pop();
                        return cornerCases;})
                    .orElseThrow(() -> Utils.createNoimplementationsForClassException(clazz,path));
        }

        boolean zeroArgConstructors = Stream.of(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() == 0);
        if(!zeroArgConstructors){
            System.out.println(path);
            throw Utils.createUnknownClassException(clazz,path);
        }

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        List<Field> filtered = fields.stream()
                .filter(field -> !field.isAccessible())
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());

        try {
            Map<Field,Set<?>> valuesByField = filtered.stream()
                    .map(field -> Pair.with(field, new HashSet<>(createCornerCasesForField(field, clazz, field.getType()))))
                    .collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

            int complexity = valuesByField.entrySet().stream()
                    .map(entry -> entry.getValue().size())
                    .reduce(0, Math::max);

            res = new HashSet<>();
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
            path.pop();
            return res;
        } catch (Exception e){
            System.out.println(path);
            throw Utils.createExceptionForPath(clazz,path, e);
        }
    }


    public <T,U> Set<U> createCornerCasesForField(Field field, Class<T> clazz, Class<U> fieldType){
        path.push(Either.right(field));
        Set<U> res;
        if(fieldType.isEnum()) {
            res = new HashSet<>(Arrays.asList(fieldType.getEnumConstants()));
            path.pop();
            return res;
        } else if(Utils.isPrimitive(fieldType)){
            res = handlePrimitive(field,clazz,fieldType);
            path.pop();
            return res;
        } else if(fieldType.equals(String.class)) {
            res = (Set<U>) new HashSet<>(Arrays.asList(java.util.UUID.randomUUID().toString(), ""));
            if(allowNullMap.getOrDefault(clazz,allowNull)){
                res.add(null);
            }
            path.pop();
            return res;
        } else if(fieldType.equals(List.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (Set<U>) createLists(genericParam, clazz);
            path.pop();
            return res;
        } else if(fieldType.equals(Set.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParam = (Class<?>) genericType.getActualTypeArguments()[0];
            res = (Set<U>) createSets(genericParam, clazz);
            path.pop();
            return res;
        } else if(fieldType.equals(Map.class)){
            ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            Class<?> genericParamKey = (Class<?>) genericType.getActualTypeArguments()[0];
            Class<?> genericParamValue = (Class<?>) genericType.getActualTypeArguments()[1];
            res = (Set<U>) createMaps(genericParamKey,genericParamValue, clazz);
            path.pop();
            return res;
        } else {
            res = createCornerCasesForClass(fieldType);
            if(allowNullMap.getOrDefault(clazz, allowNull)){
                res.add(null);
            }
            path.pop();
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
        // TODO: 14/09/2017 String? Nested generic value?
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

    @Override
    public <U> InstantiatorCornerCase withCreatorForClass(Class<U> clazz, ClassCreator<U> creator) {
        this.classInstMap.put(clazz,creator);
        return this;
    }
}
