package com.quora.controller;

import com.quora.dao.QuestionDAO;
import com.quora.module.Question;
import com.quora.service.QuestionService;
import com.quora.service.QuoraUtils;
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
 * @date: 2018/5/2 19:46
 * @version: 1.0
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/question/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String add(@RequestParam("title") String title, @RequestParam("content") String content) {

        try {
            if (questionService.addQuestion(title, content) > 0) {
                return QuoraUtils.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("add question failed: " + e.getMessage());
        }

        return QuoraUtils.getJSONString(1, "failed");
    }

}
