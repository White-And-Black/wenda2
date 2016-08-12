package com.Qiao.service;

import com.Qiao.dao.QuestionDao;
import com.Qiao.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by white and black on 2016/8/11.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
}
