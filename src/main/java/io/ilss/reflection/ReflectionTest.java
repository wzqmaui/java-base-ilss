package io.ilss.reflection;

import java.lang.reflect.*;
import java.util.Scanner;

/**
 * className ReflectionTest
 * description ReflectionTest
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 21:25
 */
public class ReflectionTest {
    public static void main(String[] args) {
        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name (e.g. java.util.Date): ");
            name = in.next();
        }

        try {
            // 利用String name 得到类
            Class cls = Class.forName(name);
            // 得到父类
            Class superCls = cls.getSuperclass();
            // 得到类的修饰 "public final "
            String modifiers = Modifier.toString(cls.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print("class " + name);
            // 判断除Object外的父类存在与否
            if (superCls != null && superCls != Object.class) {
                System.out.print(" extends " + superCls.getName());
            }
            System.out.print("{\n");

            // 开始打印域
            printFields(cls);
            System.out.println();

            // 开始打印构造器
            printConstructors(cls);
            System.out.println();

            // 开始打印方法
            printMethods(cls);
            System.out.println();

            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void printFields(Class cls) {
        // 得到这个类的所有域
        Field[] fields = cls.getDeclaredFields();
        // 便利输出
        for (Field field : fields) {
            // 分别得到修饰 类型 名字的字符串
            String modifiers = Modifier.toString(field.getModifiers());
            Class type = field.getType();
            String name = field.getName();

            //输出
            System.out.print("    ");
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.println(type.getName() + " " + name + ";");
        }
    }

    private static void printConstructors(Class cls) {
        // 得到所有的构造器
        Constructor[] constructors = cls.getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            String name = constructor.getName();
            String modifiers = Modifier.toString(constructor.getModifiers());

            System.out.print("    ");
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print(name + "(");

            // 构造器参数
            Class[] paramTypes = constructor.getParameterTypes();

            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0) System.out.print(", ");
                System.out.print(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    private static void printMethods(Class cls) {
        Method[] methods = cls.getDeclaredMethods();

        for (Method method : methods) {
            String modifiers = Modifier.toString(method.getModifiers());
            String name = method.getName();
            // 返回类型
            Class retType = method.getReturnType();

            System.out.print("    ");
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print(retType.getName() + " " + name + "(");

            // 方法的形参
            Class[] paramTypes = method.getParameterTypes();
            for (int i = 0; i < paramTypes.length; i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(paramTypes[i].getName());
            }
            System.out.println(");");
        }
    }
}

