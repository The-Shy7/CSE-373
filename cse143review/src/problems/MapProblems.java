package problems;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        Map<String, Integer> m = new HashMap<String, Integer>();

        for (String word : list) {
            if (m.containsKey(word)) {
                m.put(word, m.get(word) + 1);
            } else {
                m.put(word, 1);
            }
        }

        return m.containsValue(3);
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> intersect = new HashMap<String, Integer>();
        List<String> common = new LinkedList<String>();

        for (String word : m1.keySet()) {
            if (m2.containsKey(word)) {
                common.add(word);
            }
        }

        for (String word : common) {
            if (m1.get(word) == m2.get(word)) {
                intersect.put(word, m1.get(word));
            }
        }

        return intersect;
    }
}
