# Java反射-Array类的使用

不用说大家也知道`java.lang.Array`是对Java反射包中数组操作的一个类。JavaSE8的文档中是这样说的：

***The Array class provides static methods to dynamically create and access Java arrays.***

Array类提供静态方法来动态创建和访问Java数组。访问不难理解，动态创建可以细看一下。

###让我们先看看`java.util.Arrays`

- 注意是Arrays，相信有些小伙伴已经用过很多次这个工具类了，提供了很多对数组操作的方法方便我们使用。

- 上面说了`java.lang.Array`是提供给我们静态方法来动态创建和访问数组。让我们来看看Arrays中的copyOf方式是怎么来动态操作数组的吧。

  ```Java
  public static <T> T[] copyOf(T[] original, int newLength) {
      return (T[]) copyOf(original, newLength, original.getClass());
  }
  ```

  copyOf是拿来干嘛的呢？Arrays主要提供这个方法来给已经填满的数组来拓展数组大小的。

  你可以这样用

  ```Java
  User[] users = new User[10];
  ...//假如满了,给数组长度翻倍。
  users = Arrays.copyOf(users, users.length * 2);
  ```

  不知道大家有没有注意到，这个方法是个泛型的返回结果。它的第一个参数是原始数组，第二个参数为新的长度，返回的是调用了另一个重载的`copyOf方法`，让我们来看看这个重载的`copyOf`方法吧。

  ```Java
  public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
      @SuppressWarnings("unchecked")
      T[] copy = ((Object)newType == (Object)Object[].class)
          ? (T[]) new Object[newLength]
          : (T[]) Array.newInstance(newType.getComponentType(), newLength);
      System.arraycopy(original, 0, copy, 0,
                       Math.min(original.length, newLength));
      return copy;
  }
  ```

  里面的调用不难理解，就是如果传进来的original对象数组的Class和Object[]的Class相等那就直接`new Object[]`如果不相等就调用`java.lang.reflect.Array`中的`newInstance`方法进行创建新数组，后面调用的是`System.arraycopy`方法的作用源码中的注释是：*Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.* 意思是：从指定的数组的制定位置开始复制到目标数组的指定位置。

### 为什么要用反射实现数组的扩展

- 我们来看一下不用反射实现的`"copyOf"`

  ```
  private static Object[] badCopyOf(Object[] arr, int newLength) {
      Object[] newArray = new Object[newLength];
      System.arraycopy(arr, 0, newArray, 0, Math.min(arr.length, newLength));
      return newArray;
  }
  ```

  如果没有上面那个`Arrays`的`copyOf`方法可能很多人会直接潇潇洒洒写出如上代码。不过有没有想过一个问题，他能不能转型成对应的你想用的类？这样说，一个`MyObject[]`类转成`Object[]`，然后再转回来是可以的，但是从一开始就是`Object[]`的数组是不能转成`MyObject[]`，这样做会抛出`ClassCastException`异常，这是因为这个数组是用`new Object[length]`创建的，Java数组在创建的时候回记住每个元素的类型，就是在`new`的时候的类型。

- 那么怎样我们才可以强转呢？看如下代码

  ```Java
  private static Object goodCopyOf(Object arr, int newLength) {
    Class cls = arr.getClass();
    if (!cls.isArray()) {
        return null;
    }
    Object newArray = Array.newInstance(cls.getComponentType(), newLength);
    System.arraycopy(arr,0,newArray,0,Math.min(Array.getLength(arr), newLength));
    return newArray;
  }
  ```

  看了上面代码，有的小伙伴会有疑问，为什么要用object接收数组对象，这是因为基本数据类型的数组不能传给对象数组，但是可以转成对象

  ```Java
  double[] arr = {1.1, 1.2, 1.4, 12.2};
  arr = (double[]) goodCopyOf(arr, 10);
  ```

### 访问数组内的对象

- Array类提供了一些方法可以供我们使用

| `static Object` | `get(Object array, int index)`                    | 返回指定位置的元素                  |
| --------------- | ------------------------------------------------- | ----------------------------------- |
| `static XXX`    | `getXXX(Object array, int index)`                 | XXX是基本类型，同上                 |
| `static void`   | `set(Object array, int index, Object value)`      | 设置指定位置的对象                  |
| `static void`   | `setXXX(Object array, int index, XXX z)`          | 设置指定位置的对象，XXX基本数据类型 |
| `static Object` | `newInstance(Class<?> componentType, int length)` | 新建一个对象的数组。                |

以上来自[Java SE *官方文档 https://docs.oracle.com/javase/8/docs/api/](https://docs.oracle.com/javase/8/docs/api/)



### 完整代码如下

```Java
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

```

*** 相关代码的Github：[https://github.com/ilssio/java-base-ilss](https://github.com/ilssio/java-base-ilss)  ***