package com.quora;

import com.quora.dao.MessageDAO;
import com.quora.module.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 15:10
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql") //加上这条语句每次测试之前, 会先运行init-schema.sql脚本
public class MessageDAOTests {

    @Autowired
    private MessageDAO messageDAO;

    @Test
    public void initDatabase() {
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setFromId(i);
            message.setToId(i);
            message.setContent("content");
            message.setCreatedDate(new Date());
            //message.setConversationId(String.format("%d-%d", i, i));
            message.setHasRead(i);
            messageDAO.addMessage(message);
        }

        List<Message> messageList = messageDAO.getConversationDetail("1-1", 0, 1);
        for (Message message : messageList)
            System.out.println(message);
    }
}
