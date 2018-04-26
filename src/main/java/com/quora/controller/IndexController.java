package com.quora.controller;

import com.quora.module.Question;
import com.quora.module.ViewObject;
import com.quora.service.QuestionService;
import com.quora.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 20:55
 * @version: 1.0
 */

@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;


    @RequestMapping(path = {"/user/{userId}"})
    public String index(Model model, @PathVariable("userId") int userId) {
        List<ViewObject> viewObjects = getQuestions(userId, 0, 10);
        model.addAttribute("viewObjects", viewObjects);
        return "index";
    }

    @RequestMapping(path = {"/", "/index"})
    public String index(Model model) {

        List<ViewObject> viewObjects = getQuestions(0, 0, 10);
        model.addAttribute("viewObjects", viewObjects);
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLateseQuestions(userId, offset, limit);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            viewObjects.add(vo);
        }
        return viewObjects;
    }

}
