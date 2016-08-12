package com.Qiao.controller;

import com.Qiao.dao.UserDAO;
import com.Qiao.model.Question;
import com.Qiao.model.ViewObject;
import com.Qiao.service.QuestionService;
import com.Qiao.service.UserService;
import org.apache.log4j.Logger;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by white and black on 2016/8/11.
 */
@Controller
public class HomeController {
    private static final Logger logger=Logger.getLogger(HomeController.class);
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserDAO userDAO;

    @RequestMapping(path={"/","/home"},method={RequestMethod.GET})
    public String home(Model model, @RequestParam(value="pop",defaultValue = "0")int pop){
        List<ViewObject> vos=getQuestions(0,0,10);
        model.addAttribute("vos",vos);
        //model.addAttribute("user",)
        return "index";
    }
    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET})
    public String userHome(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestions(userId,0,10));
        return "index";
    }
    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questions=questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(Question question:questions){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

}
