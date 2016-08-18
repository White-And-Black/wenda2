package com.Qiao.controller;

import com.Qiao.dao.UserDAO;
import com.Qiao.model.User;
import com.Qiao.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.tomcat.util.log.UserDataHelper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;



/**
 * Created by white and black on 2016/7/18.
 */
@Controller
public class LoginController {
    private static final Logger logger=Logger.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @RequestMapping(path={"/reg"},method = {RequestMethod.POST})
    public String reg(Model model,
                      HttpServletResponse response,
                      @RequestParam(value="next",required = false)String next,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rememberme",defaultValue = "false") boolean rememberme
                      ) {
        try{
            Map<String,String> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }

    }
    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})
    public String reglogin(Model model,@RequestParam(value="next",required = false)String next){
        model.addAttribute("next",next);
        return "login";
    }
    @RequestMapping(path={"/login"},method = {RequestMethod.POST})
    public String login(Model model,
                        HttpServletResponse response,
                        @RequestParam(value = "next",required = false)String next,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rememberme",defaultValue = "false") boolean rememberme
                        ){
        try{
            Map<String,String> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e) {
            logger.error("登陆异常" + e.getMessage());
            return "login";
        }
    }
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
