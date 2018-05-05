package com.quora.service;

import com.quora.dao.MessageDAO;
import com.quora.module.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 14:46
 * @version: 1.0
 */
@Service
public class MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    public int addMessage(Message message) {
        //html标签及敏感词过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveWordsService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    //获得私信未读数量
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    //更新私信未读数量, 即将has_read = 1
    public int updateConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.updateConversationUnreadCount(userId, conversationId);
    }

}
