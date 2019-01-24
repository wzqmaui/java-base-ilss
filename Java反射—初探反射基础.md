# Java反射—初探反射基础

### Github：[https://github.com/ilssio/java-base-ilss](https://github.com/ilssio/java-base-ilss)

## 1. 认识Class类

Java在运行的时候，会为对象维护一个运行时的类型标识，虚拟机运行Java程序的时候用它来选择相应类的方法执行。

Java可以通过一个特定的类来访问这些类信息，这个类就是Class。

- 获取Class的几种方式

  ```Java
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
          Class cls4 = Integer[].class;
          System.out.println(cls4.getName());
          //基本数据类型的class 
          Class cls3 = int.class;
          System.out.println(cls3.getName());
  ```

  注意：

  1. `Class.forName(String)`会抛出checked exception（已检查异常）：`ClassNotFoundException`
  2. `Class.forName(String) `给的类需要包含包，包也是类名的一部分
  3. 虽然基本数据类型不是类，但是`int.class`是一个类。
  4. Integer[].class 的类名不是`java.lang.Integer`.而是`[Ljava.lang.Integer ` ,`Double.class`的为`[Ljava.lang.Double`;另外`int[].class`是`[I`

- Class类实际是一个泛型类

  `Integer.class`就是一个`Class<Integer>`

- Class类可以用 == 判断是否为同类，如：

  `if(integer.getClass() == Integer.class)`

- 动态创建类的实例

  `integer.getClass().newInstance()` 

  - 此处会抛出`InstantiationException`，

- `Class`类的一些方法

  - `T newInstance()`

    实例化一个此对象，此外方法在Java9以后已经不推荐被使用

  - `Field[] getFields()` 

    返回这个对象的`Field`数组，包括了本类和父类的公有域

  - `Field[] getDeclaredFields()`

    返回`Field`数组，包括这个类的全部域，如果对象没有域，则返回一个长度为0的数组

  - `Method[] getMethods()`

    返回这个对象的`Method`数组，包括了本类的和父类继承的公有方法

  - `Method[] getDeclaredMethods()`

    返回`Method`数组，包括这个类的全部方法，但是不包括父类继承的方法。

  - `Constructor[] getConstructors()`

    返回这个对象的`Constructor`数组，包含所有共有构造器

  - `Constructor[] getDeclaredConstructors()`

    返回这个对象的`Constructor`数组，包含所有构造器

## 2. 探索`java.lang.reflect`包

`java.lang.reflect`包中包括了几个比较重要的类：`Field`、`Method`、`Constructor`、`Modifier`

- `Field`

  在《Java核心技术卷I》中叫做域，简单理解就是对应我们对应的对象，变量。

- `Method`方法

  - `Class getReturnType()`返回一个返回类型的一个对象

- `Constructor`构造器

- `Modifier` 修饰语

  - `static String toString(int modifiers)`返回对应位置中的的字符串
  - `static boolean isAbstract(int modifiers)`
  - `static boolean isFinal(int modifiers)`
  - `static boolean isInterface(int modifiers)`
  - `static boolean isNative(int modifiers)`
  - `static boolean isPrivate(int modifiers)`
  - `static boolean isProtected(int modifiers)`
  - `static boolean isPublic(int modifiers)`
  - `static boolean isStatic(int modifiers)`
  - `static boolean isStrict(int modifiers)`
  - `static boolean isSynchronized(int modifiers)`
  - `static boolean isVolatile(int modifiers)`

- `Field  Method  Constructor`的一些方法

  - `Class getDeclaringClass()`返回定义中的Class对象
  - `int getModifier()`返回一个修饰符的值
  - `String getName()`得到名字的字符串
  - `Class[] getExceptionTypes()`得到抛出的异常类型的Class数组（Field没有）
  - `Class[] getParameterTypes()`返回一个类型参数类型的数组（Field没有）

```Java
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
```

- 通过上面的demo大家有没有想到，我们用的一些框架，里面某些地方是怎么实现的。就如数据库框架的字段对应，就可以的通过Field的getName方法取得与之匹配。
- 其实反射也是Java类的使用，但是要深入理解它的意义和用途还是挺难的。
- 未完待续。



















