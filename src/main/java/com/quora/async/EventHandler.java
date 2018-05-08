package com.quora.async;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/8 15:04
 * @version: 1.0
 */
public interface EventHandler {

    //处理该事件模型, 但只能处理其能够处理的类型, 见getSupportEventTypes()接口
    void doHandler(EventModel model);

    //该EventHandler所关心的EventType -- 即能够处理的事件类型
    List<EventType> getSupportEventTypes();
}
