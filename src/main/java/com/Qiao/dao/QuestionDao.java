package com.Qiao.dao;


import com.Qiao.model.Question;
import com.Qiao.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by white and black on 2016/7/18.
 */
@Mapper
public interface QuestionDao {
    String TABLE_NAME="question";
    String INSERT_FIELDS="title,content,created_date,user_id,comment_count";
    String SELECT_FIELDS="id,title,content,created_date,user_id,comment_count";
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")" +
            "values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId")int userId,
                                        @Param("offset") int offset,
                                        @Param("limit")int limit);

}
