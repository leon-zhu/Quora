package com.quora.async;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/8 14:17
 * @version: 1.0
 */
@Component
public class EventModel {
    private EventType type; //事件类型 - 点赞事件/评论
    private int actorId; //事件触发者 - 谁点的赞/谁发的评论
    //关联触发的载体 - 给哪个东西点赞/给哪个东西发的评论
    private int entityId;
    private int entityType;
    private int entityOwnerId; //与触发的载体相关的对象 - 如站内信 - 此表示 给谁的站内信

    private Map<String, String> exts = new HashMap<>();

    public EventModel() {
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
