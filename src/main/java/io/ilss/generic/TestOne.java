package io.ilss.generic;

import java.io.File;
import java.util.ArrayList;

/**
 * className TestOne
 * description TestOne
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 18:28
 */
public class TestOne {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        list.add("string1");
        list.add("string2");
        String str = (String) list.get(0);
        list.add(new File("test.txt"));
    }
}
