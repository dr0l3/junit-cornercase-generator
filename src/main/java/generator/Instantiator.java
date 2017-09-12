package generator;

import instantiator.CreationStrategy;
import instantiator.PrimitiveCreator;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Instantiator {
    protected Map<Class, CreationStrategy> classInstMap;
    protected Map<Pair<Class, Class>, CreationStrategy> classInClassInstMap;
    protected Map<Pair<Class, String>, CreationStrategy> fieldInClassInstMap;
    protected Map<Class, PrimitiveCreator> primitiveInClassInstMap;
    protected Map<Class, Boolean> allowNullMap;
    protected Set<Class> visiting;
    protected boolean allowNull;
    protected PrimitiveCreator defaultPrimitiveCreator;

    protected void setEmptyDefaults(){
        classInstMap = new HashMap<>();
        classInClassInstMap = new HashMap<>();
        fieldInClassInstMap = new HashMap<>();
        primitiveInClassInstMap = new HashMap<>();
        allowNullMap = new HashMap<>();
        visiting = new HashSet<>();
    }

    public Map<Class, CreationStrategy> getClassInstMap() {
        return classInstMap;
    }

    public void setClassInstMap(Map<Class, CreationStrategy> classInstMap) {
        this.classInstMap = classInstMap;
    }

    public Map<Pair<Class, Class>, CreationStrategy> getClassInClassInstMap() {
        return classInClassInstMap;
    }

    public void setClassInClassInstMap(Map<Pair<Class, Class>, CreationStrategy> classInClassInstMap) {
        this.classInClassInstMap = classInClassInstMap;
    }

    public Map<Pair<Class, String>, CreationStrategy> getFieldInClassInstMap() {
        return fieldInClassInstMap;
    }

    public void setFieldInClassInstMap(Map<Pair<Class, String>, CreationStrategy> fieldInClassInstMap) {
        this.fieldInClassInstMap = fieldInClassInstMap;
    }

    public Map<Class, PrimitiveCreator> getPrimitiveInClassInstMap() {
        return primitiveInClassInstMap;
    }

    public void setPrimitiveInClassInstMap(Map<Class, PrimitiveCreator> primitiveInClassInstMap) {
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

    public PrimitiveCreator getDefaultPrimitiveCreator() {
        return defaultPrimitiveCreator;
    }

    public void setDefaultPrimitiveCreator(PrimitiveCreator defaultPrimitiveCreator) {
        this.defaultPrimitiveCreator = defaultPrimitiveCreator;
    }
}
