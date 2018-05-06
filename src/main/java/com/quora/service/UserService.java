package com.quora.service;

import com.quora.dao.LoginTicketDAO;
import com.quora.dao.UserDAO;
import com.quora.module.LoginTicket;
import com.quora.module.User;
import com.quora.util.QuoraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 21:09
 * @version: 1.0
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    /**
     * 用户注册
     * @param name 注册姓名
     * @param password 注册密码
     * @return map
     */
    public Map<String, String> register(String name, String password) {
        Map<String, String> res = new HashMap<>();
        if (StringUtils.isEmpty(name)) {
            res.put("msg", "用户名不能为空");
            return res;
        }
        if (StringUtils.isEmpty(password)) {
            res.put("msg", "密码不能为空");
            return res;
        }
        if (getUser(name) != null) {
            res.put("msg", "用户已存在");
            return res;
        }
        //添加用户, 密码使用md5进行加密
        User user = new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5)); //加salt
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(QuoraUtils.getMD5(password + user.getSalt()));
        userDAO.addUser(user);

        //登陆注册成功, 均下发ticket
        String ticket = addLoginTicket(user.getId());
        res.put("ticket", ticket);

        return res;
    }

    /**
     * 用户登陆
     * @param name 登陆姓名
     * @param password 登陆密码
     * @return map
     */
    public Map<String, String> login(String name, String password) {
        Map<String, String> res = new HashMap<>();
        if (StringUtils.isEmpty(name)) {
            res.put("msg", "用户名不能为空");
            return res;
        }
        if (StringUtils.isEmpty(password)) {
            res.put("msg", "密码不能为空");
            return res;
        }
        User user = getUser(name);
        if (user == null) {
            res.put("msg", "用户不存在");
            return res;
        }
        //登陆密码验证
        if (!user.getPassword().equals(QuoraUtils.getMD5(password + user.getSalt()))) {
            res.put("msg", "用户名或密码错误");
            return res;
        }
        //登陆注册成功, 均下发ticket
        String ticket = addLoginTicket(user.getId());
        res.put("ticket", ticket);
        return res;
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public User getUser(String name) {
        return userDAO.selectByName(name);
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        Date date = new Date();
        date.setTime(date.getTime() + 3600*24*30); //30天
        loginTicket.setExpired(date);
        loginTicket.setStatus(0); //0-有效, 1-无效
        loginTicketDAO.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }


}
