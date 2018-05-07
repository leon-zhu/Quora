package com.quora.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 演示Jedis的使用
 *
 * @author: leon
 * @date: 2018/5/6 14:12
 * @version: 1.0
 */
@Component
public class JedisAdaptor implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdaptor.class);


    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/8"); //连接池
        System.out.println("已创建连接池");
    }

    /**
     * 向集合中添加元素
     *
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("添加数据异常: " + e.getMessage());
        } finally {
            if (jedis != null) jedis.close();
        }
        return 0;
    }

    /**
     * 从集合中删除元素
     *
     * @param key
     * @param value
     * @return
     */
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("删除数据异常: " + e.getMessage());
        } finally {
            if (jedis != null) jedis.close();
        }
        return 0;
    }

    /**
     * 获取集合中的元素数量
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("获取集合数量异常: " + e.getMessage());
        } finally {
            if (jedis != null) jedis.close();
        }
        return 0;
    }

    /**
     * 判断元素是否存在于集合中
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("判断元素是否存在异常: " + e.getMessage());
        } finally {
            if (jedis != null) jedis.close();
        }
        return false;
    }
}