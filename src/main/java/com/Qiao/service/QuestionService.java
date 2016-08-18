package com.Qiao.service;

import com.Qiao.dao.QuestionDao;
import com.Qiao.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by white and black on 2016/8/11.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    @Autowired
    SensitiveService sensitiveService;
    public int addQuestion(Question question){
        //敏感词过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question)>0?question.getId():0;
    }


    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }

    public int updateCommentCount(int id,int count){
       return questionDao.updateCommentCount(count,id);
    }
    public Question getById(int id){
        return questionDao.selectById(id);
    }
}
