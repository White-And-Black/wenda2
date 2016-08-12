package com.Qiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by white and black on 2016/8/12.
 */
@Component
public class HostHolder {
    private  static ThreadLocal<User> users=new ThreadLocal<User>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
