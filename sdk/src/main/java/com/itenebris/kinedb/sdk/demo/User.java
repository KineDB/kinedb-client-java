package com.itenebris.kinedb.sdk.demo;

public class User {

    public String id;
    public String name;
    public String age;

    public User(String id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
    
}
