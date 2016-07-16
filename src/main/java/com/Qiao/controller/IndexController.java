package com.Qiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by white and black on 2016/7/16.
 */
@Controller
public class IndexController {
    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(){
        return "Hello world!";
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

        return "dream";
    }
}
