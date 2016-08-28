package com.Qiao.service;

import com.Qiao.dao.MessageDao;
import com.Qiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by white and black on 2016/8/19.
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public int addMessage(Message message){
        messageDao.addMessage(message);
        return 1;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }
}
