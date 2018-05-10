package com.quora.service;

import com.quora.dao.FeedDAO;
import com.quora.module.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/10 19:43
 * @version: 1.0
 */
@Service
public class FeedService {

    @Autowired
    private FeedDAO feedDAO;

    public int addFeed(Feed feed) {
        return feedDAO.addFeed(feed);
    }

    public List<Feed> getUserFeeds(int maxId,
                                   List<Integer> userIds,
                                   int offset,
                                   int limit) {
        return feedDAO.selectUserFeeds(maxId, userIds, offset, limit);
    }

    public Feed getFeedById(int id) {
        return feedDAO.getFeedById(id);
    }
}
