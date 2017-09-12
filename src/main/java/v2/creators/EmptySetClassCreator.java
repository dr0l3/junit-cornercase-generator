package v2.creators;

import com.google.common.collect.Sets;

import java.util.Set;

public class EmptySetClassCreator<T> implements ClassCreator<T> {
    @Override
    public Set<T> createCornerCases() {
        return Sets.newHashSet();
    }
}
