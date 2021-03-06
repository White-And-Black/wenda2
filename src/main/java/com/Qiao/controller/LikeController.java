package com.Qiao.controller;

import com.Qiao.async.EventModel;
import com.Qiao.async.EventProducer;
import com.Qiao.async.EventType;
import com.Qiao.model.Comment;
import com.Qiao.model.EntityType;
import com.Qiao.model.HostHolder;
import com.Qiao.service.CommentService;
import com.Qiao.service.LikeService;
import com.Qiao.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by white and black on 2016/8/28.
 */


@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;
    @RequestMapping(path = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
                .setEntityId(commentId).setEntityType(EntityType.ENTITY_COMMENT)
        .setEntityOwnerId(comment.getUserId()).setExt("questionId",String.valueOf(comment.getEntityId())));
        long likeCount=likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,
                commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        long likeCount=likeService.like(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,
                commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

}
