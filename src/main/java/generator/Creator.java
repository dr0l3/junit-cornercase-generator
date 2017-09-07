package generator;

import instantiator.InstantiationStrategy;
import instantiator.PrimitiveInstantiator;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Creator {
    protected Map<Class, InstantiationStrategy> classInstMap;
    protected Map<Pair<Class, Class>,InstantiationStrategy> classInClassInstMap;
    protected Map<Pair<Class, String>,InstantiationStrategy> fieldInClassInstMap;
    protected Map<Class, PrimitiveInstantiator> primitiveInClassInstMap;
    protected Map<Class, Boolean> allowNullMap;
    protected Set<Class> visiting;
    protected boolean allowNull;
    protected PrimitiveInstantiator defaultPrimitiveInstantiator;

    protected void setEmptyDefaults(){
        classInstMap = new HashMap<>();
        classInClassInstMap = new HashMap<>();
        fieldInClassInstMap = new HashMap<>();
        primitiveInClassInstMap = new HashMap<>();
        allowNullMap = new HashMap<>();
        visiting = new HashSet<>();
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

    public Map<Class, PrimitiveInstantiator> getPrimitiveInClassInstMap() {
        return primitiveInClassInstMap;
    }

    public void setPrimitiveInClassInstMap(Map<Class, PrimitiveInstantiator> primitiveInClassInstMap) {
        this.primitiveInClassInstMap = primitiveInClassInstMap;
    }

    public Map<Class, Boolean> getAllowNullMap() {
        return allowNullMap;
    }

    public void setAllowNullMap(Map<Class, Boolean> allowNullMap) {
        this.allowNullMap = allowNullMap;
    }

    public Set<Class> getVisiting() {
        return visiting;
    }

    public void setVisiting(Set<Class> visiting) {
        this.visiting = visiting;
    }

    public boolean isAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    public PrimitiveInstantiator getDefaultPrimitiveInstantiator() {
        return defaultPrimitiveInstantiator;
    }

    public void setDefaultPrimitiveInstantiator(PrimitiveInstantiator defaultPrimitiveInstantiator) {
        this.defaultPrimitiveInstantiator = defaultPrimitiveInstantiator;
    }
}
