package io.ilss.generic;

/**
 * className MyObject
 * description MyObject
 *
 * @author feng
 * @version 1.0
 * @date 2019-01-24 18:32
 */
public final class MyObject<T> extends BaseData {
    private T valueOne;
    private T valueTwo;
    public static String str ="ss";
    int aa = 1;


    @Override
    public String toString() {
        return "MyObject{" +
                "valueOne=" + valueOne +
                ", valueTwo=" + valueTwo +
                '}';
    }

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
