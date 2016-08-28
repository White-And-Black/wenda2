package com.Qiao.async.handler;

import com.Qiao.async.EventHandler;
import com.Qiao.async.EventModel;
import com.Qiao.async.EventType;
import com.Qiao.model.Message;
import com.Qiao.model.User;
import com.Qiao.service.MessageService;
import com.Qiao.service.UserService;
import com.Qiao.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by white and black on 2016/8/28.
 */
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user=userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的评论，http://127,0.0.1:8080/question/"
                +model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupporEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
