package com.quora.async;

import com.alibaba.fastjson.JSON;
import com.quora.util.JedisAdaptor;
import com.quora.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/8 15:09
 * @version: 1.0
 */
@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private JedisAdaptor jedisAdaptor;

    //EventType:事件类型
    //List<EventHandler>: 待处理的EventHandler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        //主要对config进行初始化注册

        //找出当前工程所有实现了EventHandler接口的类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
                //当前handler支持的事件类型
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue()); //将eventType和EventHandler关联起来
                }
            }
        }

        //启动线程, 进行处理事件
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    //弹出一共两个String, 第一个是key, 第二个才是队列中保存的值
                    List<String> events = jedisAdaptor.brpop(0, key);
                    //System.out.println("size: " + events.size()); //输出2

                    EventModel eventModel = JSON.parseObject(events.get(1), EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        logger.error("不能识别的事件...");
                        continue;
                    }
                    //找到关联的handler, 一个一个处理
                    for (EventHandler handler : config.get(eventModel.getType())) {
                        handler.doHandler(eventModel);
                    }
                }
            }
        }).start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
