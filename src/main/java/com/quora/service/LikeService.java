package com.quora.service;

import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/7 14:13
 * @version: 1.0
 */
@Service
public class LikeService {

    @Autowired
    private JedisAdaptor jedisAdaptor;

    public long like(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdaptor.sadd(likeKey, String.valueOf(userId)); //元素为: userId
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdaptor.srem(dislikeKey, String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }

    public long dislike(int userId, int entityId, int entityType) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityId, entityType);
        jedisAdaptor.sadd(dislikeKey, String.valueOf(userId)); //元素为: userId
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdaptor.srem(likeKey, String.valueOf(userId));
        return jedisAdaptor.scard(dislikeKey);
    }

    /**
     * 判断是否喜欢
     * like 为 1
     * dislike 为 -1
     * 否则为 0, 表示中间状态
     * @param userId
     * @param entityId
     * @param entityType
     * @return
     */
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(likeKey, String.valueOf(userId)))
            return 1;
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdaptor.sismember(dislikeKey, String.valueOf(userId))? -1 : 0;
    }

    public long getLikeCount(int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdaptor.scard(likeKey);
    }


}
