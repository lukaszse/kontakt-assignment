package io.kontakt.apps.anomaly.detector.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static double calculateMeanValue(List<Double> values) {
        return values.stream()
                .mapToDouble(temp -> temp)
                .average()
                .orElseThrow();
    }
}
