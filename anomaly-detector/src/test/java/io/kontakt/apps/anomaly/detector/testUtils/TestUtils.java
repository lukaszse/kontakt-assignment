package io.kontakt.apps.anomaly.detector.testUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestUtils {

    private static Random random = new Random();

    public static double generateRandomDoubleValueFromRange(double min, double max) {
        return random.nextDouble(max - min + 1) + min;
    }

    public static <E> List<E> unionAndShuffle(Collection<E> c1, Collection<E> c2) {
        ArrayList<E> newList = new ArrayList<>(c1);
        newList.addAll(c2);
        Collections.shuffle(newList);
        return newList;
    }
}
