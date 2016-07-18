package com.Qiao.service;

import com.Qiao.dao.UserDAO;
import com.Qiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.Qiao.util.WendaUtil.MD5;

/**
 * Created by white and black on 2016/7/18.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public Map<String,String> register(String username, String password){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已存在");
            return map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        password=password+user.getSalt();
        user.setPassword(MD5(password));
        userDAO.addUser(user);
        return map;
    }

    public User getUser(int id){
        return  userDAO.selectById(id);
    }
}
