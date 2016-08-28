package com.Qiao.async;

import com.Qiao.util.JedisAdapter;
import com.Qiao.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by white and black on 2016/8/28.
 */
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;
    public boolean fireEvent(EventModel eventModel){
        try {
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }
}
