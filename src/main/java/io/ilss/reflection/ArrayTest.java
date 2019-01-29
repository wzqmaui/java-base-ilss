package io.ilss.reflection;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * className ArrayTest
 * description ArrayTest
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-29 23:42
 */
public class ArrayTest {
    public static void main(String[] args) {
        double[] arr = {1.1, 1.2, 1.4, 12.2};
        arr = (double[]) goodCopyOf(arr, 10);
        System.out.println(Arrays.toString(arr));

        String[] arr1 = {"aa", "bb", "cc"};
        arr1 = (String[]) goodCopyOf(arr1, 10);
        System.out.println(Arrays.toString(arr1));

        System.out.println("ClassCastException");
        arr1 = (String[]) badCopyOf(arr1, 20);
    }

    private static Object[] badCopyOf(Object[] arr, int newLength) {
        Object[] newArray = new Object[newLength];
        System.arraycopy(arr, 0, newArray, 0, Math.min(arr.length, newLength));
        return newArray;
    }

    private static Object goodCopyOf(Object arr, int newLength) {
        Class cls = arr.getClass();
        if (!cls.isArray()) {
            return null;
        }
        Object newArray = Array.newInstance(cls.getComponentType(), newLength);
        System.arraycopy(arr, 0, newArray, 0, Math.min(Array.getLength(arr), newLength));
        return newArray;
    }
}
