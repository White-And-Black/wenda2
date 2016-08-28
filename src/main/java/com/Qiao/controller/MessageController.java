package com.Qiao.controller;

import com.Qiao.model.HostHolder;
import com.Qiao.model.Message;
import com.Qiao.model.User;
import com.Qiao.model.ViewObject;
import com.Qiao.service.MessageService;
import com.Qiao.service.UserService;
import com.Qiao.util.WendaUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by white and black on 2016/8/19.
 */
@Controller
public class MessageController {
    private static final Logger logger=Logger.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path={"/msg/list"},method ={RequestMethod.GET} )
    public String conversationList(Model model){
        try{
            int localUserId=hostHolder.getUser().getId();
            List<ViewObject> conversations=new ArrayList<ViewObject>();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("conversation",msg);
                int targetId=msg.getFromId()==localUserId?msg.getToId():msg.getFromId();
                User user=userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content")String content){
        try{
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(999,"未登录");
            }
            User user=userService.getUserByName(toName);
            if(user==null){
                return WendaUtil.getJSONString(1,"用户名不存在");
            }
            Message msg=new Message();
            msg.setContent(content);
            msg.setFromId(hostHolder.getUser().getId());
            msg.setToId(user.getId());
            msg.setCreatedDate(new Date());
            msg.setConversationId(msg.getFromId()<msg.getToId()?String.format("%d_%d",msg.getFromId(),msg.getToId()):String.format("%d_%d",msg.getToId(),msg.getFromId()));
            messageService.addMessage(msg);
            return WendaUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("增加站内信失败"+e.getMessage());
            return WendaUtil.getJSONString(1,"插入站内信失败");
        }
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getMessageDetail(Model model, @Param("conversationId") String conversationId){
        try{
            List<Message> conversationDetails=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages=new ArrayList<ViewObject>();
            for(Message msg:conversationDetails){
                ViewObject vo=new ViewObject();
                vo.set("message",msg);
                User user=userService.getUser(msg.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取聊天信息失败"+e.getMessage());
        }
        return "letterDetail";
    }
    @RequestMapping(path = {"/msg/jsonAddMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addComment(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try{
            Message msg=new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            msg.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
            messageService.addMessage(msg);
            return WendaUtil.getJSONString(msg.getId());
        }catch(Exception e){
            logger.error("增加评论失败"+e.getMessage());
            return WendaUtil.getJSONString(1,"插入评论失败");
        }
    }
}
