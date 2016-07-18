package com.Qiao.controller;

import com.Qiao.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by white and black on 2016/7/16.
 */
//@Controller
public class IndexController {
    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession session){
        return "Hello world!"+session.getAttribute("msg");
    }

    @RequestMapping(path={"/profile/{userId}/{group}"},method = {RequestMethod.GET})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId, @PathVariable("group") String group,
                          @RequestParam(value="type",defaultValue = "10",required = true) int type,
                          @RequestParam(value="key",defaultValue = "null",required = true) String key){
        return String.format("profile page of %d  %s type=%d key=%s",userId,group,type,key);
    }


    @RequestMapping(path={"/home"})
    public String template(Model model){
        model.addAttribute("value1","hero");
        List<String> colors= Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("Colors",colors);
        return "home";
    }

    @RequestMapping(path={"/dream"},method = {RequestMethod.GET})
    public String template1(Model model){
        model.addAttribute("value1","hero");
        List<String> colors= Arrays.asList(new String[]{"RED","GREEN","BLUE"});
        model.addAttribute("Colors",colors);

        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++)
            map.put(String.valueOf(i),String.valueOf(i*i));
        model.addAttribute("Map",map);
        //User user1=new User("qiao");
        //user1.setDescription("This is a good boy!");
        //model.addAttribute("User.java",user1);
        return "dream";
    }
    @RequestMapping(path = {"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response, HttpServletRequest request,
                            HttpSession session,@CookieValue("JSESSIONID") String sessionId){
        StringBuilder sb=new StringBuilder();
        sb.append("CookieValue:"+sessionId+"<br>");
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br");
        }
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                sb.append("Cookie:"+cookie.getName()+"value:"+cookie.getValue()+"<br>");
            }
        }
        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");

        response.addHeader("QiaoId","hello qiao");
        response.addCookie(new Cookie("UserName","qiao142857"));
        return sb.toString();
    }
    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,HttpSession httpSession){
        httpSession.setAttribute("msg","jump from redirect");
        RedirectView red=new RedirectView("/index",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return "redirect:/";
    }
}
