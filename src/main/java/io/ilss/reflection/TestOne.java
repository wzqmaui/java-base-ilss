package io.ilss.reflection;

/**
 * className TestOne
 * description TestOne
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 20:52
 */
public class TestOne {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Integer integer = 100;
        Class cls = integer.getClass();
        // 类名
        System.out.println(cls.getName());
        // 通过对类名来获取对象
        Class cls1 = Class.forName("java.lang.Integer");
        System.out.println(cls1.getName());
        //其他方法
        Class cls2 = Integer.class;
        System.out.println(cls2.getName());
        Class cls3 = Integer[].class;
        System.out.println(cls3.getName());
        //基本数据类型的class
        Class cls4 = int.class;
        System.out.println(cls4.getName());
        Class cls5 = Double[].class;
        System.out.println(cls5.getName());

        int a = integer.getClass().newInstance();
        System.out.println(a);
    }
}
