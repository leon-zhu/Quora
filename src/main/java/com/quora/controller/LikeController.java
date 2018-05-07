package com.quora.controller;

import com.quora.module.EntityType;
import com.quora.module.HostHolder;
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


    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null)
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        long likeCount = likeService.like(hostHolder.getUser().getId(), commentId, EntityType.ENTITY_COMMENT); //commentId: 某条评论的赞踩
        return QuoraUtils.getJSONString(0, String.valueOf(likeCount));

    }

    @RequestMapping(path = "/dislike", method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null)
            return QuoraUtils.getJSONString(QuoraUtils.ANONYMOUS_USER_ID);
        long dislikeCount = likeService.dislike(hostHolder.getUser().getId(), commentId, EntityType.ENTITY_COMMENT); //commentId: 某条评论的赞踩
        return QuoraUtils.getJSONString(0, String.valueOf(dislikeCount));

    }

}
