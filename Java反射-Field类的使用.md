# Java反射-Field类使用

Field作为反射中对应类或对象中的域或者叫做属性的操作类，除了我前一篇文章中的得到名字和类型等，Field的作用不限于此。

*[上篇文章：Java反射-初探反射基础)(点击进入)](https://#) *

Java SE 8的Docs这样说：*A Field provides information about, and dynamic access to, a single field of a class or an interface. The reflected field may be a class (static) field or an instance field.*

简单理解就是：我们可用通过Field类对类或对象的field进行动态操作。

- 关于Field的一些方法：

| 返回值    | 名字和参数                      | 作用                                          |
| :-------- | :------------------------------ | :-------------------------------------------- |
| `Object`  | `get(Object obj)`               | 返回这个object对应field字段的Object           |
| `xxx`     | `getXXX(Object obj)`            | 同上，不过XXX可以是Int,Char,Boolean等         |
| `void`    | `set(Object obj, Object value)` | 设置obj对象的调用方法的这个field的值为value   |
| `void`    | `setXXX(Object obj, XXX value)` | 设置特定类型值,例setInt(Object obj,int value) |
| `Class`   | `getDeclaringClass()`           | 返回定义中的Class对象                         |
| `String`  | `getName()`                     | 得到名字的字符串                              |
| `int `    | `getModifier()`                 | 返回一个修饰符的值                            |
| `Class `  | `getType()`                     | 返回这个field的对象Class                      |
| `void`    | `setAccessible(boolean flag)`   | 设置是否允许set get                           |
| `boolean` | `isAccessible()`                | 查看field是否允许set和get。                   |
| ...       | ...                             | ...                                           |

*其他的可以去Java的官方文档去查看。[https://docs.oracle.com/javase/8/docs/api/](https://docs.oracle.com/javase/8/docs/api/)*

- 简单案例：

  ```Java
  package io.ilss.reflection;
  
  /**
   * className User
   * description User
   *
   * @author feng
   * @version 1.0
   * @date 2019-01-26 19:11
   */
  public class User {
      private String username;
      private String password;
      private int sex;
      public String address; //注意这个是public
  
      public User() {
      }
  
      public User(String username, String password) {
          this.username = username;
          this.password = password;
      }
      
      ...//getter setter method
  }
  ```

  ```Java
  package io.ilss.reflection;
  
  import java.lang.annotation.Annotation;
  import java.lang.reflect.Field;
  
  /**
   * className UserTest
   * description UserTest
   *
   * @author feng
   * @version 1.0
   * @date 2019-01-26 19:12
   */
  public class UserTest {
      public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
          User user = new User("ilss", "ilss password");
          Class cls = user.getClass();
          //获取username名字的Field
          Field usernameField = cls.getDeclaredField("username");
          System.out.println(usernameField.toString());
  
          //获取user对象对应的username
          Object o = getSafe(usernameField, user);
          System.out.println("The username value of user is " + o);
  
          //获取基本数据类型
          user.setSex(1);
          Field sexField = cls.getDeclaredField("sex");
          sexField.setAccessible(true);
          sexField.setInt(user, 2);
          int sex = sexField.getInt(user);
          System.out.println("user sex : " + sex);
  
          //对user进行修改值 pubic不需要setAccessible
          Field addressField = cls.getDeclaredField("address");
          addressField.set(user, "ilss address");
          System.out.println("user address : " + user.getAddress());
  
  
      }
  
      public static Object getSafe(Field field, Object obj) {
          Object ret = null;
          try {
              if (field.isAccessible()) {
                  ret = field.get(obj);
  
              } else {
                  field.setAccessible(true);
                  ret = field.get(obj);
              }
          } catch (IllegalAccessException e) {
              e.printStackTrace();
          }
          return ret;
      }
  }
  
  ```

  需要说的点：

  - setAccessible方法。是Field继承自AccessibleObject类，AccessibleObject是Field、Method、Constuctor类的父类。简单理解意思就是 如果类型是private修饰的，你不可以直接访问，就需要设置访问权限为true.如果是public则不需要设置。
  - set和get调用的时候都需要确保可以访问，不然如果不能访问抛出IllegalAccessException。

- 制作一个通用的toString(Object obj)方法

```Java
package io.ilss.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * className ObjectAnalyzer
 * description ObjectAnalyzer
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-26 19:34
 */
public class ObjectAnalyzer {
    private ArrayList<Object> visited = new ArrayList<>();
    
    public String toString(Object obj) {
        //无效处理
        if (obj == null) {
            return "null";
        }
        //注意 如果不判断contains可能会出现无限递归调用。
        if (visited.contains(obj)) {
            return "...";
        }
        //将已经处理的做记录
        visited.add(obj);
        Class cls = obj.getClass();
        //如果为String类型就直接返回
        if (cls == String.class) {
            return (String) obj;
        }

        //是否数组
        if (cls.isArray()) {
            //用StringBuilder来append字符串
            StringBuilder r = new StringBuilder(cls.getComponentType() + "[]{");
            //利用relect包的Array操作类来便利
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0) {
                    r.append(",");
                }
                Object val = Array.get(obj, i);
                // getComponentType() 得到数组对象元素的类型
                // isPrimitive()是判断是否是基本数据类型
                if (cls.getComponentType().isPrimitive()) {
                    r.append(val);
                } else {
                    //不是基本数据类型就递归调用。
                    r.append(toString(val));
                }
            }
            return r + "}";
        }


        StringBuilder ret = new StringBuilder(cls.getName());
        do {
            ret.append("[");

            Field[] fields = cls.getDeclaredFields();
            //对fields数组的所有访问权限设置为true
            AccessibleObject.setAccessible(fields, true);

            for (Field f : fields) {
                //只对非static参数处理
                if (!Modifier.isStatic(f.getModifiers())) {
                    //如果不是第一个就加逗号
                    if (!ret.toString().endsWith("[")) {
                        ret.append(",");
                    }
                    ret.append(f.getName()).append("=");
                    try {
                        //对field的值进行获取
                        Class t = f.getType();
                        Object val = f.get(obj);
                        //基本数据类型就直接append 不是就递归调用
                        if (t.isPrimitive()) {
                            ret.append(val);
                        } else {
                            ret.append(toString(val));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ret.append("]");
            //处理父类
            cls = cls.getSuperclass();
        }
        while (cls != null);

        return ret.toString();
    }
    
    @Override
    public String toString() {
        //利用自己调用toString(this)来实现自己的toString
        return this.toString(this);
    }
}
```

注意：

里面有个Array类，是java.lang.reflect包下一个数组操作类。代码里的调用意思都不难理解。就不解释了

- 调用类

```Java
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

```

相关代码的github：[https://github.com/ilssio/java-base-ilss](https://github.com/ilssio/java-base-ilss)



