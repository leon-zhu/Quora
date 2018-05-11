package com.quora.module;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/10 19:37
 * @version: 1.0
 */
@Component
public class Feed {
    private int id;
    private int type; //新鲜事类型, 渲染模式不同
    private int userId; //谁发出的
    private Date createdDate;
    private String data; //数据, JSON格式

    //辅助变量
    private JSONObject dataJSON = null;

    public Feed() {
    }

    public Feed(int id, int type, int userId, Date createdDate, String data) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.createdDate = createdDate;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON == null? null : dataJSON.getString(key);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", type=" + type +
                ", userId=" + userId +
                ", createdDate=" + createdDate +
                ", data='" + data + '\'' +
                '}';
    }
}
