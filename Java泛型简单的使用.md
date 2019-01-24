# Java泛型简单的使用

## 前言

- Java里面的泛型在实际开发中运用的很多，学过C++的同学一定知道C++的模板，而Java中的泛型，一定程度上和它还是挺像的。

- 相信写Java的人，大都有用过List的实现类ArrayList。在Java没有泛型之前，它的内部是一个Object的数组实现的。这也导致一个问题，每次使用里面的元素的时候需要向下转型，而且很明显，如果是Object的话，意味着我们可以丢任何对象进去。自动转型成Object。如：

  ```java
  ArrayList list = new ArrayList();
  list.add("string1");
  list.add("string2");
  String str = (String) list.get(0);
  list.add(new File("test.txt"));
  ```

- 然而使用泛型容易，我们就经常使用List的泛型，但是如果我们要写一个泛型的类其实并不那么容易。

## 最简单的泛型

```Java
package io.ilss.advanced.generic;

/**
 * className MyObject
 * description MyObject
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 18:32
 */
public class MyObject<T> extends BaseData {
    private T valueOne;
    private T valueTwo;

    public MyObject(T valueOne, T valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }

    public T getValueOne() {
        return valueOne;
    }

    public void setValueOne(T valueOne) {
        this.valueOne = valueOne;
    }

    public T getValueTwo() {
        return valueTwo;
    }

    public void setValueTwo(T valueTwo) {
        this.valueTwo = valueTwo;
    }

    public static void main(String[] args) {
        MyObject<String> object = new MyObject<>("String one!", "String two");
        System.out.println("value one " + object.valueOne + " value two " + object.valueTwo);
    }
}
```

- 在MyObject中引入一个类型变量T，用尖括号<>括起来，放在类名的后面。如上！在定义的时候可以有多个类型的变量，在<>中以逗号,分隔例如public class MyObject<T, U>{…},在类中如果要使用类型 就直接把定义的类型变量看做类名使用即可。

## 用泛型做一个接口返回类

- 相信有一定编码经验的人都用过工具类，那我们用泛型来封装一个统一响应返回的类ResponseMsg。

```Java
package io.ilss.advanced.generic;

/**
 * className ResponseMsg
 * description ResponseMsg
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 18:47
 */
public class ResponseMsg<T extends BaseData> {
    public static int SUCCESS_CODE = 1;
    public static int ERROR_CODE = 0;
    public static int OTHER_CODE = -1;
    private int code;
    private String msg;
    private T data;

    public static <U extends BaseData> ResponseMsg sendSuccess(U data) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = SUCCESS_CODE;
        responseMsg.data = data;
        responseMsg.msg = "Remote Call Success!";
        return responseMsg;
    }

    public static <U extends BaseData> ResponseMsg sendError(U data, String msg) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = ERROR_CODE;
        responseMsg.data = data;
        responseMsg.msg = "Remote Call Error";
        return responseMsg;
    }
    public static <U extends BaseData> ResponseMsg sendOther(U data, String msg) {
        ResponseMsg<U> responseMsg = new ResponseMsg<>();
        responseMsg.code = OTHER_CODE;
        responseMsg.data = data;
        responseMsg.msg = msg;
        return responseMsg;
    }

    public static void main(String[] args) {
        System.out.println(ResponseMsg.<MyObject>sendSuccess(new MyObject<String>("asdf","asfd")));
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

```

- 上面的重点是上的<U> 我利用静态方法封装了ResponseMsg的构建，只需要提供静态方法让调用的人传入一个类，也可以不用写Getter Setter方法,或者直接变成私有方法。
- 方法如果要用泛型，则只需要在定时的时候在返回类型前面加上<T>即可使用。调用的时候直接在调用方法前面用<>传入你想用的类即可。如上所示，也可以将方法用的泛型直接传入自己类定义的泛型。
- 此外还可以通过extends限定你是某某类的子类，或者实现了某个接口。如果有多个接口可以用&连接，比如<T extends Comparable & Serializable>，如果有多个泛型可以是这样<T extends OneObject, U extends TwoObject>

## 注意

- 泛型不能直接new，需要外部传入。

  如：

  ```
   T data = new T(); //这是不被允许的，不能实例化对象
   T[] arr = new T[10] //这也是不被允许的  不能构造泛型数组
  ```

  

  