package com.quora.controller;

import com.quora.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/30 9:42
 * @version: 1.0
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 注册功能
     * @param model 数据模型
     * @param name 注册名
     * @param password 注册密码
     * @return url
     */
    @RequestMapping(path = "/reg", method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("name") String name,
                      @RequestParam("password") String password,
                      HttpServletResponse response) {


        try {
            Map<String, String> res = userService.register(name, password);

            if (res.containsKey("ticket")) {
                //包含ticket, 写入cookie
                Cookie cookie = new Cookie("ticket", res.get("ticket"));
                cookie.setPath("/"); //可在同一应用服务器内共享方法
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                //不包含ticket, 说明注册发生异常
                model.addAttribute("msg", res.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            return "login";
        }
    }

    //注册页面: 访问/reglogin, 然后显示login.ftl页面
    //在login.ftl中: 登陆->/login/, 注册->/reg/
    @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
    public String reg(Model model) {
        return "login";
    }

    /**
     * 登陆功能
     * @param model 数据模型
     * @param name 登陆名
     * @param password 密码
     * @return String
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model,
                      @RequestParam("name") String name,
                      @RequestParam("password") String password,
                        @RequestParam("rememberme") boolean remember,
                        HttpServletResponse response) {

        try {
            Map<String, String> res = userService.login(name, password);

            if (res.containsKey("ticket")) {
                //将ticket添加到cookie
                Cookie cookie = new Cookie("ticket", res.get("ticket"));
                cookie.setPath("/"); //可在同一应用服务其内共享方法
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                //注册发生异常
                model.addAttribute("msg", res.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            return "login";
        }

    }

}
