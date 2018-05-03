package com.quora.service;

import com.quora.dao.QuestionDAO;
import com.quora.module.HostHolder;
import com.quora.module.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/4/26 21:09
 * @version: 1.0
 */
@Service
public class QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private HostHolder hostHolder;

    public List<Question> getLateseQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question) {
        return questionDAO.addQuestion(question);
    }

    public int addQuestion(String title, String content) {
        Question question = new Question();
        //对title进行html标签过滤
        question.setTitle(HtmlUtils.htmlEscape(title)); //底层实际上就是对<, >, /, "做转义
        //对content进行html标签过滤
        question.setContent(HtmlUtils.htmlEscape(content));
        question.setCreatedDate(new Date());
        question.setCommentCount(0);
        question.setUserId(hostHolder.getUser().getId()); //Controller已经判断用户已登录

        return addQuestion(question);
    }

}
