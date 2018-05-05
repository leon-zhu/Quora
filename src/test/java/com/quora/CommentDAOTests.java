package com.quora;

import com.quora.dao.CommentDAO;
import com.quora.dao.LoginTicketDAO;
import com.quora.dao.QuestionDAO;
import com.quora.dao.UserDAO;
import com.quora.module.Comment;
import com.quora.module.LoginTicket;
import com.quora.module.Question;
import com.quora.module.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 11:24
 * @version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql") //加上这条语句每次测试之前, 会先运行init-schema.sql脚本
public class CommentDAOTests {

    @Autowired
    private CommentDAO commentDAO;

    @Test
    public void initDatabase() {
        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setUserId(1);
            comment.setStatus(0);
            comment.setEntityId(1);
            comment.setEntityType(1);
            comment.setCreatedDate(new Date());
            comment.setContent("内容");
            commentDAO.addComment(comment);
        }

        List<Comment> comments = commentDAO.getCommentsByEntity(1, 1);
        for (Comment comment : comments)
            System.out.println(comment);

        System.out.println("评论数: " + commentDAO.getCommentCount(1, 1));
    }

}
