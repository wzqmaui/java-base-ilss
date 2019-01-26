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
        if (visited.contains(obj)) {
            return "...";
        }
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
                // getComponentType() 得到数组对象元素的类型， isPrimitive是判断是否是基本数据类型
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
