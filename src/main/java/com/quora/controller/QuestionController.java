package com.quora.controller;

import com.quora.module.*;
import com.quora.service.*;
import com.quora.util.QuoraUtils;
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

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @RequestMapping(path = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("qid") int qid, Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }

        Question question = questionService.getQuestionById(qid);
        model.addAttribute("question", question);

        model.addAttribute("user", userService.getUser(question.getUserId())); //发布该问题的用户

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESITON);
        List<ViewObject> viewObjectList = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject obj = new ViewObject();
            //点赞, 点踩
            //当前用户是否对当前实体点过赞
            if (hostHolder.getUser() == null) {
                obj.set("liked", 0);
            } else {
                obj.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            //当前是否得点赞数
            obj.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            obj.set("comment", comment);
            obj.set("user", userService.getUser(comment.getUserId()));
            viewObjectList.add(obj);
        }
        model.addAttribute("comments", viewObjectList);

        List<ViewObject> followUsers = new ArrayList<>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESITON, qid, 0, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESITON, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }


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

}
