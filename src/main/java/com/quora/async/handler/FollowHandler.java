package com.quora.async.handler;

import com.quora.async.EventHandler;
import com.quora.async.EventModel;
import com.quora.async.EventType;
import com.quora.module.EntityType;
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
 * description here
 *
 * @author: leon
 * @date: 2018/5/9 22:40
 * @version: 1.0
 */
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        Message msg = new Message();
        msg.setFromId(model.getActorId());
        msg.setCreatedDate(new Date());
        msg.setToId(model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.ENTITY_QUESITON) {
            msg.setContent("用户" + user.getName() + "关注了你的问题, " +
                    "http:localhost:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
            msg.setContent("用户" + user.getName() + "关注了你, " +
                    "http:localhost:8080/user/" + model.getActorId());
        }

        messageService.addMessage(msg);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
