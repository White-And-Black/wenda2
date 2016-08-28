package com.Qiao.controller;

import com.Qiao.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by white and black on 2016/8/28.
 */
@Controller
public class SettingController {
    @Autowired
    WendaService wendaService;
    @RequestMapping(path = {"/setting"},method = {RequestMethod.GET})
    @ResponseBody
    public String getting(HttpSession httpSession){
        return "Setting OK."+wendaService.getMessage(1);
    }
}
