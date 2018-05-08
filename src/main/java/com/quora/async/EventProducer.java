package com.quora.async;

import com.alibaba.fastjson.JSONObject;
import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 事件分发入口 fire the event
 *
 * @author: leon
 * @date: 2018/5/8 14:45
 * @version: 1.0
 */
@Service
public class EventProducer {
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisAdaptor jedisAdaptor;


    /**
     * 分发事件，即: 将事件保存到队列里
     *
     * @param eventModel 事件模型
     * @return 是否成功
     */
    public boolean fireEvent(EventModel eventModel) {
        try {
            //序列化
            String json = JSONObject.toJSONString(eventModel); //将该对象转换成json字符串格式
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdaptor.lpush(key, json);
            return true;
        } catch (Exception e) {
            logger.error("分发事件出错: " + e.getMessage());
        }
        return false;
    }
}
