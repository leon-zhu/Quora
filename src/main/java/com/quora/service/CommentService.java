package com.quora.service;

import com.quora.dao.CommentDAO;
import com.quora.module.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 10:56
 * @version: 1.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.getCommentsByEntity(entityId, entityType);
    }

    public int addComment(Comment comment) throws Exception {
        //进行html过滤及敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveWordsService.filter(comment.getContent()));
        return commentDAO.addComment(comment);
    }

    //获取该用户对所有实体的评论数
    public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }


    public Comment getCommentById(int id) {
        return commentDAO.getCommentById(id);
    }


}
