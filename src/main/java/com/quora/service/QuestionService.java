package com.quora.service;

import com.quora.dao.QuestionDAO;
import com.quora.module.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private QuestionDAO questionDAO;

    public List<Question> getLateseQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

}
