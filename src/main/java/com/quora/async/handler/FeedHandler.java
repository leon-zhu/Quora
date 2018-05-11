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
import com.quora.service.FollowService;
import com.quora.service.QuestionService;
import com.quora.service.UserService;
import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
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

    @Autowired
    private FollowService followService;

    @Autowired
    private JedisAdaptor jedisAdaptor;

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

        //方便测试
        Random rand = new Random();
        model.setActorId(1 + rand.nextInt(10));

        //有评论时触发, 那么创建一条feed
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null)
            return;
        feedService.addFeed(feed); //加入该条feed(评论事件触发)


        //当新鲜事儿触发时, 除了将该条feed添加到数据库; 还要将它推送(push)给自己的粉丝
        //此处可以优化, 进行分段取(比如只暂时推送给活跃用户或者在线用户)
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), 0, Integer.MAX_VALUE);
        followers.add(0); //如果没有登录的话, 那么只能看系统的队列(0代表系统)
        for (Integer followerId : followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(followerId);
            jedisAdaptor.lpush(timelineKey, String.valueOf(feed.getId()));
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT, EventType.FOLLOW);
    }
}
