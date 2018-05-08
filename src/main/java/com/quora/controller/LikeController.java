package com.quora.controller;

import com.quora.async.EventModel;
import com.quora.async.EventProducer;
import com.quora.async.EventType;
import com.quora.module.EntityType;
import com.quora.module.HostHolder;
import com.quora.service.CommentService;
import com.quora.service.LikeService;
import com.quora.util.QuoraUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/7 14:13
 * @version: 1.0
 */
@Controller
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private CommentService commentService;


    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null)
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);


        eventProducer.fireEvent(new EventModel().setEntityType(EntityType.ENTITY_COMMENT)
        .setType(EventType.LIKE).setEntityId(commentId).setActorId(hostHolder.getUser().getId()));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId); //commentId: 某条评论的赞踩
        return QuoraUtils.getJSONString(0, String.valueOf(likeCount));

    }

    @RequestMapping(path = "/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null)
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        long dislikeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId); //commentId: 某条评论的赞踩
        return QuoraUtils.getJSONString(0, String.valueOf(dislikeCount));

    }

}
