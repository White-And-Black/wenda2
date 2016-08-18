package com.Qiao.controller;

import com.Qiao.model.Comment;
import com.Qiao.model.EntityType;
import com.Qiao.model.HostHolder;
import com.Qiao.service.CommentService;
import com.Qiao.service.QuestionService;
import com.Qiao.service.SensitiveService;
import com.Qiao.service.UserService;
import com.Qiao.util.WendaUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;


/**
 * Created by white and black on 2016/8/17.
 */
@Controller
public class CommentController {
    private static final Logger logger= Logger.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(path={"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,@RequestParam("content")String content){
        try{
            content= HtmlUtils.htmlEscape(content);
            content=sensitiveService.filter(content);
            Comment comment=new Comment();
            if(hostHolder.getUser()!=null){
                comment.setUserId(hostHolder.getUser().getId());
            }else{
                comment.setUserId(WendaUtil.ANO_USERID);
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
            int count =commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            //这里有可能会出错，通过EntityId无法判断Entity对象，还需考虑entityType
            questionService.updateCommentCount(comment.getEntityId(),count);
        }catch (Exception e){
//            e.printStackTrace();
            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/question/"+String.valueOf(questionId);
    }



}
