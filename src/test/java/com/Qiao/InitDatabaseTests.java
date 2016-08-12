package com.Qiao;

import com.Qiao.dao.QuestionDao;
import com.Qiao.dao.UserDAO;
import com.Qiao.model.Question;
import com.Qiao.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by white and black on 2016/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDAO userDAO;
    @Autowired
    QuestionDao questionDao;

    @Test
    public void contextLoads() {
       Random random=new Random();
        for(int i=0;i<11;i++){
            User user=new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setName(String.format("USER%d",i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            Question question=new Question();
            question.setCommentCount(i);
            Date date=new Date();
            date.setTime(date.getTime()+1000*3600*i);
            question.setCreatedDate(date);
            question.setUserId(i+1);
            question.setTitle(String.format("Title{%d}",i));
            question.setContent(String.format("Hahahahah content %d",i));
            questionDao.addQuestion(question);
        }
        Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
        System.out.println("shishangwunanshi!");
        System.out.println(questionDao.selectLatestQuestions(0,0,10));

    }
}
