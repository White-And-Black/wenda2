package com.Qiao.dao;

import com.Qiao.model.LoginTicket;
import com.Qiao.model.Question;
import org.apache.ibatis.annotations.*;

/**
 * Created by white and black on 2016/8/11.
 */
@Mapper
public interface LoginTicketDao {
    String TABLE_NAME="login_ticket";
    String INSERT_FIELDS="user_id,expired,status,ticket";
    String SELECT_FIELDS="id,user_id,expired,status,ticket";
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")" +
            "values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
    LoginTicket findByTicket(String ticket);

    @Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
