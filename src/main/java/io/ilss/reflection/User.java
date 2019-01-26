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
    public String address;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
