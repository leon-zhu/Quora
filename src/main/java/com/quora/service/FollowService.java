package com.quora.service;

import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 关注
 *
 * @author: leon
 * @date: 2018/5/9 19:12
 * @version: 1.0
 */
@Service
public class FollowService {

    @Autowired
    private JedisAdaptor jedisAdaptor;

    /**
     * 若某用户进行关注, 那么要做两件事儿
     * 1. 把自己放到实体的粉丝列表里
     * 2. 把关注的对象放到关注对象列表里
     *
     * @param userId 用户id
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 是否关注成功
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction ts = jedisAdaptor.multi(jedis);
        Date date = new Date();
        //1 将该用户加到实体的粉丝列表, 用id关联
        ts.zadd(followerKey, date.getTime(), String.valueOf(userId));
        //2 将对象放入关注列表中, 用id关联
        ts.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> res = jedisAdaptor.exec(ts, jedis);

        return res.size() == 2 && (long)res.get(0) > 0 && (long)res.get(1) > 0;
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction ts = jedisAdaptor.multi(jedis);
        Date date = new Date();
        //1 将该用户加到实体的粉丝列表, 用id关联
        ts.zrem(followerKey, String.valueOf(userId));
        //2 将对象放入关注列表中, 用id关联
        ts.zrem(followeeKey, String.valueOf(entityId));
        List<Object> res = jedisAdaptor.exec(ts, jedis);

        return res.size() == 2 && (long)res.get(0) > 0 && (long)res.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> set) {
        List<Integer> list = new ArrayList<>();
        for (String s : set) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    /**
     * 获得所有的粉丝列表id
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Set<String> set = jedisAdaptor.zrevrange(followerKey, offset, offset + count); //获取粉丝范围[offset, count], score按时间排
        return getIdsFromSet(set);

    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Set<String> set = jedisAdaptor.zrevrange(followeeKey, offset, offset + count); //获取粉丝范围[offset, count], score按时间排
        return getIdsFromSet(set);
    }

    /**
     * 获得实体对应的粉丝数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdaptor.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdaptor.zcard(followeeKey);
    }

    /**
     * 判断该用户是不是该实体的粉丝
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdaptor.zrank(followerKey, String.valueOf(userId)) != null;

    }

}
