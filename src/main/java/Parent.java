import java.util.Objects;

/**
 * className Parent
 * description
 *
 * @author feng
 * @version 1.0
 * @date 2019/1/8 下午8:25
 */
public class Parent {
    private String name;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        //先判断对象地址是否相同，如果相同直接返回相等
        if (this == o) { return true; }
        //为空时和对象不相同时返回false
        if (o == null || getClass() != o.getClass()){ return false; }
        //向上转型
        Parent parent = (Parent) o;
        //返回值比对的结果
        return Objects.equals(name, parent.name) && Objects.equals(sex, parent.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex);
    }
}
