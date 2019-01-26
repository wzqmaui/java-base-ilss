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
