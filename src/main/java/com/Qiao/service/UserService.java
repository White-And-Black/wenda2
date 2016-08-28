package com.Qiao.service;

import com.Qiao.dao.LoginTicketDao;
import com.Qiao.dao.UserDAO;
import com.Qiao.model.LoginTicket;
import com.Qiao.model.User;
import com.Qiao.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.Qiao.util.WendaUtil.MD5;

/**
 * Created by white and black on 2016/7/18.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDao loginTicketDao;

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
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }
    public Map<String,String> login(String username,String password){
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
        if(user==null){
            map.put("msg","此用户不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword()))
        {
            map.put("msg","密码错误");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    public String addLoginTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date now=new Date();
        now.setTime(3600*1000*24+now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(loginTicket);
        return  loginTicket.getTicket();
    }
    public User getUser(int id){
        return  userDAO.selectById(id);
    }
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }
    public  User getUserByName(String name){
        return userDAO.selectByName(name);
    }
}
