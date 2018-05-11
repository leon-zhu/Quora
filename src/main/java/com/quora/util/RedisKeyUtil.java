package com.quora.util;

import org.apache.catalina.authenticator.SingleSignOnListener;

/**
 * 生成Redis的key, 避免随意的key取值, 出现数据覆盖
 *
 * @author: leon
 * @date: 2018/5/7 15:43
 * @version: 1.0
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";
    private static String BIZ_FOLLOWER = "FOLLOWER"; //粉丝: 发起关注的人
    private static String BIZ_FOLLOWEE = "FOLLOWEE"; //关注者: 被关注的人
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getTimelineKey(int userId) {
        return  BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

    //该实体的所有粉丝的key: 如关注该问题的所有粉丝, 关注该用户的所有粉丝
    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    //某用户关注某一类实体的key: 如用户A关注的所有人的key, 用户A关注的所有问题的key
    public static String getFolloweeKey(int userId, int entityType) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }


    //该实体的所有点赞者的key
    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }


}



