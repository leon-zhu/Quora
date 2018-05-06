package com.quora.controller;

import com.quora.module.HostHolder;
import com.quora.module.Message;
import com.quora.module.User;
import com.quora.module.ViewObject;
import com.quora.service.MessageService;
import com.quora.util.QuoraUtils;
import com.quora.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/5 14:47
 * @version: 1.0
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        try {
            if (hostHolder.getUser() == null)
                return "redirect:/reglogin";
            int userId = hostHolder.getUser().getId();
            List<Message> messageList = messageService.getConversationList(userId, 0, 10);
            List<ViewObject> conversations = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject obj = new ViewObject();
                obj.set("message", message);
                int targetId = message.getFromId() == userId?message.getToId():userId;
                User user = userService.getUser(targetId); //私信的对方Id
                obj.set("user", user);
                obj.set("unreadCount", messageService.getConversationUnreadCount(userId, message.getConversationId()));
                conversations.add(obj);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取列表失败: " + e.getMessage());
            return "redirect:/";
        }

    }

    @RequestMapping(path = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            //相应的私信标记为已读
            messageService.updateConversationUnreadCount(hostHolder.getUser().getId(), conversationId);

            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取详情失败: " + e.getMessage());
            return "redirect:/";
        }

    }


    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                            @RequestParam("content") String content) {

        try {
            if (hostHolder.getUser() == null) {
                return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID, "未登录");
            }
            User user = userService.getUser(toName);
            if (user == null)
                return QuoraUtils.getJSONString(1, "用户不存在");

            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            System.out.println(message);
            return QuoraUtils.getJSONString(0);
        } catch (Exception e) {
            logger.error("发信失败: " + e.getMessage());
            return QuoraUtils.getJSONString(1, "发信失败");
        }

    }

}
