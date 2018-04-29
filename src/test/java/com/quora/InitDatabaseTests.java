package com.quora;

import com.quora.dao.QuestionDAO;
import com.quora.dao.UserDAO;
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
 * To test the dao interface
 *
 * @author: leon
 * @date: 2018/4/26 18:35
 * @version: 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql") //加上这条语句每次测试之前, 会先运行init-schema.sql脚本
public class InitDatabaseTests {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private QuestionDAO questionDAO;
    @Test
    public void initDatabase() {
        Random rand = new Random();
        for (int i = 1; i < 11; i++) {
            User user = new User();
            user.setName("name" + i);
            user.setPassword("password" + i);
            user.setSalt(UUID.randomUUID().toString().substring(0, 10).replace("-", ""));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", rand.nextInt(1000)));
            userDAO.addUser(user);

            user.setName("leon");
            System.out.println("update: " + userDAO.updatePassword(user.getId(), "12345"));

            Question question = new Question();
            question.setTitle("title" + i);
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*i); //1000ms * 60 * 60 -> 一共是1个小时
            question.setCreatedDate(date);
            question.setContent("content" + i);
            question.setUserId(i);
            questionDAO.addUser(question);
            //questionDAO.deleteById(question.getId());
        }
        List<Question> questions = questionDAO.selectLatestQuestions(0, 1, 5);
        for (Question question : questions) {
            System.out.println(question);
        }
    }

}
