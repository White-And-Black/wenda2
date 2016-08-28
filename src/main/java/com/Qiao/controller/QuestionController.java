package com.Qiao.controller;


import com.Qiao.model.*;
import com.Qiao.service.CommentService;
import com.Qiao.service.LikeService;
import com.Qiao.service.QuestionService;
import com.Qiao.service.UserService;
import com.Qiao.util.WendaUtil;
import org.apache.coyote.http11.filters.VoidInputFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by white and black on 2016/8/12.
 */
@Controller
public class QuestionController {
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    private static final Logger logger=Logger.getLogger(QuestionController.class);

    @RequestMapping(value="/question/{qid}",method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            if(hostHolder.getUser()==null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));

            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            vos.add(vo);
        }
        model.addAttribute("comments",vos);
        return "detail";
    }

    @RequestMapping(value="/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
        try{
            Question question=new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null){
                question.setUserId(WendaUtil.ANO_USERID);
                //return WendaUtil.getJSONString(999);
            }else
            question.setUserId(hostHolder.getUser().getId());
            if(questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0);
            }
            questionService.addQuestion(question);
        }catch (Exception e){
            logger.error("增加题目失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }
}
