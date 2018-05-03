package com.quora.service;

import com.quora.dao.QuestionDAO;
import com.quora.module.HostHolder;
import com.quora.module.Question;
import com.sun.org.apache.xpath.internal.operations.Quo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        question.setTitle(title);
        question.setContent(content);
        question.setCreatedDate(new Date());
        question.setCommentCount(0);
        if (hostHolder.getUser() != null)
            question.setUserId(hostHolder.getUser().getId());
        else
            question.setUserId(QuoraUtils.ANONYMOUS_USER_ID); //未登录, 实际上匿名用户不允许提问, 只有登录用户才看的到提问按钮
        return addQuestion(question);
    }

}
