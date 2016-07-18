package com.Qiao.model;

/**
 * Created by white and black on 2016/7/16.
 */
public class User {
    private String name;
    private String description;
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name)
    {
        this.name=name;

    }
}
