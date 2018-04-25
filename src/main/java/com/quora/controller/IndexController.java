package com.quora.controller;

import com.quora.aspect.LogAspect;
import com.quora.module.Student;
import com.quora.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.nio.file.Path;
import java.util.*;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/25 9:55
 * @version: 1.0
 */
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session) {
        logger.info("index page");
        String msg = (String) session.getAttribute("msg");
        if (msg == null)
            return "This is index page.";
        else
            return "This is index page, " + msg;
    }


    //url: profile/{groupId}/{userId}?name=leon&country=CN
    @RequestMapping(path = {"profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable(value = "groupId", required = false) String groupId,
                          @PathVariable(value = "userId") String userId,
                          @RequestParam(value = "name", defaultValue = "leon", required = false) String name,
                          @RequestParam(value = "country") String country) {
        return String.format("groupId = %s, userId = %s</br>" +
                "name = %s, country = %s</br>", groupId, userId, name, country);
    }


    @RequestMapping(path = "/test")
    public String test(Model model) {
        int[] array = new int[] {1, 2, 3};
        List<String> list = Arrays.asList("a", "b", "c");
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        model.addAttribute("var", "variable");
        model.addAttribute("array", array);
        model.addAttribute("list", list);
        model.addAttribute("map", map);

        model.addAttribute("stu", new Student(10, "leon"));
        return "test";
    }

    @RequestMapping(path = "/request")
    @ResponseBody
    public String request(Model model, HttpServletRequest request,
                          HttpServletResponse response, HttpSession session) {

        StringBuilder sb = new StringBuilder();
        sb.append("method: " + request.getMethod() + "</br>");
        sb.append("servletPath: " + request.getServletPath() + "</br>");
        sb.append("requestURL: " + request.getRequestURL() + "</br>");
        sb.append("requestURI: " + request.getRequestURI() + "</br>");

        sb.append("</br>Heads: </br>");
        Enumeration<String> headNames = request.getHeaderNames();
        while (headNames.hasMoreElements()) {
            String name = headNames.nextElement();
            sb.append(name + ": " + request.getHeader(name) + "</br>");
        }


        response.addHeader("name", "leon");
        response.addCookie(new Cookie("newCookie", UUID.randomUUID().toString()));

        return sb.toString();
    }

    @RequestMapping(path = "/redirect1/{code}")
    public RedirectView redirect(@PathVariable(name = "code") int code,
                           HttpSession session) {
        session.setAttribute("msg", "redirect: " + code);
        RedirectView rv = new RedirectView("/", true);
//        if (code == 301)
//            rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return rv;
    }

    @RequestMapping(path = "/admin")
    @ResponseBody
    public String admin(@RequestParam(name = "key") String key) {
        if (key.equals("zly0318")) {
            return "Hello admin";
        }
        throw new IllegalArgumentException("非法访问");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }

    @Autowired
    SettingService settingService;

    @RequestMapping(path = "/setting")
    @ResponseBody
    public String setting() {
        return settingService.getId(10);
    }


}






