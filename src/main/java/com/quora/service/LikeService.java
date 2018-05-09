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

    /**
     * 点赞
     *
     * @param userId: set中的值
     * @param entityType : 实体类型, 比如问题、评论
     * @param entityId: 实体Id
     * @return 对当前实体的点赞数, 里面存的是点赞人的id
     */
    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdaptor.sadd(likeKey, String.valueOf(userId)); //元素为: userId
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdaptor.srem(dislikeKey, String.valueOf(userId));
        return jedisAdaptor.scard(likeKey);
    }

    /**
     * 点踩
     *
     * @param userId set中的值
     * @param entityType 实体类型, 比如问题、评论
     * @param entityId 实体Id
     * @return 尽管是点踩, 但是返回的仍然是点赞人的数量
     */
    public long dislike(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        jedisAdaptor.sadd(dislikeKey, String.valueOf(userId)); //元素为: userId
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdaptor.srem(likeKey, String.valueOf(userId));
        return jedisAdaptor.scard(likeKey); //有些疑惑
    }

    /**
     * 判断当前用户是否对该实体点过赞
     *
     * like 为 1
     * dislike 为 -1
     * 否则为 0, 表示中间状态
     * @param userId 浏览用户
     * @param entityId 实体id
     * @param entityType 实体类型
     * @return 返回当前用户对这个实体是否点过赞
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(likeKey, String.valueOf(userId)))
            return 1;
        String dislikeKey = RedisKeyUtil.getDislikeKey(entityType, entityId);
        return jedisAdaptor.sismember(dislikeKey, String.valueOf(userId))? -1 : 0;
    }

    /**
     * 获得某实体的点赞数
     *
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 该实体收到的所有的点赞数
     */
    public long getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        System.out.println("调用LikeService中的getLikeCount函数: entityType = " + entityType + ", entityId = " + entityId);

        return jedisAdaptor.scard(likeKey);
    }


}
