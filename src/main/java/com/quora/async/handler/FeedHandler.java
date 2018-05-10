package com.quora.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.quora.async.EventHandler;
import com.quora.async.EventModel;
import com.quora.async.EventType;
import com.quora.module.EntityType;
import com.quora.module.Feed;
import com.quora.module.Question;
import com.quora.module.User;
import com.quora.service.FeedService;
import com.quora.service.QuestionService;
import com.quora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 当有评论时调用此FeedHandler
 *
 * @author: leon
 * @date: 2018/5/10 22:09
 * @version: 1.0
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FeedService feedService;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User user = userService.getUser(model.getActorId());
        if (user == null) {
            return null;
        }
        map.put("userId", String.valueOf(user.getId()));
        map.put("userHeadUrl", user.getHeadUrl());
        map.put("userName", user.getName());
        //评论实体是问题
        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESITON)) {
            Question q = questionService.getQuestionById(model.getEntityId());
            if (q == null) {
                return null;
            }
            map.put("questionId", String.valueOf(q.getId()));
            map.put("questionTitle", q.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }


    @Override
    public void doHandler(EventModel model) {
        //有评论时触发, 那么创建一条feed
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null)
            return;
        feedService.addFeed(feed); //加入该条feed(评论事件触发)

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT, EventType.FOLLOW);
    }
}
