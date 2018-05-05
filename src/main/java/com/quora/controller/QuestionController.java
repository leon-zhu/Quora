package com.quora.controller;

import com.quora.dao.QuestionDAO;
import com.quora.module.*;
import com.quora.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/2 19:46
 * @version: 1.0
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(path = "/question/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String add(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            if (questionService.addQuestion(title, content) > 0)
                return QuoraUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("add question failed: " + e.getMessage());
        }

        return QuoraUtils.getJSONString(1, "failed");
    }

    @RequestMapping(path = "/question/{id}")
    public String questionDetail(@PathVariable("id") int id, Model model) {
        Question question = questionService.getQuestionById(id);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getComments(id, EntityType.ENTITY_QUESITON);
        List<ViewObject> viewObjectList = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject obj = new ViewObject();
            obj.set("comment", comment);
            obj.set("user", userService.getUser(comment.getUserId()));
            viewObjectList.add(obj);
        }
        model.addAttribute("comments", viewObjectList);
        return "detail";
    }

}
