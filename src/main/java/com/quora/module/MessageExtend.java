package com.quora.module;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 19:43
 * @version: 1.0
 */

public class MessageExtend {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;
    private int count; //共有多少条记录

    public MessageExtend() {
    }

    public MessageExtend(int id, int fromId, int toId, String content, Date createdDate, int hasRead, String conversationId, int count) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.createdDate = createdDate;
        this.hasRead = hasRead;
        this.conversationId = conversationId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "MessageExtend{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", hasRead=" + hasRead +
                ", conversationId='" + conversationId + '\'' +
                ", count=" + count +
                '}';
    }
}
