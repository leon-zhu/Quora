package com.quora.async.handler;

import com.quora.async.EventHandler;
import com.quora.async.EventModel;
import com.quora.async.EventType;
import com.quora.module.HostHolder;
import com.quora.module.Message;
import com.quora.module.User;
import com.quora.service.MessageService;
import com.quora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 点赞事件
 *
 * @author: leon
 * @date: 2018/5/8 16:24
 * @version: 1.0
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    @Override
    public void doHandler(EventModel model) {
        Message msg = new Message();
        msg.setFromId(model.getActorId());
        msg.setCreatedDate(new Date());
        msg.setToId(model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        msg.setContent("用户" + user.getName() + "攒了你的评论, " +
                "http:localhost:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(msg);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
