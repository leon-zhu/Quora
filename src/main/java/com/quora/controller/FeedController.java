package com.quora.controller;

import com.quora.module.EntityType;
import com.quora.module.Feed;
import com.quora.module.HostHolder;
import com.quora.service.FeedService;
import com.quora.service.FollowService;
import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/11 10:01
 * @version: 1.0
 */
@Controller
public class FeedController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private JedisAdaptor jedisAdaptor;

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null? 0 : hostHolder.getUser().getId();
        List<String> feedIds = jedisAdaptor.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, Integer.MAX_VALUE);
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if (feed == null)
                continue;
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() == null? 0 : hostHolder.getUser().getId();
        //获取自己的关注对象id集合
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, 0, Integer.MAX_VALUE);
        }
        //拉取所关注对象的feed流
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 0, 10); //取十个
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
