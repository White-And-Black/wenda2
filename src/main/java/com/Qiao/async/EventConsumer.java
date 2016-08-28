package com.Qiao.async;

import com.Qiao.util.JedisAdapter;
import com.Qiao.util.RedisKeyUtil;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import org.aspectj.lang.annotation.AdviceName;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by white and black on 2016/8/28.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
    private static final Logger logger= Logger.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandler>> config=new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null){
            for (Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> eventTypes=entry.getValue().getSupporEventType();
                for(EventType type:eventTypes){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key= RedisKeyUtil.getEventQueueKey();
                    List<String> events=jedisAdapter.brpop(0,key);
                    for(String message:events){
                        if(message.equals(key)){
                            continue;
                        }
                        EventModel eventModel= JSON.parseObject(message,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for(EventHandler handler:config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
