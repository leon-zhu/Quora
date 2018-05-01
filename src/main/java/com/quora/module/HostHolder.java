package com.quora.module;

import org.springframework.stereotype.Component;

/**
 * 线程本地变量, 解决session共享问题; 这里保存当前线程用户信息
 *
 * @author: leon
 * @date: 2018/5/1 20:02
 * @version: 1.0
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
