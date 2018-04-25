package com.quora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/25 9:55
 * @version: 1.0
 */
@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/index"})
    @ResponseBody
    public String index() {
        return "This is index page.";
    }
}
