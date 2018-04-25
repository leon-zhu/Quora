package com.quora.module;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/25 15:14
 * @version: 1.0
 */
public class Student {

    private int age;
    private String name;

    public Student() {
    }

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "This is " + name + ", age is " + age;
    }
}
