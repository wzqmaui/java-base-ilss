package io.ilss.reflection;

import java.util.ArrayList;
import java.util.List;

/**
 * className ObjectAnalyzerTest
 * description ObjectAnalyzerTest
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-26 19:30
 */
public class ObjectAnalyzerTest {
    public static void main(String[] args) {
        List<Integer> squares = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            squares.add(i * i);
        }
        System.out.println(new ObjectAnalyzer().toString(squares));

        System.out.println(new ObjectAnalyzer().toString());

        User[] arr = new User[10];
        System.out.println(new ObjectAnalyzer().toString(arr));

        User user = new User("ilss", "ilss password");
        System.out.println(new ObjectAnalyzer().toString(user));
    }
}
