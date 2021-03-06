package com.quora.controller;

import com.quora.module.*;
import com.quora.service.CommentService;
import com.quora.service.FollowService;
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
 * 知乎首页展示
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

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;


    /**
     * 用户页面, 对应于userId
     *
     * @param model  模型
     * @param userId 用户id
     * @return index.ftl
     */
    @RequestMapping(path = {"/user/{userId}"})
    public String index(Model model, @PathVariable("userId") int userId) {

        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    /**
     * 首页, userId为0
     *
     * @param model 模型
     * @return index.ftl
     */
    @RequestMapping(path = {"/", "/index"})
    public String index(Model model) {

        List<ViewObject> viewObjects = getQuestions(0, 0, 10);
        model.addAttribute("viewObjects", viewObjects);
        return "index";
    }

    /**
     * 选取最新的问题列表,
     *
     * @param userId 用户id
     * @param offset 偏移大小
     * @param limit  问题数量
     * @return 问题list
     */
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLateseQuestions(userId, offset, limit);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            //这行可能会导致freemarker出现空指针(提问时userId乱填导致的)
            vo.set("user", userService.getUser(question.getUserId())); //发布该问题的用户
            vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESITON, question.getId()));
            viewObjects.add(vo);
        }
        return viewObjects;
    }

}
