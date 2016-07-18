package com.Qiao.controller;

import com.Qiao.service.UserService;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.tomcat.util.log.UserDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by white and black on 2016/7/18.
 */
@Controller
public class LoginController {
    //private static final Logger logger=LoggerFactory.get
    @Autowired
    UserService userService;

    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model,
            @RequestParam("username") String username,
                          @RequestParam("password") String password){
        try{
            Map<String,String> map=userService.register(username,password);
            if(map.containsKey("msg")){
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
            return "redirect:/";
        }catch (Exception e){
            //logger.error("注册异常"+e.getMessage());
            return "login";
        }

    }
    @RequestMapping(path={"/","/reglogin"},method = {RequestMethod.GET})
    public String reg(Model model){
        return "login";
    }
}
