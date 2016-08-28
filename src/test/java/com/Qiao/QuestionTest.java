package com.Qiao;

import com.Qiao.dao.CommentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by white and black on 2016/8/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class QuestionTest {
    @Autowired
    CommentDao commentDao;
    @Test
    public void updateStatusTest(){
        commentDao.updateStatus(1,1,1);
    }

}
