package com.quora.controller;

import com.quora.dao.QuestionDAO;
import com.quora.module.Comment;
import com.quora.module.EntityType;
import com.quora.module.HostHolder;
import com.quora.service.CommentService;
import com.quora.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 11:01
 * @version: 1.0
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreatedDate(new Date());
        comment.setEntityId(questionId);
        comment.setEntityType(EntityType.ENTITY_QUESITON);
        comment.setStatus(0);
        if (hostHolder != null)
            comment.setUserId(hostHolder.getUser().getId());
        else
            return "redirect:/reglogin"; //只允许登陆用户评论
        try {
            commentService.addComment(comment);
            //需要事物控制Transaction
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(questionId, count);

        } catch (Exception e) {
            logger.info("增加评论失败");
        }
        return "redirect:/question/" + questionId;

    }

}
