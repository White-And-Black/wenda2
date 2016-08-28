package com.Qiao.service;

import org.springframework.stereotype.Service;

/**
 * Created by white and black on 2016/8/28.
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message:"+String.valueOf(userId);
    }
}
