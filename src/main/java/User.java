package main.java;

import java.util.Objects;

public class User {
    private static int id = 1;
    private int userid;
    private String firstname;
    private String lastname;
    private String username;
    private String password;

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.userid = id;
        id++;
    }

    public int getId(){
        return id;
    }

    public int getUserid(){
        return userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return userid == user.userid;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userid);
    }
}
