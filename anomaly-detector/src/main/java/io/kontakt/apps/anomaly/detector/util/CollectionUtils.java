package io.kontakt.apps.anomaly.detector.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    // Apache commons library could be also used instead
    public static <E> List<E> union(Collection<E> col1, Collection<E> col2) {
        final List<E> newList = new ArrayList<>(col1);
        newList.addAll(col2);
        return newList;
    }
}
