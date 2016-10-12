package com.funtest.analysis.bean;

/**
 * @author admin
 * @create 2016-10-12 11:18
 */
public class SimpleUser {
    Integer id;
    String name;

    public Integer getId() {
        return id;
    }

    public SimpleUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SimpleUser setName(String name) {
        this.name = name;
        return this;
    }
}
