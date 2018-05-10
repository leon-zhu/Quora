package com.quora.controller;

import com.quora.async.EventModel;
import com.quora.async.EventProducer;
import com.quora.async.EventType;
import com.quora.module.*;
import com.quora.service.CommentService;
import com.quora.service.FollowService;
import com.quora.service.QuestionService;
import com.quora.service.UserService;
import com.quora.util.QuoraUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注服务入口
 *
 * @author: leon
 * @date: 2018/5/9 21:23
 * @version: 1.0
 */
@Controller
public class FollowController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private QuestionService questionService;

    /**
     * 当前用户关注userId所对应的用户
     *
     * @param userId : 关注对象的id
     * @return 返回当前用户的关注者人数(不是粉丝数)
     */
    @RequestMapping(path = "/followUser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        }

        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        //关注用户事件分发
        eventProducer.fireEvent(new EventModel().setType(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
        .setEntityOwnerId(userId).setEntityType(EntityType.ENTITY_USER).setEntityId(userId));
        //将当前用户关注的用户数也返回
        return QuoraUtils.getJSONString(res? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = "/unfollowUser", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId) {
        if (hostHolder.getUser() == null) {
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        }

        boolean res = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        //取消关注用户事件分发(这里进行提醒服务，一般只有关注才提醒, 暂时取消)
//        eventProducer.fireEvent(new EventModel().setType(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
//                .setEntityOwnerId(userId).setEntityType(EntityType.ENTITY_USER).setEntityId(userId));
        //将当前用户关注的用户数也返回
        return QuoraUtils.getJSONString(res? 0 : 1,
                String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    /**
     * 关注问题入口
     *
     * @param questionId 问题对象id
     * @return JSON串
     */
    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        }

        //获取该问题
        Question q = questionService.getQuestionById(questionId);
        if (q == null) {
            return QuoraUtils.getJSONString(1, "问题不存在");
        }

        boolean res = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESITON, questionId);
        //关注问题事件分发
        eventProducer.fireEvent(new EventModel().setType(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(q.getUserId()).setEntityType(EntityType.ENTITY_QUESITON).setEntityId(questionId));
        //关注问题时, 关注页面下显示关注者的头像等信息 - 故添加对应用户信息
        Map<String, Object> map = new HashMap<>();
        map.put("headUrl", hostHolder.getUser().getHeadUrl());
        map.put("name", hostHolder.getUser().getName());
        map.put("id", hostHolder.getUser().getId());
        map.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESITON, questionId)); //当前问题下的关注粉丝数
        return QuoraUtils.getJSONString(res? 0 : 1, map);
    }

    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.getUser() == null) {
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        }
        //获取问题
        Question q = questionService.getQuestionById(questionId);
        if (q == null) {
            return QuoraUtils.getJSONString(1, "问题不存在");
        }

        //问题的entityOwnerId还是用户id
        boolean res = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESITON, questionId);
        //取消关注问题的事件分发(取消关注的时候我们不再提醒)
//        eventProducer.fireEvent(new EventModel().setType(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
//                .setEntityOwnerId(q.getUserId()).setEntityType(EntityType.ENTITY_QUESITON).setEntityId(questionId));
        //取消关注问题时, 关注页面下显示关注者的头像等信息 - 故添加对应用户信息
        Map<String, Object> map = new HashMap<>();
        map.put("id", hostHolder.getUser().getId());
        map.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESITON, questionId));
        return QuoraUtils.getJSONString(res ? 0 : 1, map);
//        Map<String, Object> map = new HashMap<>();
//        map.put("headUrl", hostHolder.getUser().getHeadUrl());
//        map.put("name", hostHolder.getUser().getName());
//        map.put("id", hostHolder.getUser().getId());
//        map.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESITON, questionId)); //当前问题下的关注粉丝数
//        return QuoraUtils.getJSONString(res? 0 : 1, map);
    }

    @RequestMapping(path = "/user/{uid}/followers", method = RequestMethod.GET)
    public String followers(@PathVariable("uid") int userId, Model model) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        //后来添加的
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @RequestMapping(path = "/user/{uid}/followees", method = RequestMethod.GET)
    public String followees(@PathVariable("uid") int userId, Model model) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }




    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> res = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) continue;
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid)); //用户的评论数
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            res.add(vo);
        }
        return res;
    }



}

















