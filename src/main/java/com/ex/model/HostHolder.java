package com.ex.model;

import org.springframework.stereotype.Component;

/**
 * Created by xc on 17-2-25.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<User>();//所有用户加入本地线程池
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
