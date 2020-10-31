package problems;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - Do not add any additional imports
 * - Do not create new `int[]` objects for `toString` or `rotateRight`
 */
public class ArrayProblems {

    /**
     * Returns a `String` representation of the input array.
     * Always starts with '[' and ends with ']'; elements are separated by ',' and a space.
     */
    public static String toString(int[] array) {
        String strArray = "[";

        if (array.length != 0) {
            if (array.length == 1) {
                strArray = strArray + "" + array[0];
            } else {
                for (int i = 0; i < array.length; i++) {
                    strArray = strArray + "" + array[i];

                    if (i + 1 < array.length) {
                        strArray = strArray + ", ";
                    }
                }
            }
        }

        return strArray + "]";
    }

    /**
     * Returns a new array containing the input array's elements in reversed order.
     * Does not modify the input array.
     */
    public static int[] reverse(int[] array) {
        int[] newArray = new int[array.length];
        int j = array.length;

        if (array.length != 0) {
            for (int i = 0; i < array.length; i++) {
                newArray[j - 1] = array[i];
                j -= 1;
            }
        }

        return newArray;
    }

    /**
     * Rotates the values in the array to the right.
     */
    public static void rotateRight(int[] array) {
        if (array.length > 1) {
            int last = array[array.length - 1];

            for (int i = array.length - 2; i >= 0; i--) {
                array[i + 1] = array[i];
            }

            array[0] = last;
        }
    }
}
