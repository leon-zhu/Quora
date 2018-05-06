package com.quora.util;

import com.alibaba.fastjson.JSONObject;
import com.quora.module.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * 演示Jedis的使用
 *
 * @author: leon
 * @date: 2018/5/6 14:12
 * @version: 1.0
 */
public class JedisAdaptor {
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d: %s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/1");
        jedis.flushDB(); //删除当前所选数据库的keys
        //get, set使用
        jedis.set("h", "world");
        print(1, jedis.get("h"));
        jedis.rename("h", "h1"); //更改key的名字
        print(2, jedis.get("h1"));
        jedis.setex("h2", 10, "world"); //设置过期时间
        print(3, jedis.get("h2"));

        //数值运算
        jedis.set("pv", "100"); //page view - 页面浏览量
        jedis.incr("pv"); //加1
        jedis.incrBy("pv", 5); //加任意数值
        jedis.decr("pv"); //减1
        jedis.decrBy("pv", 10); //减任意数值
        print(4, jedis.get("pv"));

        //打印出所有的keys, 支持正则表达式
        print(5, jedis.keys("*"));

        //list
        String listName = "list";
        jedis.del(listName);
        for (int i =0; i < 10; i++) {
            jedis.lpush(listName, "value" + String.valueOf(i));
        }
        print(6, jedis.lrange(listName, 0, jedis.llen(listName)));
        print(7, jedis.lrange(listName, 0, 3)); //双闭区间
        print(8, jedis.llen(listName));
        print(9, jedis.lpop(listName));
        print(10, jedis.llen(listName));
        print(11, jedis.lrange(listName, 1, 3));
        print(12, jedis.lindex(listName, 0));
        jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "value8", "newValue");
        print(13, jedis.lrange(listName, 0, jedis.llen(listName)));

        //Hash
        String userKey = "user";
        jedis.hset(userKey, "name", "leon");
        jedis.hset(userKey, "age", "22");
        jedis.hset(userKey, "phone", "1311508186");
        print(14, jedis.hget(userKey, "name"));
        print(15, jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        print(16, jedis.hgetAll(userKey));
        print(17, jedis.hexists(userKey, "name"));
        print(18, jedis.hexists(userKey, "email"));
        print(19, jedis.hkeys(userKey));
        print(20, jedis.hvals(userKey));
        jedis.hsetnx(userKey, "name", "hanna"); //value非空才进行设置
        jedis.hsetnx(userKey, "email", "lyzhu0318@163.com");
        print(21, jedis.hgetAll(userKey));

        //集合set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(22, jedis.smembers(likeKey1));
        print(23, jedis.smembers(likeKey2));
        print(24, jedis.sunion(likeKey1, likeKey2)); //集合并
        print(25, jedis.sdiff(likeKey1, likeKey2));
        print(25, jedis.sdiff(likeKey2, likeKey1));
        print(26, jedis.sinter(likeKey1, likeKey2)); //集合交
        print(27, jedis.sismember(likeKey1, "11"));
        print(28, jedis.sismember(likeKey1, "3"));
        jedis.srem(likeKey1, "5"); //删除
        print(29, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25"); //从后面移动某元素到前面
        print(30, jedis.smembers(likeKey1));
        print(31, jedis.smembers(likeKey2));
        print(32, jedis.scard(likeKey1));

        //sorted set (zset)
        String rankKey = "rankKey";
        jedis.zadd(rankKey, 177, "leon");
        jedis.zadd(rankKey, 140, "lucy");
        jedis.zadd(rankKey, 58, "hanna");
        print(33, jedis.zcard(rankKey));
        print(34, jedis.zcount(rankKey, 20, 50));
        print(35, jedis.zrange(rankKey, 0, 100)); //第0到第100名, 不是指得分
        print(36, jedis.zscore(rankKey, "leon"));
        jedis.zincrby(rankKey, 20, "leon");
        print(37, jedis.zrange(rankKey, 0, 200));
        print(38, jedis.zscore(rankKey, "leon"));
        print(39, jedis.zrevrange(rankKey, 0, 2)); //闭区间, 反序
        //根据分值范围来排序
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, 10, 150)) {
            print(40, tuple.getElement() + ":" + tuple.getScore());
        }
        //获取其排名
        print(41, jedis.zrank(rankKey, "leon"));
        print(42, jedis.zrevrank(rankKey, "leon"));

        String setKey = "zset";
        jedis.zadd(setKey, 10, "a");
        jedis.zadd(setKey, 10, "b");
        jedis.zadd(setKey, 10, "c");
        jedis.zadd(setKey, 10, "d");
        print(43, jedis.zlexcount(setKey, "-", "+"));
        print(44, jedis.zlexcount(setKey, "[b", "[d")); //双闭区间
        jedis.zrem(setKey, "b");
        print(45, jedis.zlexcount(setKey, "(a", "(c")); //双开区间
        jedis.zremrangeByLex(setKey, "(a", "+"); //删除字典序a后面的
        print(46, jedis.zrange(setKey, 0, 20));

        //连接池的操作
        /*JedisPool pool = new JedisPool("redis://localhost:6379/1");
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            print(47, j.get("pv"));
            j.close(); //记得及时关闭, 默认最大8条缓存
        }*/

        User user = new User();
        user.setName("name");
        user.setPassword("passwd");
        user.setHeadUrl("a.png");
        jedis.set("user1", JSONObject.toJSONString(user));
        print(50, jedis.get("user1"));

        String value2 = jedis.get("user1");
        User user2 = JSONObject.parseObject(value2, User.class);
        System.out.println(user2);



    }
}
